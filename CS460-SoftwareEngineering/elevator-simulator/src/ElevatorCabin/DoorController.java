package ElevatorCabin;
import ElevatorSystem.Direction;
import Simulator.Elevators;
import Simulator.Floors;

import java.util.Timer;
import java.util.TimerTask;

public class DoorController {

    private boolean locked;
    private boolean timeOut = false;
    private int cabinId;
    private Elevators elevator;


    public DoorController(boolean locked, int cabinId){
        this.locked = locked;
        this.cabinId = cabinId;
        this.elevator = Elevators.valueOf("E" + cabinId);
    }

    public  boolean isLocked() {
        return locked;
    }

    public void setLock(boolean locked){
        this.locked = locked;
    }

    public void waitTime(long delay) {
        TimerTask task = new TimerTask() {
            public void run() {
                timeOut = true;
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, delay);
    }

    public boolean timedOut() {
        return timeOut;
    }


    public void openDoors() {
        elevator.leftDoorOpen();
        elevator.rightDoorOpen();
    }

    public void closeDoors() {
        elevator.leftDoorClosed();
        elevator.rightDoorClosed();
    }

    public DoorStatus getStatus() {
        return elevator.getDoorStatus();
    }

    public void openFloorDoor(int floor, boolean up){
        if (floor > 0 && floor <= 10){
            Floors f = Floors.valueOf("F" + floor);
            f.setDoorSpeed(cabinId - 1, true, 0.5);
            f.setDoorSpeed(cabinId - 1, false, 0.5);

            // Set arrival lights
            f.setArrivalLights(cabinId - 1, up);
        }

    }

    public void closeFloorDoor(int floor, boolean up){

        if (floor > 0 && floor <= 10){
            Floors f = Floors.valueOf("F" + floor);
            f.setDoorSpeed(cabinId - 1, true, -0.5);
            f.setDoorSpeed(cabinId - 1, false, -0.5);

            // Set arrival lights
            f.clearArrivalLights(cabinId -1, up);
        }

    }

}
