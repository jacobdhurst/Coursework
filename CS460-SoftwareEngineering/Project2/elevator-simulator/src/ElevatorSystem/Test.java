package ElevatorSystem;

import ElevatorSystem.unused.BuildingPackage;

import java.util.Arrays;

public class Test
{
  public static void main(String args[])
  {
    BuildingPackage building = new BuildingPackage();
    System.out.println(Arrays.toString(building.elevators[0].bottomFloorAlignment.getStatus()));
  }
}
