package ElevatorSystem.unused;
import ElevatorSystem.*;
import ElevatorSystem.ElevatorComponents.*;
import ElevatorSystem.Floor.ArrivalLights;
import ElevatorSystem.Floor.FloorDisplay;
//import ElevatorSystem.Cabin.CabinFloorDisplay;
//import ElevatorSystem.Cabin.CabinFloorFireKey;

import static ElevatorSystem.SystemValues.*;

public class ElevatorPackage
{
  /**
   * class that builds all elevator components for one elevator
   */
  private ElevatorShaft shaft = new ElevatorShaft();
  private CabinDoors cabindoors;
  public Alarm alarm = new Alarm();
  public ArrivalLights arrivalLights = new ArrivalLights();
  //public CabinFloorFireKey firekey = new CabinFloorFireKey();
  //public CabinFloorButtons cabinButtons = new CabinFloorButtons();
  //public CabinFloorDisplay cabinDisplay = new CabinFloorDisplay();
  public CabinMotor cabinMotor;
  public DoorLatch cabinDoorOpen = new DoorLatch();
  public DoorLatch cabinDoorClosed = new DoorLatch();
  public DoorMotor doorMotor;
  public DoorMotor[] floorDoorMotors;
  public DoorLatch[] floorOpenLatches;
  public DoorLatch[] floorClosedLatches;
  public FloorAlignment topFloorAlignment;
  public FloorAlignment bottomFloorAlignment;
  public FloorDisplay floorDisplay = new FloorDisplay();
  public WeightSensor weightSensor = new WeightSensor();
  public ObstructionSensor obstructionSensor = new ObstructionSensor();

  public ElevatorPackage()
  {
    floorDoorMotors = new DoorMotor[numOfFloors];
    floorOpenLatches = new DoorLatch[numOfFloors];
    floorClosedLatches = new DoorLatch[numOfFloors];
    for(int i = 0; i < numOfFloors; i++)
    {
      DoorLatch open = new DoorLatch();
      DoorLatch closed = new DoorLatch();
      FloorDoors doors = new FloorDoors(open,closed);
      floorDoorMotors[i] = new DoorMotor(doors);
      floorOpenLatches[i] = open;
      floorClosedLatches[i] = closed;
    }
    topFloorAlignment = new FloorAlignment(CabinStartingPos+CabinHeight,shaft,SystemValues.top);
    bottomFloorAlignment = new FloorAlignment(CabinStartingPos,shaft,SystemValues.bottom);
    cabinMotor = new CabinMotor(topFloorAlignment, bottomFloorAlignment);
    cabindoors = new CabinDoors(cabinDoorOpen,cabinDoorClosed, cabinMotor);
    doorMotor = new DoorMotor(cabindoors);
  }
}
