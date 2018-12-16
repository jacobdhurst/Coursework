package ControlSystem;

import Primary.*;

/**
 * Keeps track of the current mode of the traffic control system.
 */
public class Mode {
    /* Set to true to print mode to standard out; false to suppress output */
    private static final boolean TESTING = true;

    /* The current mode will have value null by default */
    private static ModeType currentMode = null;

    /**
     * Returns int value representing the current mode. NS emergencies are given
     * priority over EW emergencies.
     * 
     * @return int value representing the current mode.
     */
    public static int getMode() {
        ModeType newMode = null;

        /* Check for emergency */
        Emergencies emerg = Emergency.getEmergency();
        if (emerg.equals(Emergencies.NORTH_EMERGENCY)) {
            newMode = ModeType.NORTH_EMERGENCY;
        } else if (emerg.equals(Emergencies.EAST_EMERGENCY)) {
            newMode = ModeType.EAST_EMERGENCY;
        } else if (emerg.equals(Emergencies.SOUTH_EMERGENCY)) {
            newMode = ModeType.SOUTH_EMERGENCY;
        } else if (emerg.equals(Emergencies.WEST_EMERGENCY)) {
            newMode = ModeType.WEST_EMERGENCY;
        }

        /* If there is no emergency, poll day/night sensor */
        if (newMode == null) {
            newMode = DayNight.DAY.getDay() ? ModeType.TIMED : ModeType.RESPONSIVE;
        }

        /* Print testing output */
        if (TESTING && !newMode.equals(currentMode)) {
            System.out.println("Using " + newMode + " mode.");
        }

        /* Update and return current mode */
        currentMode = newMode;
        return currentMode.getNum();
    }
}
