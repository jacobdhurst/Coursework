/*
 * CS351L Project #2: SmartRail
 * Jacob Hurst & Jaehee Shin
 * 10/18/17
 *
 * Configuration.java - builds configurations from a given file or from given grid array - stores built configurations
 * in configurations ArrayList rather than constantly rebuilding them.
 */

package Controller;

import java.io.*;
import java.util.ArrayList;

class Configuration
{
  private static final ArrayList<Configuration> configurations = new ArrayList<>(4);
  private String[][] grid = new String[8][];
  private int height, index = -1;
  
  /**
   * Special constructor used for custom configurations.
   */
  public Configuration(String grid[][])
  {
    this.grid = grid;
    this.index = 0;
    this.height = grid.length;
    
    configurations.clear();
  }
  
  /**
   * Default constructor, builds configuration based off of given file.
   *
   * @param file
   */
  public Configuration(InputStream file, int index)
  {
    if(configurations.size() == 0)
    {
      configurations.add(new Configuration());
      configurations.add(new Configuration());
      configurations.add(new Configuration());
      configurations.add(new Configuration());
    }
    if(configurations.get(index).getIndex() != -1) loadConfiguration(index);
    else
    {
      this.index = index;
      InputStreamReader input = new InputStreamReader(file);
      BufferedReader reader = new BufferedReader(input);
  
      try
      {
        String line = reader.readLine();
        height = 0;
        while(line != null)
        {
          grid[height] = new String[3];
          String temp[] = line.split("\\:");
          
          setGrid(temp);
          
          line = reader.readLine();
          height++;
        }
      }
      catch(IOException e)
      {
        e.printStackTrace();
      }
      configurations.add(index, this);
      configurations.remove(configurations.size()-1);
    }
  }
  
  /**
   * Special constructor used to maintain loaded configurations.
   */
  private Configuration(){}
  
  /**
   * @return height, where height = number of rails.
   */
  int getHeight()
  {
    return height;
  }
  
  /**
   * @param side
   * @param index
   * @return station at given side and height.
   */
  String getStation(String side, int index)
  {
    return (side.equals("L")) ? grid[index][0] : grid[index][2];
  }
  
  /**
   * @param index
   * @return string representation of the tracks at given height.
   */
  String getTrack(int index)
  {
    return grid[index][1];
  }
  
  /**
   * Loads a previously built configuration from given index.
   *
   * @param index
   */
  private void loadConfiguration(int index)
  {
    grid = configurations.get(index).grid;
    height = configurations.get(index).height;
  }
  
  /**
   * @return index of this configuration.
   */
  private int getIndex()
  {
    return index;
  }
  
  
  /**
   * Helper function, set's a line from configuration to grid.
   * Checks for any changes that need to be made within.
   *
   * @param temp
   */
  private void setGrid(String[] temp)
  {
    if(temp.length == 3)
    {
      if(temp[0].isEmpty())
      {
        grid[height][0] = "NO STATION";
        grid[height][1] = temp[1];
        grid[height][2] = temp[2];
      }
      else
      {
        grid[height][0] = temp[0];
        grid[height][1] = temp[1];
        grid[height][2] = temp[2];
      }
    }
    else if(temp.length == 2)
    {
      if(temp[0].isEmpty())
      {
        grid[height][0] = "NO STATION";
        grid[height][1] = temp[1];
        grid[height][2] = "NO STATION";
      }
      else if(isRail(temp[0]))
      {
        grid[height][0] = "NO STATION";
        grid[height][1] = temp[0];
        grid[height][2] = temp[1];
      }
      else if(isRail(temp[1]))
      {
        grid[height][0] = temp[0];
        grid[height][1] = temp[1];
        grid[height][2] = "NO STATION";
      }
      else System.out.println("Invalid file format detected.");
    }
    else System.out.println("Invalid file format detected.");
  }
  
  /**
   * @param string
   * @return whether the current string appears to be a rail.
   */
  private boolean isRail(String string)
  {
    boolean isRail = true;
    for(int i = 1; i < string.length(); i++)
    {
      String value = string.substring(i-1, i);
      if(!(value.equals("T") || value.equals("L") || value.equals("S"))) isRail = false;
    }
    return isRail;
  }
}
