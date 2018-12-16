package Simulator;

import ElevatorCabin.DoorStatus;
import Simulator.Shaft.ShaftDisplay;

public enum Elevators {

    E1(true, true, 0, -1, -1),
    E2(true, true, 0, -1, -1),
    E3(true, true, 0, -1, -1),
    E4(true, true, 0, -1, -1);

    private boolean leftDoorClosed, rightDoorClosed;
    private int moving, topSensor, bottomSensor;

    private ShaftDisplay shaft;

    Elevators (boolean leftDoorClosed, boolean rightDoorClosed, int moving, int topSensor, int bottomSensor){
        this.leftDoorClosed = leftDoorClosed;
        this.rightDoorClosed = rightDoorClosed;
        this.moving = moving;
        this.topSensor = topSensor;
        this.bottomSensor = bottomSensor;
    }

    public void setShaft(ShaftDisplay shaft)
    {
        this.shaft = shaft;
    }

    public void sensors()
    {
        int[] active = shaft.getActiveSensors();
        this.topSensor = active[0];
        this.bottomSensor = active[1];
    }

    public DoorStatus getDoorStatus() {
        return shaft.getElevatorDoorStatus();
    }

    public void leftDoorClosed(){
        this.leftDoorClosed = true;
        shaft.closeElevatorLeftDoor();
    }

    public void leftDoorOpen(){
        this.leftDoorClosed = false;
        shaft.openElevatorLeftDoor();
    }

    public void rightDoorClosed(){
        this.rightDoorClosed = true;
        shaft.closeElevatorRightDoor();
    }

    public void rightDoorOpen(){
        this.rightDoorClosed = false;
        shaft.openElevatorRightDoor();
    }

    public void movingUp(double speed, double destination) //Speed is double [0,1] where 1 represents full speed, 0 represents no speed.
    {
        this.moving = -1;
        shaft.moveElevatorUp(speed, destination);
    }

    public void movingDown(double speed, double destination)
    {
        this.moving = 1;
        shaft.moveElevatorDown(speed, destination);
    }

    public void idle()
    {
        this.moving = 0;
        shaft.idleElevator();
    }
}
