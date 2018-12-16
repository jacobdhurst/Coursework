package ElevatorSystem.unused;

import static ElevatorSystem.SystemValues.numOfElevators;

public class BuildingPackage
{
  public ElevatorPackage[] elevators;
  public FloorPackage floors = new FloorPackage();

  public BuildingPackage()
  {
    elevators = new ElevatorPackage[numOfElevators];
    for(int i = 0; i < numOfElevators; i++)
    {
      elevators[i] = new ElevatorPackage();
    }
  }
}
