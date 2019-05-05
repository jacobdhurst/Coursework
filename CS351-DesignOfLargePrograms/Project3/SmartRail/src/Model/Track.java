/*
 * CS351L Project #2: SmartRail
 * Jacob Hurst & Jaehee Shin
 * 10/18/17
 *
 * Track.java - Provides functionality for the generic tracks.
 */

package Model;

import View.TrackView;

import java.util.ArrayList;

public class Track extends Thread
{
  private final int index;
  private String message;

  private Train train;
  private volatile String lock;

  private boolean messageReceived, ready;

  private Track neighborL, neighborR;

  private final ArrayList<String> path = new ArrayList<>();
  private final ArrayList<Track> secureList = new ArrayList<>();
  private int entered = 0;

  private TrackView trackView;


  /**
   * Default constructor - takes name & index.
   *
   * @param name
   * @param index
   */
  public Track(String name, int index)
  {
    super(name);

    this.index = index;

    lock = " ";

    messageReceived = false;
    ready = false;
  }
  
  /**
   * Run method - controls the tracks actions.
   */
  public void run()
  {
    while (true)
    {
      synchronized (this)
      {
        try
        {
          if (messageReceived)
          {
            handleMessage();
            messageReceived = false;
            message = null;
          }
          wait();
        }
        catch (InterruptedException ignored) {}
      }
    }
  }
  
  /**
   * Accepts a message and sets flag for this thread to recognize message.
   *
   * @param message
   */
  public synchronized void acceptMessage(String message)
  {
    messageReceived = true;
    this.message = message;
  }
  
  /**
   * @return true if this has a message to process.
   */
  public boolean receivedMessage()
  {
    return messageReceived;
  }
  
  /**
   * @return most recent message received.
   */
  public String getMessage()
  {
    return getName() + "-" + getIndex() + " received:\n\"" + message + "\".";
  }
  
  /**
   * Sets the train currently occupying this track.
   *
   * @param train
   */
  public void setTrain(Train train)
  {
    this.train = train;
  }
  
  /**
   * @return train currently occupying this track.
   */
  public Train getTrain()
  {
    return train;
  }

  /**
   * @param side
   * @return neighbor at given side.
   */
  public Track getNeighbor(String side)
  {
    switch(side) {
      case "L":
        return neighborL;
      case "R":
        return neighborR;
      case "D":
        return ((Switch) this).getConnection();
      case "U":
        return ((Switch) this).getConnection();
    }
    return null;
  }
  
  /**
   * @param direction
   * @return whether this is an endpoint.
   */
  public boolean isEndPoint(String direction)
  {
    return (getNeighbor(direction) == null);
  }
  
  /**
   * Sets view component for this thread.
   *
   * @param trackView
   */
  public void setTrackView(TrackView trackView)
  {
    this.trackView = trackView;
  }
  
  /**
   * @return view component for this thread.
   */
  public TrackView getTrackView()
  {
    return trackView;
  }
  
  /**
   * @return the lock-holder of this track.
   */
  public String getLock()
  {
    return lock;
  }
  
  /**
   * @return whether a path exists.
   */
  public boolean isReady()
  {
    return ready;
  }
  
  /**
   * Sets the neighboring to the left.
   *
   * @param track
   */
  void setNeighborL(Track track)
  {
    neighborL = track;
  }

  /**
   * Sets the neighboring track to the right.
   *
   * @param track
   */
  void setNeighborR(Track track)
  {
    neighborR = track;
  }
  
  /**
   * @return index of this track.
   */
  private int getIndex()
  {
    return index;
  }
  
  
  /**
   * Handles messages as they are received.
   */
  private synchronized void handleMessage()
  {


    /* train is checking if a path exists before starting */
    if (message.contains("pf:"))
    {
      entered = 0;
      path.clear();
      ready = pathFinder(this, message.substring(3), (getNeighbor("L") == null) ? "R" : "L", 0);
      if (path.size() <= 1) ready = false;
    }
    /* if the train has asked to move either permit the move, inform it to switch, or don't permit requested move */
    else if (message.contains("traverse:"))
    {
      if (this instanceof Light && ((Light) this).getLight().equals("red"))
      {
        train.acceptMessage("wait");
      }

      String direction = message.substring(9);
      String move = "unavailable";

      /* track is unlocked early so track cannot find its train */


      if (getNeighbor(direction) != null && getNeighbor(direction).getLock().equals(train.getName())) move = "available";
      else if (this instanceof Switch) move = "switch";
      if (!move.equals("switch") && this instanceof Switch) ((Switch)this).getConnection().setLock(" ");
      train.acceptMessage(move);
    }
    /* train is leaving this track and is requesting this is now unlocked */
    else if (message.contains("unlock:"))
    {
      if (this instanceof Switch)
      {
        String direction = message.substring(7);
        Track connection = ((Switch) this).getConnection();
        ((Switch) connection).setNeighboringLights((direction.equals("L") ? "R" : "L"), "green");
        ((Switch) this).setNeighboringLights(direction, "green");
      }
      if (this.getLock().equals(this.train.getName())) setLock(" ");
      setTrain(null);
    }
    /* train is requesting a path from it's departure to arrival tracks */
    else if (message.contains("pathFinder:"))
    {
      message = message.substring(11);
      String station[] = message.split("\\-");

      entered = 0;
      path.clear();

      if(train == null)
      {
        this.setLock(" ");
        return;
      }

      /* finding & securing path if it exists */
      boolean validRequest = !(this.getName().equals(station[1])   ||
                               this.getName().equals("NO STATION") ||
                               station[0].equals(station[1]));
      if (validRequest && pathFinder(this, station[1], determineDirection(), 0))
      {
        boolean securedPath = securePath(determineDirection());
        if(securedPath) train.acceptMessage("secured:" + determineDirection());
        else train.acceptMessage("!pathFound");
        path.clear();
      }
      else train.acceptMessage("!pathFound");
    }
  }
  
  /**
   * Should only be called when train is at an edge
   *
   * @return direction to travel.
   */
  private String determineDirection()
  {
    return (neighborL == null) ? "R" : "L";
  }
  
  /**
   * Finds a path from the given departure to arrival recursively.
   *
   * @param departure
   * @param arrival
   * @param direction
   * @return
   */
  private synchronized boolean pathFinder(Track departure, String arrival, String direction, int increment) {
    increment += 1;
    if(entered > 64 || increment > 64) {
      return false;
    }
    if(departure == null) {
      entered++;
      if(path.size() > 0) path.remove(path.size() - 1);
      return false;
    }else if(path.contains(String.valueOf(departure.getId()))) return false;
  
    boolean fromTrain = !(message.contains("pf:") || departure.getLock().equals(" "));
    if(fromTrain && departure.getName().equals(arrival)) return false;
    if(fromTrain && !departure.getLock().equals(train.getName())) {
      if(path.size() > 0) path.remove(path.size() - 1);
      return false;
    }
  
    if(path.size() > 0 && !path.get(path.size() - 1).equals(String.valueOf(departure.getId()))) path.add(String.valueOf(departure.getId()));
    else if(path.size() == 0) path.add(String.valueOf(departure.getId()));
    else return false;
  
    Track connection = null;
    if(departure instanceof Switch) {
      connection = ((Switch) departure).getConnection();
    }
    return departure.getName().equals(arrival) || pathFinder(departure.getNeighbor(direction), arrival, direction, increment) || pathFinder(connection, arrival, direction, increment);
  }

  /**
   * Secures the given path sets locks, lights, and switches as needed.
   *
   * @param direction
   */
  private synchronized boolean securePath(String direction)
  {
    Track track = this;

    for (int i = 0; i < path.size(); i++)
    {
      if (track == null) return false;
      track.messageReceived = false;
      if(!(track.getLock().equals(" ") || track.getLock().equals(train.getName())))
      {
        int n = 0;
        while (n < secureList.size())
        {
          if(secureList.get(n).getLock().equals(train.getName()))
          {
            if(secureList.get(n) instanceof Switch) secureList.get(n).setLock(track.getLock());
            else secureList.get(n).setLock(" ");
          }
          n++;
        }
        return false;
      }
      if(train != null) track.setLock(this.train.getName());
      else return false;

      secureList.add(track);

      if (track instanceof Switch)
      {
        ((Switch) track).setNeighboringLights(direction, "red");
        track = ((Switch) track).getConnection();
        ((Switch) track).setNeighboringLights((direction.equals("R")) ? "L" : "R", "red");

        track.setLock(this.train.getName());
        secureList.add(track);

        String next = path.get(i + 1);

        Track switchNeighbor = track.getNeighbor(direction);
        if (switchNeighbor instanceof Switch) switchNeighbor = ((Switch) switchNeighbor).getConnection();
        
        /* a bit convoluted but here I am just checking whether the path does not use this switch to reach destination */
        if (!next.equals(String.valueOf(track.getNeighbor(direction).getId())) &&
                !next.equals(String.valueOf(switchNeighbor.getId())))
        {
          track = ((Switch) track).getConnection();
        }
      }
      track = track.getNeighbor(direction);
    }

    if(secureList.size() > 0) secureList.clear();
    return true;
  }
  
  /**
   * Sets this track as locked by given train.
   *
   * @param lock
   */

  private synchronized void setLock(String lock)
  {
    this.lock = lock;
  }
}