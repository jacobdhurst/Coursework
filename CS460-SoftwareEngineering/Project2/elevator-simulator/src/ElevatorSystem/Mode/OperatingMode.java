package ElevatorSystem.Mode;

import java.util.*;

/**
 * Used to get the current mode of each elevator. The first time getModes is
 * called, it will return PowerUp mode for all elevators. Then, it will switch
 * modes according to the FireAlarm and MechanicalFailure sensors. When a
 * Failure mode for an elevator ends, that elevator will go into PowerUp mode.
 * 
 * Note that PowerUp mode is only returned once and then it switches to another
 * mode. This is done because OperatingMode does not have a way to know when
 * PowerUp mode has ended. So, when OperatingMode returns PowerUp mode, it is
 * up to the other components to ensure that the elevator knows its position
 * before starting normal operation.
 */
public class OperatingMode {
    private boolean[] powerUp = new boolean[4];
    private boolean[] failure = new boolean[4];

    private FireAlarm fireAlarm = new FireAlarm();
    private MechanicalFailure[] mechanicalFailure = new MechanicalFailure[4];

    /**
     * Constructs a new OperatingMode object.
     */
    public OperatingMode() {
        Arrays.fill(powerUp, true);
        Arrays.fill(failure, false);

        for (int i = 0; i < 4; i++) {
            mechanicalFailure[i] = new MechanicalFailure();
        }
    }

    /**
     * Returns the mode of each elevator. An array of modes is returned instead
     * of a single mode because some elevators might be experiencing mechanical
     * failures while the rest could be working properly.
     * 
     * @return array of 4 modes, one for each elevator
     */
    public Mode[] getModes() {
        Mode[] modes = new Mode[4];

        for (int i = 0; i < 4; i++) {
            /* Check for power up */
            if (powerUp[i]) {
                powerUp[i] = false;
                modes[i] = Mode.POWER_UP;
                continue;
            }

            /*
             * Check for mechanical failure. If we resolve a mechanical failure,
             * then return PowerUp mode. */
            if (mechanicalFailure[i].getStatus()) {
                failure[i] = true;
                modes[i] = Mode.FAILURE;
            } else if (failure[i]) {
                failure[i] = false;
                modes[i] = Mode.POWER_UP;
            }
        }

        /* Check for fire */
        if (fireAlarm.getStatus()) {
            for (int i = 0; i < 4; i++) {
                if (modes[i] == null) {
                    modes[i] = Mode.FIRE;
                }
            }
        } else {
            /* Once we are here, any elevator where the mode hasn't been set
             * yet is in normal mode */
            for (int i = 0; i < 4; i++) {
                if (modes[i] == null) {
                    modes[i] = Mode.NORMAL;
                }
            }
        }

        return modes;
    }
}
