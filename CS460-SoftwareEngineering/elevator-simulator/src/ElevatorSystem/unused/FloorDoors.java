package ElevatorSystem.unused;

import ElevatorSystem.unused.DoorLatch;
import ElevatorSystem.unused.Doors;

import static ElevatorSystem.SystemValues.DoorClosed;

/**
 * holds the door position and contact switches which are updated when door motor calls the class
 */

public class FloorDoors extends Doors
{
  private int position = DoorClosed;
  private DoorLatch open;
  private DoorLatch closed;

  public FloorDoors(DoorLatch open, DoorLatch closed)
  {
    this.open = open;
    this.closed = closed;
    open.update(false);
    closed.update(true);
  }

  protected void update(int speed)
  {
    this.position += speed;
    if (position >= DoorClosed)
    {
      this.position = DoorClosed;
      open.update(false);
      closed.update(true);
    } else if (position <= 0)
    {
      this.position = 0;
      open.update(true);
      closed.update(false);
    } else
    {
      open.update(false);
      closed.update(false);
    }
  }
}
