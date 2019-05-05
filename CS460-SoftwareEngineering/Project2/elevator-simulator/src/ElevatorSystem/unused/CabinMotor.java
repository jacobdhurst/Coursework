package ElevatorSystem.unused;

import ElevatorSystem.unused.FloorAlignment;

import static ElevatorSystem.SystemValues.*;

/**
 * Simulates a cabin motor.
 */
public class CabinMotor {
    private FloorAlignment top;
    private FloorAlignment bottom;
    private int position = CabinStartingPos;

    public CabinMotor(FloorAlignment top, FloorAlignment bottom)
    {
        this.top = top;
        this.bottom = bottom;
        step(0);
    }
    /**
     * Take a step.
     * 
     * @param speed speed of the step
     */
    public void step(int speed) {
        this.position += speed;
        if(position >= topBarCode)
        {
            this.position = topBarCode;
        }
        else if(position <= bottomBarCode)
        {
            this.position = bottomBarCode;
        }
        top.update(position+CabinHeight);
        bottom.update(position);
    }
}
