/*
 * CS351L Project #2: SmartRail
 * Jacob Hurst & Jaehee Shin
 * 10/18/17
 *
 * Train.java - Provides functionality for the trains.
 */

package Model;

import View.TrainView;

public class Train extends Thread
{
  private Track arrival, current, departure;

  private String direction, translate, message;

  private boolean pathFound, messageReceived, moved;

  private TrainView trainView;

  /**
   * Default constructor
   *
   * @param name
   */
  public Train(String name)
  {
    super(name);

    messageReceived = false;
    pathFound = false;
    moved = false;
  }
  
  /**
   * Run method - controls the trains actions.
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
    return getName() + " received:\n\"" + message + "\".";
  }
  
  /**
   * Sets the current this train will depart from.
   *
   * @param departure
   */
  public void setDeparture(Track departure)
  {
    this.departure = departure;
    this.current = departure;

    direction = translate = determineDirection();
    trainView.changeDirection(translate);
  }
  
  /**
   * Sets view component for this thread.
   *
   * @param trainView
   */
  public void setTrainView(TrainView trainView)
  {
    this.trainView = trainView;
  }
  
  /**
   * @return view component for this thread.
   */
  public TrainView getTrainView()
  {
    return trainView;
  }
  
  /**
   * @return direction train should translate.
   */
  public String getTranslate()
  {
    return this.translate;
  }
  
  /**
   * @return train index
   */
  public int getIndex()
  {
    return Integer.parseInt(getName().substring(6));
  }
  
  /**
   * @return whether or not the train has just moved.
   */
  public boolean isMoved()
  {
    return moved;
  }
  
  /**
   * @return direction train should travel.
   */
  private String determineDirection()
  {
    if (message != null && message.equals("switch")) return "S";
    else if (current.getNeighbor("L") == null) return "R";
    else if (current.getNeighbor("R") == null) return "L";
    
    if (message == null) return "";
    else return direction;
  }
  
  /**
   * Changes the direction, departure, and arrival of this train.
   */
  private synchronized void changeDirection()
  {
    Track temp = departure;
    departure = current;
    arrival = temp;
    
    direction = translate = determineDirection();
    trainView.changeDirection(translate);
  }
  
  /**
   * Handles messages as they are received.
   */
  private synchronized void handleMessage()
  {
    current.setTrain(this);
    /* train is at a red light */
    if (message.equals("wait")) return;

    /* train is at endpoint and should turn around */
    if (current.isEndPoint(direction))
    {
      if (trainView.getCurrentTrack().getNeighbor(direction) == null)
      {
        moved = false;
        changeDirection();
      }
      else
      {
        moved = true;
        pathFound = false;
      }
    }
    /* train was permitted to move, available = standard left or right, switch = take switch connection */
    else if (message.equals("available") || message.equals("switch") || message.equals("unavailable"))
    {
      moved = !trainView.isEndPoint() ||
              current.getName().equals("Track") ||
              current.getName().equals("Light") ||
              current.getName().equals("Switch");

      current.acceptMessage("unlock:" + direction);

      switch (message)
      {
        case "available":
          current = current.getNeighbor(direction);
          translate = direction;
          break;
        case "switch":
          if (trainView.getCurrentTrack().getId() != current.getId()) break;
          if (current.getId() > ((Switch) current).getConnection().getId()) translate = "U";
          else translate = "D";

          current = ((Switch) current).getConnection();
          break;
        default:
          /* train must've gotten lost somehow, rerouting... */
          pathFound = false;
          moved = false;
          break;
      }
      current.setTrain(this);
    }
    /* trains requested route was found & secured */
    else if (message.contains("secured"))
    {
      if (message.endsWith("R")) direction = translate = "R";
      else if (message.endsWith("L")) direction = translate = "L";

      pathFound = true;
    }
    
    /* train has not found a path yet, requesting a path to destination */
    String arrivalName = (arrival == null) ? trainView.getArrival() : arrival.getName();
    if (!pathFound) current.acceptMessage("pathFinder:" + departure.getName() + "-" + arrivalName);
    
    /* train has a path and is requesting to move from current current in a direction */
    if (pathFound) current.acceptMessage("traverse:" + direction);
  }
}
