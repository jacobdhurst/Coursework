package ElevatorCabin;

import ElevatorSystem.Cabin.CabinRequest;
import ElevatorSystem.Direction;
import Simulator.Panels;

import java.util.LinkedList;

public enum CabinID {
    E1(1, Direction.STOPPED),
    E2(2, Direction.STOPPED),
    E3(3, Direction.STOPPED),
    E4(4, Direction.STOPPED);

    private int cabinID, destination;
    private float speed;
    private Direction direction;
    private Panels panel;

    CabinID(int cabinID, Direction direction){
        this.cabinID = cabinID;
        this.destination = 1;
        this.direction = direction;
        this.panel = Panels.valueOf("P"+cabinID);
    }

    public int getCabinID(){
        return cabinID;
    }

    public Panels getPanel(){
        return panel;
    }

    public void setSpeed(float speed){
        this.speed = speed;
    }

    public void setDirection(Direction direction){
        this.direction = direction;
    }

    public float getSpeed(){
        return speed;
    }

    public Direction getDirection(){
        return direction;
    }

    public void setDestination(int destination)
    {
        this.destination = destination;
    }

    public int getDestination()
    {
        return destination;
    }
}
