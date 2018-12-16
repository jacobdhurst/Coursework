package ElevatorSystem.Floor;


import ElevatorSystem.Shared.ElevatorEnum;
import ElevatorSystem.SystemValues;
import Simulator.Floors;

public class FloorDisplay
{
    private int displayStatus;

    public void setArrivalDisplay(int floor, ElevatorEnum elev){
        int elevID = elev.getIdx();
        this.displayStatus = floor;
        for(int i = 0; i < SystemValues.numOfFloors; i++){
            Floors.valueOf("F"+(i+1)).setFloorPanel(elevID,Integer.toString(floor));
        }
    }
}



