package DestinationScheduler;

import ElevatorCabin.CabinID;
import ElevatorCabin.ElevatorStatus;
import ElevatorSystem.Cabin.CabinRequest;
import ElevatorSystem.Direction;
import ElevatorSystem.Floor.FloorRequest;
import ElevatorSystem.Shared.FloorEnum;

import java.util.*;

public class DestinationScheduler {

    LinkedList<CabinRequest> currentCabinReq;
    LinkedList<FloorRequest> currentFloorReq;
    LinkedList<FloorRequest> main_options = new LinkedList<>();
    LinkedList<FloorRequest> queued_options = new LinkedList<>();

    ElevatorStatus currentStat;

    boolean isStart = true;

    Direction prev_direction;

    public FloorEnum getNextDestination(LinkedList<FloorRequest> floorReq, LinkedList<CabinRequest> cabReq, ElevatorStatus status){
        this.currentCabinReq = cabReq;
        this.currentFloorReq = floorReq;
        this.currentStat = status;
        prev_direction = status.getDirection();
//        System.out.println("Current Floor " + currentStat.getFloor());
        //this.currentStat.setDirection(Direction.STOPPED);


       // return FloorEnum.F5;


        return floorSchedule();

//        else{
//            System.out.println("Emergency Mode is active...");
//            return null;
//
//        }

    }

    void addFloorRequest(FloorRequestTest request){
        //currentFloorReq.addFirst(request);
    }

    void addCabinRequest(CabinRequestTest request){
        //currentCabinReq.addFirst(request);
    }

    Direction convertFromBool(boolean b){
        if(b) return Direction.UP;
        else return Direction.DOWN;
    }



    Direction directionSet(int current, int dest){
        if((current - dest) > 0){
            return Direction.UP;
        }
        else{
            return Direction.DOWN;
        }
    }




    FloorEnum floorSchedule() {
        //https://en.wikipedia.org/wiki/Elevator#The_elevator_algorithm



        int currentCabinId = currentStat.getCabinId();



        if (!currentFloorReq.isEmpty() && !currentCabinReq.isEmpty()) {
            Direction myCurrentDir = currentStat.getDirection();

            for (FloorRequest floorReq : currentFloorReq) {

                for (CabinRequest cabinReq : currentCabinReq) {

                    int floorCall = floorReq.getFloor().getFloorNum();
                    int cabinDest = cabinReq.getDestNum();
                    int currentLoc = currentStat.getFloor().getFloorNum();
                    boolean stuff = currentStat.getCabinId() == cabinReq.getElevatorID().getElevatorNum();

//                    System.out.println("Floor Request " + floorReq.getFloor().getFloorNum());
                    //Moving up
                    if((floorCall > currentLoc) || (cabinReq.getDestNum()) > currentLoc) {

//                        System.out.println("Moving Up");
                        System.out.println(cabinReq.getDestNum());
                        System.out.println(currentLoc);
                        if (cabinReq.getDestNum() > floorCall && currentLoc != floorCall) {

                            currentFloorReq.remove(floorReq);
//                            System.out.println("Floor is in range " + floorReq.getFloor().getFloorNum());
                            return floorReq.getFloor();
                        }
                        else {
                            currentCabinReq.remove(cabinReq);
//                            System.out.println("Going to cabin request " + cabinDest +" , " + floorCall);
                            return cabinReq.getDestID();
                        }

                    }

                    //Moving down
                    else if((floorCall < currentLoc) || (cabinReq.getDestNum()) < currentLoc ){
//                        System.out.println("Moving Down");
//                        System.out.println(cabinReq.getDestNum());
//                        System.out.println(currentLoc);

                        if(cabinReq.getDestNum() < floorCall && currentLoc != floorCall){
//                            System.out.println("Floor is in range " + floorCall);
                            //currentFloorReq.remove(floorReq);
                            return floorReq.getFloor();
                        }
                        else {
//                            System.out.println("Going to cabin request " + cabinDest +" , " + floorCall);
                            //currentCabinReq.remove(cabinReq);
                            return cabinReq.getDestID();
                        }
                    }

                    else if(currentFloorReq.isEmpty()) {
//                        System.out.println("ASDASDASD");
//                        System.out.println("Cabin info " + cabinReq.toString());
//                        System.out.println("Floor info " + floorReq.toString());
                            //queued_options.add(floorReq);
                    }
                   // }

                }

            }
//            if(!main_options.isEmpty()) {
//                System.out.println("Closest -> " + closestFloor(main_options).toString());
//                System.out.println("Elevator " + currentStat.getCabinId());
//                return closestFloor(main_options);
//
//            }
        }


        //LinkedList<CabinRequest> cabinRequests = new LinkedList<>();

        //No more requests from floors only internal panel requests remain.
//        if (!currentCabinReq.isEmpty() && currentFloorReq.isEmpty()) {
//            int closest;
//            int abs_diff;
//            int diff;
//
////            System.out.println("ASDASDASDASDASDASDASSADDSDSASSAD");
//            int currentFloor = currentStat.getFloor().getFloorNum();
////            System.out.println("CurrentFloor " + currentFloor);
//            closest = currentCabinReq.getFirst().getDestNum();
//            for (CabinRequest cabReq : currentCabinReq) {
//                int destination = cabReq.getDestNum();
//                int id = cabReq.getDestIdx();
//                if (currentFloor != destination && id == currentStat.getCabinId()) {
//                    abs_diff = Math.abs(currentFloor - destination);
//                    diff = (currentFloor - destination);
//
//                    //System.out.println("Diff " + abs_diff + " closest cabin request " + cabReq.requestedDest);
//                    if (abs_diff < closest) {
//                        //closest = cabReq.requestedDest;
//                        if (diff > 0 && currentStat.getDirection().equals(Direction.DOWN)) {
//                            closest = destination;
//                        } else if (diff < 0 && currentStat.getDirection().equals(Direction.UP)) {
//                            closest = destination;
//                        } else {
////                            System.out.println(cabReq.toString());
//                        }
//                    }
//                }
//
//            }
//            //System.out.println("Next closest floor " + closest + " current direction " + currentStat.getDirection());

//        }



        else if(!main_options.isEmpty()) {
            System.out.println("Closest -> " + closestFloor(main_options).toString());
            System.out.println("Elevator " + currentStat.getCabinId());
            return closestFloor(main_options);

        }

        System.out.println(currentFloorReq);

        return null;
    }




    FloorEnum closestFloor(LinkedList<FloorRequest> floors){

        int closest = floors.getFirst().getFloor().getFloorNum();
        int currentFloor = currentStat.getFloor().getFloorNum();
        for(FloorRequest f : floors){
            int floor_f = f.getFloor().getFloorNum();
            if (currentFloor != floor_f) {
                if (closest < floor_f) {
                    closest = floor_f;
                }
            }
            else return f.getFloor();
        }

        return FloorEnum.valueOf("F"+closest);
    }

    CabinID nextCabinStop(LinkedList<CabinRequestTest> cabin){

        int closest = cabin.getFirst().requestedDest;
        int currentCabin = currentStat.getCabinId();
        for(CabinRequestTest c : cabin){
            if (currentCabin != c.requestedDest) {
                if (closest < c.requestedDest) {
                    closest = c.requestedDest;
                }
            }
        }

        return CabinID.valueOf("E"+closest);

    }


//    int shortestDistance(){
//
//
//        int diff;
//        int currentFloor = currentStat.getFloor().getFloorNum();
//        System.out.println("CurrentFloor " + currentFloor);
//        int closest = currentFloorReq.getFirst().getFloor().getFloorNum();
//        for(FloorRequest floorReq: currentFloorReq) {
//            if (currentFloor != floorReq.floorId) {
//                diff = Math.abs(currentFloor - floorReq.floorId);
//
//                System.out.println("Diff " + diff + " floorReq " + floorReq.floorId);
//                if (diff < closest) {
//                    closest = diff;
//                }
//            }
//
//        }
//
//        System.out.println("close " + closest);
//        return closest;
//    }


//    public static void main(String args[]){
//
//        LinkedList<CabinRequestTest> test = new LinkedList<>();
//        LinkedList<FloorRequestTest> test2 = new LinkedList<>();
//        CabinRequestTest req;
//        FloorRequestTest req2;
//        Random rand = new Random();
//        int dest;
//        for(int i = 0; i < 4; i++) {
//            for (int j = 0; j < 5; j++) {
//                dest = rand.nextInt(9) + 1;
//                req = new CabinRequestTest(i, dest, false);
//                System.out.println("My cabin requests = " + req.requestedDest + " ID : " + req.elevatorId);
//                test.add(req);
//            }
//        }
//
//
//        for(int i = 1; i <= 10; i++){
//            dest = rand.nextInt(2);
//            Direction d = Direction.values()[dest];
//            //req2 = new FloorRequestTest(i, d, i , false);
//            //System.out.println("My floor requests = " + req2.dir);
//            //test2.add(req2);
//        }
//
//        //ElevatorStatus status = new ElevatorStatus(Direction.UP, FloorEnum.F1, FloorEnum.F2, 0 );
//        //DestinationScheduler scheduler = new DestinationScheduler();
//        //scheduler.getNextDestination(test2, test, status, false);
//    }

}


