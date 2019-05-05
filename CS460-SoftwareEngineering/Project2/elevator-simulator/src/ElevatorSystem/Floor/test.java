package ElevatorSystem.Floor;

import java.util.Random;

public class test
{
  public static void main(String[] args) {

    FloorManager manager = new FloorManager();
    FloorRequest request;

    for(int i = 0; i < 25; i++)
    {
      request  = manager.getFloorRequest();
      if(request == null) System.out.println("null");
      else System.out.println("floorID: "+request.getFloor()+", Button: "+request.getDirection()+", Firekey: "+request.isFireKeyTurned());
    }
  }
}
