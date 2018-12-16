package ElevatorSystem.unused;

/**
 * updates CabinDoors or FloorDoors based on current speed
 * Safety Latch?
 */
public class DoorMotor
{
  private final Doors doors;
  public DoorMotor(Doors doors)
  {
    this.doors = doors;
  }
  void step(int Speed)
  {
    doors.update(Speed);
  }
}
