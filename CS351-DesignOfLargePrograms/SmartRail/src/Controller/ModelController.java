/*
 * CS351L Project #2: SmartRail
 * Jacob Hurst & Jaehee Shin
 * 10/20/17
 *
 * ModelController.java - Controller for train, track, light, & switch threads.
 * Creates threads from the given configuration & adds a track to each rail (not started until set on station).
 */

package Controller;

import Model.*;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Thread.sleep;

public class ModelController
{
  /* only used to connect switches prior to running */
  private final ArrayList<String> map = new ArrayList<>();
  private final ArrayList<Train> trains = new ArrayList<>();
  private int activeCount = 0;
  private Rail rail[];

  /**
   * Default constructor - takes a configuration.
   *
   * @param configuration
   */
  ModelController(Configuration configuration)
  {
    buildConfiguration(configuration);
    setSwitches();

    for (int i = 0; i < configuration.getHeight(); i++)
    {
      addTrain(i);
    }
  }
  
  /**
   * Method to start a train thread.
   *
   * @param index
   */
  public void startTrain(int index, String side)
  {
    int dep = side.equals("L") ? 0 : rail[index].getTracks().length - 1;
    int arr = (dep == 0) ? rail[index].getTracks().length - 1 : 0;

    Train train = trains.get(index);
    Track departing = rail[index].getTracks()[dep];
    train.setDeparture(departing);
    train.getTrainView().setCurrentTrack(departing);

    train.getTrainView().setTrainX((dep + 1) * 100);

    if (train.getTrainView().getArrival().equals("CANCEL")) return;
    while (!departing.isReady())
    {
      if (train.getTrainView().getArrival().equals("CANCEL")) return;
      if (!departing.getName().equals(train.getTrainView().getArrival()))
      {
        departing.acceptMessage("pf:" + train.getTrainView().getArrival());
      }

      synchronized (departing)
      {
        departing.notify();
      }

      synchronized (this)
      {
        try
        {
          sleep(100);
        }
        catch (InterruptedException ignored)
        {
        }
      }
      if (!departing.isReady()) train.getTrainView().error();
    }

    int dest[] = determineArriving(train.getTrainView().getArrival());
    rail[index].getTracks()[dep].getTrackView().stationDisabled(true);
    rail[index].getTracks()[arr].getTrackView().stationDisabled(true);
    rail[dest[0]].getTracks()[dest[1]].getTrackView().stationDisabled(true);

    departing.setTrain(trains.get(index));
    train.setDeparture(departing);

    train.getTrainView().setVisible(true);
    train.acceptMessage("start");
    train.start();
    activeCount++;
  }
  
  /**
   * @return array of rails (rails are collections of tracks on a line).
   */
  Rail[] getRails()
  {
    return rail;
  }
  
  
  /**
   * @return array of active trains.
   */
  ArrayList<Train> getTrains()
  {
    return trains;
  }
  
  
  /**
   * @return count of active trains.
   */
  int getActiveCount()
  {
    return activeCount;
  }
  
  /**
   * Builds the given configuration and sets up component threads.
   *
   * @param configuration
   */
  private void buildConfiguration(Configuration configuration)
  {
    String start, tracks, end;
    int index = configuration.getHeight();
    
    rail = new Rail[index];
    
    for (int i = 0; i < index; i++)
    {
      start = configuration.getStation("L", i);
      tracks = configuration.getTrack(i);
      end = configuration.getStation("R", i);
      
      Track[] track = new Track[tracks.length()];
      
      for (int j = 0; j < tracks.length(); j++)
      {
        switch (tracks.substring(j, j + 1))
        {
          case "L":
            Random random = new Random();
            boolean red = random.nextInt() % 2 == 0;
            if (red) track[j] = new Light("Light", j, "red");
            else track[j] = new Light("Light", j, "green");
            break;
          case "S":
            track[j] = new Switch("Switch", j);
            break;
          default:
            track[j] = new Track("Track", j);
            break;
        }
      }
      
      track[0].setName(start);
      track[tracks.length() - 1].setName(end);
      
      rail[i] = new Rail(track);
      map.add(tracks);
    }
  }
  
  /**
   * Helper function to set the connections between switches.
   */
  private void setSwitches()
  {
    for (int y = 0; y < map.size(); y++)
    {
      String tracks = map.get(y);
      for (int x = 0; x < tracks.length(); x++)
      {
        Track track = rail[y].getTracks()[x];
        if (track.getNeighbor("R") == null || y + 1 == rail.length || x >= rail[y + 1].getLength()) break;
        if (track instanceof Switch && !((Switch) track).isConnected())
        {
          Track connection = rail[y + 1].getTracks()[x];
          if (connection instanceof Switch)
          {
            ((Switch) track).setConnection(connection);
            ((Switch) connection).setConnection(track);
          }
        }
      }
    }
  }
  
  /**
   * Helper function for startTrain, determines which arriving station should also be disabled.
   *
   * @param arriving
   * @return rail & track index of arriving track
   */
  private int[] determineArriving(String arriving)
  {
    int railIndex, trackIndex, point[];
    
    railIndex = trackIndex = 0;
    point = new int[2];
    
    for (Rail r : rail)
    {
      for (Track t : r.getTracks())
      {
        if (t.getName().equals(arriving))
        {
          point[0] = railIndex;
          point[1] = trackIndex;
          return point;
        }
        trackIndex++;
      }
      railIndex++;
      trackIndex = 0;
    }
    
    point[0] = 0;
    point[1] = 0;
    return point;
  }
  
  /**
   * Adds a train to the rail at the given index.
   *
   * @param index
   */
  private void addTrain(int index)
  {
    trains.add(new Train("Train-" + index));
    
    if (index == 0) trains.get(index).setPriority(Thread.MAX_PRIORITY);
    else if (index > 10) trains.get(index).setPriority(Thread.MIN_PRIORITY);
    else trains.get(index).setPriority(Thread.MAX_PRIORITY - index);
  }
}
