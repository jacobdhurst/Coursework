package ElevatorCabin;

import ElevatorSystem.Direction;
import ElevatorSystem.Shared.FloorEnum;
import ElevatorSystem.SystemValues;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;


public class CabinMotionControl {
    private float cabinHeight, floorHeight, approximateLocation, speed;

    private int destination;

    private CabinID id;
    private CabinMotor motor;

    private Timeline motorTimer;
    private long start, elapsed, end;
    private boolean locked, timeout;

    private Direction direction;

    private MovementProfile movement;

    public CabinMotionControl(CabinID id)
    {
        this.id             = id;
        cabinHeight         = SystemValues.CabinHeight;
        floorHeight         = SystemValues.floorPos[2] - SystemValues.floorPos[1];
        approximateLocation = SystemValues.CabinStartingPos;
        destination         = (int)(approximateLocation/70);
        direction           = Direction.STOPPED;

        locked  = true;
        timeout = true;

        motor      = new CabinMotor(approximateLocation);
        motorTimer = new Timeline(new KeyFrame(Duration.millis(20), e -> motorHandle()));
        motorTimer.setCycleCount(Animation.INDEFINITE);

        movement = new MovementProfile(approximateLocation, approximateLocation, floorHeight);
    }

    public int getCurrentFloor()
    {
        approximateLocation = motor.getLocation();
        return (int)(approximateLocation/70);
    }

    public int getDestinationFloor()
    {
        return destination;
    }

    public Direction getCurrentDirection()
    {
        return direction;
    }

    public void setCurrentDirection(Direction direction)
    {
        this.direction = direction;
    }

    public boolean isLocked()
    {
        return locked;
    }

    public void setLock()
    {
        locked = true;
        motorTimer.pause();
    }

    public void moveToFloor(FloorEnum floor)
    {
        //by design this only handles movement to a single floor at a time and should not be called again until timer expires
        int distance;

        destination = floor.getFloorNum();

        if(destination > getCurrentFloor()) direction = Direction.UP;
        else if(destination < getCurrentFloor()) direction = Direction.DOWN;
        else direction = Direction.STOPPED;

        //in number of floors
        distance = Math.abs(destination - getCurrentFloor());
        if(distance != 0) distance -= 1; //quick sanity check

        start = System.currentTimeMillis();
        elapsed = System.currentTimeMillis() - start;
        end = (long)(((distance) + 2)*1000); //+1.3 second per floor, +2.6 seconds for the last floor


        motorTimer.play();
    }

    void motorHandle()
    {
//        System.out.println("Cabin " + id.getCabinID() + ": moving " + direction + " " + motor.getLocation() + " (" + getCurrentFloor() + ") rate:" +
//                           + movement.getSpeed(getCurrentFloor(), destination) + " time: (" + elapsed/1000 + "s/" + end/1000 + "s)");

        locked = false;
        elapsed = System.currentTimeMillis() - start;

        //update cabin enum either way but since we pause timer when timeout occurs must repeat a little bit of code
        if(elapsed < end)
        {
            motor.step(movement.getSpeed(getCurrentFloor(), destination), direction);
            id.setSpeed(movement.getSpeed(getCurrentFloor(), destination));
            id.setDirection(direction);
        }
        else
        {
            direction = Direction.STOPPED;
            id.setSpeed(movement.getSpeed(getCurrentFloor(), destination));
            id.setDirection(direction);
            setLock();
        }
    }
}

class MovementProfile {
    private float current, destination, floorHeight;

    MovementProfile(float current, float destination, float floorHeight)
    {
        this.current = current;
        this.destination = destination;
        this.floorHeight = floorHeight;
    }

    public float getSpeed(float current, float destination)
    {
        this.current = current;
        this.destination = destination;
        float distance = Math.abs(destination - current);

        if(distance > 2) return 2.0f;      //normal speed
        else if(distance > 0) return 1.0f; //low speed
        else return 0.0f;                   //no speed
    }
}