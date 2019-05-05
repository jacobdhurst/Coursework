package ElevatorSystem.Floor;

import ElevatorSystem.SystemValues;
import Simulator.Floors;

import java.util.Random;

import static ElevatorSystem.SystemValues.*;

public class FloorButtons
{
    private boolean buttons[][];
    private Random rnd = new Random();

    /*[][0] = upRequest, [][1] = downRequest*/
    public FloorButtons(){
        this.buttons = new boolean[10][2];
    }


    /*gets the next floor request that wasn't sent yet, returns null if no new requests*/
//    public Floors getFloorButtonsRequest(){
//        for(int i = 0; i < SystemValues.numOfFloors; i++){
//            boolean status[] = Floors.valueOf("F" + (i+1)).requestStatus();
//            buttons[i][0] = status[0];
//            buttons[i][1] = status[1];
//            if(!status[2]){
//                Floors.valueOf("F"+(i+1)).requestSent();
//                return Floors.valueOf("F"+(i+1));
//            }
//        }
//        return null;
//    }

    public int[] getFloorButtonsRequest()
    {
        for(int floor = 0; floor < numOfFloors; floor++)
        {
            for(int button = 0; button < 2; button++)
            {
                if(buttons[floor][button] == true) continue;
                else
                {
                    if(floor == 9 && button == 0) continue;
                    else if(floor == 0 && button == 1) continue;
                    else if(rnd.nextInt(floorRequestChance) + 1 == 1)
                    {
                        buttons[floor][button] = true;
                        return new int[]{floor+1, button};
                    }
                }
            }
        }
        return null;
    }

    /*Adds request to buttons[][] and updates Floor enum*/
    public void turnOnCallButtonLight(int floor, boolean up){
        //WANT TO TEST MODE HERE, don't want to light up if not Normal Mode or fire key used
        if (up) {
            buttons[floor - 1][0] = true;
            Floors.valueOf("F" + floor).requestUp();
        } else {
            buttons[floor - 1][1] = true;
            Floors.valueOf("F" + floor).requestDown();
        }

    }

    /*sets request to false for buttons[][] and updates Floor enum*/
    public void turnOffCallButtonLight(int floor, boolean up){
        if(up){
            buttons[floor-1][0] = false;
        }
        else{
            buttons[floor-1][1] = false;
        }
        Floors.valueOf("F" + floor).clear(up);
    }

    /*sets request to false for buttons[][] and updates Floor enum*/
    public void removeFloorRequest(int floor, boolean up){
        if(up){
            buttons[floor-1][0] = false;
        }
        else{
            buttons[floor-1][1] = false;
        }
        Floors.valueOf("F" + floor).clear(up);
    }
}
