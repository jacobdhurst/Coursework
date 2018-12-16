/*
 * CS351L Project #2: SmartRail
 * Jacob Hurst & Jaehee Shin
 * 10/18/17
 *
 * Light.java - Extension of track, provides lighting functionality.
 */

package Model;

import View.LightView;

public class Light extends Track
{
  private String light;
  
  private LightView lightView;
  
  /**
   * Default constructor - takes a name, index, and light status (red or green).
   *
   * @param name
   * @param index
   * @param light
   */
  public Light(String name, int index, String light)
  {
    super(name, index);
    this.light = light;
  }
  
  /**
   * Sets view component for this thread.
   *
   * @param lightView
   */
  public void setLightView(LightView lightView)
  {
    this.lightView = lightView;
    
    if(light.equals("green")) this.lightView.changeLight("green");
    else if(light.equals("red")) this.lightView.changeLight("red");
  }
  
  /**
   * @return light's lightView.
   */
  public LightView getLightView()
  {
    return this.lightView;
  }

  /**
   * Sets the light to either red or green.
   *
   * @param light
   */
  void setLight(String light)
  {
    this.light = light;

    if(light.equals("green")) lightView.changeLight("green");
    else lightView.changeLight("red");
  }
  
  /**
   * @return state of the light.
   */
  String getLight()
  {
    return this.light;
  }
}