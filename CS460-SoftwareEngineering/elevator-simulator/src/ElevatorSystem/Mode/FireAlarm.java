package ElevatorSystem.Mode;

import java.util.*;

/**
 * Simulates a fire alarm. The probability of a fire occuring is 2%. The
 * duration of the fire alarm will be a random integer from 5 to 15. Duration
 * represents the number of calls to getStatus needed for the fire alarm to
 * deactivate.
 */
public class FireAlarm {
    private static final int MIN_DURATION = 5;
    private static final int MAX_DURATION = 15;
    private static final double PROB = 0.02;

    private final Random rnd = new Random();

    private boolean fire = false;
    private int count = 0;
    private int duration = -1;

    /**
     * Returns the status of this fire alarm.
     * 
     * @return true if there is a fire alarm; false otherwise
     */
    public boolean getStatus() {
        /* Check if fire alarm has ended */
        if (count == duration) {
            fire = false;
            count = 0;
            duration = -1;
            return false;
        }

        /* Check if fire alarm is still active */
        if (fire) {
            count++;
            return true;
        }

        /* Randomly get the status of the fire alarm */
        fire = rnd.nextInt((int) (1/PROB)) == 0;

        if (fire) {
            duration = rnd.nextInt(MAX_DURATION - MIN_DURATION) + MIN_DURATION;
        }

        return fire;
    }
}
