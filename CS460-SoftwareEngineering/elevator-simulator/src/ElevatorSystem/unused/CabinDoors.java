package ElevatorSystem.unused;

import ElevatorSystem.unused.CabinMotor;
import ElevatorSystem.unused.DoorLatch;
import ElevatorSystem.unused.Doors;

import java.util.Random;

import static ElevatorSystem.SystemValues.DoorClosed;
import static ElevatorSystem.SystemValues.alignmentVariance;

public class CabinDoors extends Doors
{
  private int position = DoorClosed;
  private DoorLatch open;
  private DoorLatch closed;
  private CabinMotor motor;
  private final Random rnd = new Random();

  public CabinDoors(DoorLatch open, DoorLatch closed, CabinMotor motor)
  {
    this.open = open;
    this.closed = closed;
    open.update(false);
    closed.update(true);
    this.motor = motor;
  }

  protected void update(int speed)
  {
    this.position += speed;
    if(position >= DoorClosed)
    {
      this.position = DoorClosed;
      open.update(false);
      closed.update(true);
    }
    else if(position <= 0)
    {
      this.position = 0;
      open.update(true);
      closed.update(false);
      motor.step(rnd.nextInt(alignmentVariance));
    }
    else
    {
      open.update(false);
      closed.update(false);
    }
  }
}
