package ElevatorCabin;

import ElevatorSystem.Direction;

public class CabinMotor {
    private float approximateLocation;

    public CabinMotor(float approximateLocation)
    {
        this.approximateLocation = approximateLocation;
    }

    float step(float speed, Direction direction)
    {
        float offset = 0.0f, step;
        if(direction.equals(Direction.UP)) offset = 1.0f;
        else if(direction.equals(Direction.DOWN)) offset = -(1.0f);

        step = speed*offset;

        approximateLocation += step;

        return approximateLocation;
    }

    int getLocation()
    {
        return (int)approximateLocation;
    }
}