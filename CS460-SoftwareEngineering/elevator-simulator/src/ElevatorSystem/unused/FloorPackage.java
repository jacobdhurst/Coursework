package ElevatorSystem.unused;

import ElevatorSystem.Floor.ArrivalLights;
import ElevatorSystem.Floor.FloorFireKey;
import ElevatorSystem.Floor.FloorDisplay;

import static ElevatorSystem.SystemValues.numOfFloors;

/**
 * class for building floor components
 */
public class FloorPackage
{
  public FloorDisplay[] floorDisplays;
  public FloorFireKey[] floorFireKeys;
  public ArrivalLights[] arrivalLights;

  public FloorPackage()
  {
    floorDisplays = new FloorDisplay[numOfFloors];
    floorFireKeys = new FloorFireKey[numOfFloors];
    arrivalLights = new ArrivalLights[numOfFloors];
    for(int i = 0; i < numOfFloors; i++)
    {
      floorDisplays[i] = new FloorDisplay();
      floorFireKeys[i] = new FloorFireKey();
      arrivalLights[i] = new ArrivalLights();
    }
  }
}
