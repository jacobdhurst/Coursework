/*
 * CS351L Project #2: SmartRail
 * Jacob Hurst & Jaehee Shin
 * 10/18/17
 *
 * Switch.java - Extension of track, provides switching functionality.
 */

package Model;

import View.SwitchView;

public class Switch extends Track
{
  private Track connection;
  private boolean connected;
  
  private SwitchView switchView;
  
  /**
   * Default constructor takes name & index.
   *
   * @param name
   * @param index
   */
  public Switch(String name, int index)
  {
    super(name, index);
    connection = this;
    connected = false;
  }
  
  /**
   * Sets the track connected either above or below this track.
   *
   * @param connection
   */
  public void setConnection(Track connection)
  {
    this.connection = connection;
    connected = true;
  }
  
  /**
   * @return track connected either above or below this track.
   */
  public Track getConnection()
  {
    return this.connection;
  }
  
  /**
   * @return whether the track has been connected.
   */
  public boolean isConnected()
  {
    return connected;
  }
  
  /**
   * Sets view component for this thread.
   *
   * @param switchView
   */
  public void setSwitchView(SwitchView switchView) {
    this.switchView = switchView;
  }
  
  /**
   * @return view component for this thread.
   */
  public SwitchView getSwitchView() {
    return switchView;
  }
  
  /**
   * Sets value for neighboring light of switch if it exists.
   *
   * @param direction
   * @param value
   */
  void setNeighboringLights(String direction, String value)
  {
    Track temp = this.getNeighbor(direction);
    if(temp instanceof Light) ((Light)temp).setLight(value);
  }
}
