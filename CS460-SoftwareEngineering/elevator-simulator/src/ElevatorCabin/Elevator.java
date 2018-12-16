package ElevatorCabin;

import ElevatorSystem.Cabin.CabinManager;
import ElevatorSystem.Cabin.CabinRequest;
import ElevatorSystem.Direction;
import ElevatorSystem.Floor.FloorManager;
import ElevatorSystem.Shared.ElevatorEnum;
import ElevatorSystem.Shared.FloorEnum;
import Simulator.Floors;
import Simulator.Panels;

import java.util.LinkedList;

public class Elevator {

    private ElevatorStatus elevatorStatus;
    private DoorController doorController;
    private CabinMotionControl cabinMotionControl;
    private LinkedList<CabinRequest> requests = new LinkedList<>();
    private FloorEnum destination;
    private CabinID cabinID;
    private CabinManager cabinManager;
    private FloorManager floorManager;
    private ElevatorEnum eID;
    private FloorEnum fID;
    private boolean closing, moving, open, opening, hasRequest, waiting, up = false;
    private int waitingTime = 5;
    private int thisTime = 0;
    private boolean movingUp;


    public Elevator(CabinID cabinID, CabinManager cabinManager, ElevatorEnum eID, FloorManager floorManager) {
        this.cabinID = cabinID;
        this.eID = eID;
        this.cabinManager = cabinManager;
        this.floorManager = floorManager;
        elevatorStatus = new ElevatorStatus(Direction.STOPPED, FloorEnum.F1, null, cabinID.getCabinID());
        doorController = new DoorController(true, cabinID.getCabinID());
        cabinMotionControl = new CabinMotionControl(cabinID);
        open = true;
    }


    public void addRequest(CabinRequest request) {
        boolean duplicate = false;
        for(CabinRequest r : requests){
            if (r.getDestIdx() == request.getDestIdx()){
                duplicate = true;
                break;
            }
        }
        if(!duplicate){
            requests.add(request);
            this.cabinID.getPanel().newRequest(request.getDestIdx() + 1);
        }
    }

    public LinkedList<CabinRequest> getRequests() {
        return requests;
    }

    public Direction getCurrentCMCDirection() {
        return cabinMotionControl.getCurrentDirection();
    }

    public ElevatorStatus getStatus() {
        return elevatorStatus;
    }



    //SAFETY MONITOR---SAFETY MONITOR---SAFETY MONITOR---SAFETY MONITOR---SAFETY MONITOR---SAFETY MONITOR---SAFETY MONITOR

    public void updateCabin() {
        DoorStatus doorStatus = doorController.getStatus();

        if (doorStatus.equals(DoorStatus.OPEN)
                || doorStatus.equals(DoorStatus.CLOSING)
                || doorStatus.equals(DoorStatus.OPENING)){
            open = true;
            if(doorStatus.equals(DoorStatus.CLOSING)) closing = true;
            else if(doorStatus.equals(DoorStatus.OPENING)) opening = true;
        } else {
            open = false;
        }



        if (open) {
            if(!doorStatus.equals(DoorStatus.CLOSING)){
                doorController.closeFloorDoor(elevatorStatus.getFloor().getFloorNum(), movingUp);
                doorController.closeDoors();
            }
        } else {
            //Move if doors closed and has request.
            //Else if arrived at dest, open doors, ready up for next request.
            if(hasRequest && !moving){
                cabinMotionControl.moveToFloor(destination);
                up = cabinMotionControl.getCurrentDirection().equals(Direction.UP);
                movingUp = up;
                moving = true;
            } else if(cabinMotionControl.getCurrentDirection().equals(Direction.STOPPED) && hasRequest){
                moving = false;
                hasRequest = false;
                elevatorStatus.setCurrentFloor(elevatorStatus.getDest()); // elevator arrived so set current to dst
                elevatorStatus.setDestFloor(null);
                doorController.openFloorDoor(elevatorStatus.getFloor().getFloorNum(), movingUp);
                doorController.openDoors();
                Panels.valueOf("P"+(eID.getIdx()+1)).requestHandled(destination.getIdx());
                open = true;
                waiting = true;
            }
        }
        cabinID.getPanel().setCurrentFloor(cabinMotionControl.getCurrentFloor());
    }

    //SAFETY MONITOR---SAFETY MONITOR---SAFETY MONITOR---SAFETY MONITOR---SAFETY MONITOR---SAFETY MONITOR---SAFETY MONITOR



    public void makeStopAt(FloorEnum floor) {
        if (floor != null) { //Will be null if in failure.
            destination = floor;
            elevatorStatus.setDestFloor(floor);
            hasRequest = true;
            open = true;
            moving = false;
        }
    }
}
