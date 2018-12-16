/*
 * CS351L Project #2: SmartRail
 * Jacob Hurst & Jaehee Shin
 * 10/18/17
 *
 * Rail.java - Collection of tracks that make up a rail.
 */

package Model;

public class Rail
{
  private final Track[] rail;
  
  /**
   * Default constructor - takes array of tracks in rail.
   *
   * @param rail
   */
  public Rail(Track[] rail)
  {
    this.rail = rail;
    buildRail();
  }
  
  /**
   * @return length of the rail.
   */
  public int getLength()
  {
    return this.rail.length;
  }
  
  /**
   * @return array of tracks on rail.
   */
  public Track[] getTracks()
  {
    return this.rail;
  }
  
  
  /**
   * builds the rail, sets connections between neighboring tracks.
   */
  private void buildRail()
  {
    for(int i = 0; i < rail.length; i++)
    {
      if(i == 0)
      {
        rail[i].setNeighborL(null);
        rail[i].setNeighborR(rail[i+1]);
      }
      else if(i == (rail.length - 1))
      {
        rail[i].setNeighborL(rail[i-1]);
        rail[i].setNeighborR(null);
      }
      else
      {
        rail[i].setNeighborL(rail[i-1]);
        rail[i].setNeighborR(rail[i+1]);
      }
      rail[i].start();
    }
  }
}
