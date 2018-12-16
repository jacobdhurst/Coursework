package ElevatorSystem.Floor;

import ElevatorSystem.Direction;
import ElevatorSystem.Floor.FloorButtons;
import ElevatorSystem.Shared.ElevatorEnum;
import ElevatorSystem.SystemValues;
import Simulator.Floors;

public class FloorManager {

    private FloorButtons floorButtons;
    private FloorFireKey firekey;
    private ArrivalLights arrivalLights;

    public FloorManager(){
        this.floorButtons = new FloorButtons();
        this.firekey = new FloorFireKey();
        this.arrivalLights = new ArrivalLights();
    }

    public void elevatorArrivalLights(ElevatorEnum elev, int floor, boolean up){

        floorButtons.removeFloorRequest(floor,up); //doors should have this, but just in case its here
        arrivalLights.turnOnCallLight(floor,elev,up);
    }

    public void elevatorArrivalLightsOff(ElevatorEnum elev, int floor, boolean up){
        arrivalLights.turnOffCallLight(floor,elev,up);
    }


    /*Will return a single floor request, null if no new request*/

    public FloorRequest getFloorRequest(){
        int[] buttonrequest = floorButtons.getFloorButtonsRequest();
        if(buttonrequest == null) return null;
        boolean tempdir;
        if(buttonrequest[1] == 0) tempdir = true;
        else tempdir = false;
        return new FloorRequest(buttonrequest[0],tempdir,firekey.getFloorKeyStatus(buttonrequest[0]-1));
    }

    public void rejectFloorRequest(int floor, boolean up){
        floorButtons.removeFloorRequest(floor,up);
    }

    public FloorButtons getFloorButtons(){return  this.floorButtons;}
}
