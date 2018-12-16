package ElevatorSystem.Mode;

import java.util.*;

/**
 * Simulates a mechanical failure. The probability of a mechanical failure
 * occuring is 0.5%. The duration of the mechanical failure will be a random
 * integer from 5 to 15. Duration represents the number of calls to getStatus
 * needed for the mechanical failure to end.
 */
public class MechanicalFailure {
    private static final int MIN_DURATION = 5;
    private static final int MAX_DURATION = 15;
    private static final double PROB = 0.005;

    private final Random rnd = new Random();

    private boolean failure = false;
    private int count = 0;
    private int duration = -1;

    /**
     * Returns the status of this mechanical failure.
     * 
     * @return true if there is a mechanical failure; false otherwise
     */
    public boolean getStatus() {
        /* Check if mechanical failure has ended */
        if (count == duration) {
            failure = false;
            count = 0;
            duration = -1;
            return false;
        }

        /* Check if mechanical failure is still active */
        if (failure) {
            count++;
            return true;
        }

        /* Randomly get the status of the mechanical failure */
        failure = rnd.nextInt((int) (1/PROB)) == 0;

        if (failure) {
            duration = rnd.nextInt(MAX_DURATION - MIN_DURATION) + MIN_DURATION;
        }

        return failure;
    }
}
