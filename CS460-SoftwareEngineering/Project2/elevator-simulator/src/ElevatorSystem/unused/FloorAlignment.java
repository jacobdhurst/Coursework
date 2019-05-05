package ElevatorSystem.unused;

/**
 * has reference to elevator shaft to check if its active and the alignment
 * gets passed its current position by CabinID Cabin
 */
public class FloorAlignment
{
  private int position;
  private final ElevatorShaft shaft;
  private final int location;

  public FloorAlignment(int position, ElevatorShaft shaft, int location)
  {
    this.position = position;
    this.shaft = shaft;
    this.location = location;
  }

  void update(int position)
  {
    this.position = position;
  }

  public int[] getStatus()
  {
    return shaft.checkPosition(position,location);
  }
}
