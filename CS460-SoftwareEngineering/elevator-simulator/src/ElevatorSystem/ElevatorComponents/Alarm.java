package ElevatorSystem.ElevatorComponents;

public class Alarm
{
  private boolean status = false;

  public void setStatus(boolean status){
    this.status = status;
  }

  public boolean getStatus()
  {
    return this.status;
  }
}
