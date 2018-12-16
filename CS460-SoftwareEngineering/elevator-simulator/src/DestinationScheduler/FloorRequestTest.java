package DestinationScheduler;

import ElevatorSystem.Direction;

public class FloorRequestTest {

    int floorId;
    Direction dir;
    int assigned;
    boolean emerg;
    public FloorRequestTest(int floorId, Direction e, int assigned, boolean emerg){
        this.floorId = floorId;
        this.dir = e;
        this.assigned = assigned;
        this.emerg = emerg;
    }

    public String toString(){
        return "Floor : " + floorId + " Direction : " + dir;
    }


}
