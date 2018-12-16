package ElevatorSystem.Floor;

import ElevatorSystem.Shared.ElevatorEnum;
import ElevatorSystem.SystemValues;
import Simulator.Floors;

public class ArrivalLights
{

  public void displayTest(int floor, ElevatorEnum elev)
  {
    int elevID = elev.getElevatorNum();
    for(int i = 0; i < SystemValues.numOfFloors; i++){
      Floors.valueOf("F"+(i+1)).setFloorPanel(elevID,Integer.toString(floor));
    }
  }


  public void turnOnCallLight(int floor, ElevatorEnum elev, boolean up)
  {
    int elevID = elev.getElevatorNum();
    Floors.valueOf("F"+floor).setArrivalLights(elevID,up);
    for(int i = 0; i < SystemValues.numOfFloors; i++){
      Floors.valueOf("F"+(i+1)).setFloorPanel(elevID,Integer.toString(floor));
    }
  }

  public void turnOffCallLight(int floor, ElevatorEnum elev, boolean up)
  {
    int elevID = elev.getElevatorNum();
    Floors.valueOf("F"+floor).clearArrivalLights(elevID,up);
  }

}
