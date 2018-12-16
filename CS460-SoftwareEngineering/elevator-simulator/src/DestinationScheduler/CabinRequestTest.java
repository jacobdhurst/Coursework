package DestinationScheduler;

import java.util.LinkedList;

public class CabinRequestTest {

    LinkedList<Integer> currentCabinRequest = new LinkedList<>();
    int elevatorId;
    int requestedDest;
    boolean emerg;

    public CabinRequestTest(int elevatorId, int dest, boolean emerg){
        this.elevatorId = elevatorId;
        this.requestedDest = dest;
        this.emerg = emerg;
    }
    public int getElevatorId(){
        return elevatorId;
    }
    public int getDest(){
        return requestedDest;
    }
    public boolean getEmerg(){
        return emerg;
    }

    public String toString(){

        return "Cabin : " + elevatorId + " Destination : " + requestedDest;

    }

}
