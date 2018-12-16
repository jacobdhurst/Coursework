/*
Filename:   Scheduler.java
Student(s): Team 05
Course:     CS 460
*/

// package
package ControlSystem;

// imports
import java.util.EnumMap;
import static ControlSystem.Utility.pdm;

/**
 * class to manage all mode schedules
 */
public class Scheduler {

    // private instance fields
    private ModeType currentMode;
    private ModeType newMode;
    private LightPatternTable globalLPT;
    private TimedMode timedFSM;
    private ResponsiveMode responsiveFSM;
    private EmergencyMode emergencyFSM;
    private EnumMap<ModeType, Schedule> scheduleMap;
    private CarInput carSensors;
    private PedestrianInput walkButtons;
    private TimeInput stopwatch;
    private int switchTime;
    private Boolean modeChanged;

    /**
     * constructor for the Scheduler
     */
    public Scheduler() {

        this.currentMode = null; // invalid val. on creation;
        this.newMode = null; // invalid val. on creation
        this.modeChanged = false;

        // create the global light pattern table used by all schedules
        this.globalLPT = new LightPatternTable();

        // create schedule instances of each type
        this.timedFSM =  new TimedMode(globalLPT);
        this.responsiveFSM =  new ResponsiveMode(globalLPT);
        this.emergencyFSM =  new EmergencyMode(globalLPT);

        // create lookup enummap
        this.scheduleMap = new EnumMap<ModeType, Schedule>(ModeType.class);
        scheduleMap.put(ModeType.TIMED, this.timedFSM);
        scheduleMap.put(ModeType.RESPONSIVE, this.responsiveFSM);
        scheduleMap.put(ModeType.NORTH_EMERGENCY, this.emergencyFSM);
        scheduleMap.put(ModeType.EAST_EMERGENCY, this.emergencyFSM);
        scheduleMap.put(ModeType.SOUTH_EMERGENCY, this.emergencyFSM);
        scheduleMap.put(ModeType.WEST_EMERGENCY, this.emergencyFSM);

        // create timer instance
        this.stopwatch = new TimeInput();
        this.switchTime = 0;

        // create sensor input instances
        this.carSensors = new CarInput();
        this.walkButtons = new PedestrianInput();

    }

    /**
     * public method of the Scheduler, used to set current mode and
     * return a LightPattern
     * @param mNum - integer denoting the Mode for this cycle
     * @return - the next LightPattern to set
     */
    public LightPattern getNextPattern(int mNum) {

        // reverse lookup the mode type from the enum
        newMode = ModeType.getByNum(mNum);

        // check for bad mode number being passed in
        if (newMode == null) {
            //pdm("Error - invalid mode sent to scheduler: " + mNum);
            return null;
        }

        // check for mode change (2 cases)
        modeChanged = false;
        if ((currentMode == null) || (currentMode.getNum() != mNum)) {
            modeChanged = true;
        }

        switch (newMode) {

            case TIMED: { return processTimedMode(); }

            case RESPONSIVE: { return processResponsiveMode(); }

            case NORTH_EMERGENCY:
            case EAST_EMERGENCY:
            case SOUTH_EMERGENCY:
            case WEST_EMERGENCY: { return processEmergencyMode(); }

            default: {
                //pdm("Error - invalid mode sent to scheduler: " + mNum);
                return null;
            }

        }

    }

    /**
     * method to retrieve the next switch time for this light pattern
     * and mode
     * TODO - not a fan of hardcoding these values into a method; if time allows
     * see if there is a way to make an external enum with values
     * @param lp - lightpattern being evaluated
     * @param currentMode - the current mode
     * @return - integer of seconds of new switchTime (-1 if UNDEFINED lp)
     */
    private int getSwitchTime (LightPattern lp, ModeType currentMode) {

        LightPatternMix newMix = lp.getMix();
        int newTime;

        switch (newMix) {

            case GREEN_RED_ONLY: {

                switch (currentMode) {
                    case TIMED: {
                        newTime = 7;
                        break;
                    }
                    case RESPONSIVE: {
                        newTime = 5;
                        break;
                    }
                    case NORTH_EMERGENCY:
                    case EAST_EMERGENCY:
                    case SOUTH_EMERGENCY:
                    case WEST_EMERGENCY: {
                        newTime = 1; // per discussion with team, 0 is unsafe
                        break;
                    }
                    default: {
                        newTime = -1;
                    }
                }
                break;
            }
            case YELLOW_PRESENT: {
                newTime = 2;
                break;
            }
            case ALL_RED: {
                newTime = 1;
                break;
            }
            default: {
                newTime = -1;
            }
        }
        return newTime;
    }

    /**
     * process schedule in Timed Mode
     * @return the new LightPattern to put into effect
     */
    private LightPattern processTimedMode() {

        LightPattern newLp;

        // update switchtime for present pattern in mode for this cycle
        if (currentMode != null) {
            // Scheduler has been called at least once before
            switchTime = getSwitchTime(
                    scheduleMap.get(this.currentMode).getPreviousPattern(),
                    newMode);
        }
        // check time remaining
        int timeRemaining;
        if (modeChanged) {
            timeRemaining = 0;
        } else {
            timeRemaining = switchTime - stopwatch.getTime();
        }

        // has timer elapsed?
        if (timeRemaining <= 0) {
            // either first run or time elapsed, time to get a new pattern
            if (modeChanged && (currentMode == null)) {
                // case 1 - this is the first time Scheduler has been called
                //pdm(newMode.toString() + ", time elapsed, first run");
                // update currentMode
                this.currentMode = newMode;
                // retrieve the next pattern for this mode
                newLp = timedFSM.nextPattern();
            } else if (modeChanged) {
                // case 2 - Scheduler has been called at least once before
                // and the mode has changed
                // determine the current pattern index in effect in old mode
                int currentPatternIdx =
                        scheduleMap.get(this.currentMode).getPatternIndex();
//                pdm(newMode.toString() + ", time elapsed, mode changed - old " +
//                            "pattern idx " + currentPatternIdx);
                // update the current mode
                this.currentMode = newMode;
                // retrieve the new mode's next pattern after this one
                newLp = timedFSM.nextPattern(currentPatternIdx);
            } else {
                // case 3 - Scheduler has been called at least once before
                // and the mode has not changed
                //pdm(newMode.toString() + ", time elapsed, same mode");
                // retrieve the next pattern for this mode
                newLp = timedFSM.nextPattern();
            }
            // update timer for new pattern
            switchTime = getSwitchTime(newLp, currentMode);
            stopwatch.resetTime();
            //pdm("new pattern switchtime is " + switchTime);
        } else {
            // timer is still running
            // return the presently set pattern only
            //pdm(newMode.toString() + ", remaining time " + timeRemaining);
            newLp = timedFSM.getPreviousPattern();
        }
        // return the new pattern
        return newLp;

    }

    /**
     * process schedule in Responsive Mode
     * @return the new LightPattern to put into effect
     */
    private LightPattern processResponsiveMode() {

        // local fields
        LightPattern newLp;

        // update switchtime for present pattern in mode for this cycle
        if (currentMode != null) {
            // Scheduler has been called at least once before
            switchTime = getSwitchTime(
                    scheduleMap.get(this.currentMode).getPreviousPattern(),
                    newMode);
        }

        // check time remaining
        int timeRemaining;
        if (modeChanged) {
            timeRemaining = 0;
        } else {
            timeRemaining = switchTime - stopwatch.getTime();
        }

        // refresh sensors
        int[] carStatus = carSensors.getStatus();
        int[] pedStatus = walkButtons.getStatus();

        // has timer elapsed?
        //pdm(newMode.toString() + ", remaining time " + timeRemaining);
        if (timeRemaining <= 0) {
            // either first run or time elapsed, time to get a new pattern
            if (modeChanged && (currentMode == null)) {
                // case 1 - this is the first time Scheduler has been called
                //pdm(newMode.toString() + ", time elapsed, first run");
                // update currentMode
                this.currentMode = newMode;
                // retrieve the next pattern for this mode
                newLp = responsiveFSM.nextPattern(carStatus, pedStatus);
            } else if (modeChanged) {
                // case 2 - Scheduler has been called at least once before
                // determine the current pattern index in effect in old mode
                int currentPatternIdx =
                        scheduleMap.get(this.currentMode).getPatternIndex();
                //pdm(newMode.toString() + ", time elapsed, mode changed -
                // old pattern idx " + currentPatternIdx);
                // update the current mode
                this.currentMode = newMode;
                // retrieve the new mode's next pattern after this one
                newLp = responsiveFSM.nextPattern(currentPatternIdx,
                                                  carStatus,
                                                  pedStatus);
            } else {
                // case 3 - Scheduler has been called at least once before
                // and the mode has not changed
                //pdm(newMode.toString() + ", time elapsed, same mode");
                // retrieve the next pattern for this mode
                newLp = responsiveFSM.nextPattern(carStatus,
                                                  pedStatus);
            }
            // update timer for new pattern
            switchTime = getSwitchTime(newLp, currentMode);
            stopwatch.resetTime();
            //pdm("new pattern switchtime is " + switchTime);
        } else {
            // timer is still running
            // return the presently set pattern only
            //pdm(newMode.toString() + ", remaining time " + timeRemaining);
            newLp = responsiveFSM.getPreviousPattern();
        }
        // return the new pattern
        return newLp;

    }

    /**
     * process schedule in Emergency Mode
     * @return the new LightPattern to put into effect
     */
    private LightPattern processEmergencyMode() {

        LightPattern newLp;

        // first need to ask EmergencyMode if it is already in steady state
        int currentPatternIdx =
                        scheduleMap.get(this.currentMode).getPatternIndex();
        if (emergencyFSM.isSteadyStateReached(newMode.getNum(),
                                              currentPatternIdx)) {
            // emergencyMode is already in the desired pattern, no nextpattern
            // needed and no timer necessary
            // pdm(newMode.toString() + ", steady state pattern already set");
            newLp = emergencyFSM.getPreviousPattern();
        } else {
            // emergencyMode needs a couple of cycles to get to steady state
            // revert to timed behavior to ask for next pattern

            // update switchtime for present pattern in mode for this cycle
            if (currentMode != null) {
                // Scheduler has been called at least once before
                switchTime = getSwitchTime(
                        scheduleMap.get(this.currentMode).getPreviousPattern(),
                        newMode);
            }
            // check time remaining
            int timeRemaining;
            if (modeChanged) {
                timeRemaining = 0;
            } else {
                timeRemaining = switchTime - stopwatch.getTime();
            }
            // has timer elapsed?
            if (timeRemaining <= 0) {
                // either first run or time elapsed, time to get a new pattern
                if (modeChanged && (currentMode == null)) {
                    // case 1 - this is the first time Scheduler has been called
                    //pdm(newMode.toString() + ", time elapsed, first run");
                    // update currentMode
                    this.currentMode = newMode;
                    // retrieve the next pattern for this mode
                    newLp = emergencyFSM.nextPattern(newMode.getNum(),
                                                     currentPatternIdx);
                } else if (modeChanged) {
                    // case 2 - Scheduler has been called at least once before
                    // and the mode has changed
                    // determine the current pattern index in effect in old mode
                    //pdm(newMode.toString() + ", time elapsed, mode changed");
                    // update the current mode
                    this.currentMode = newMode;
                    // retrieve the new mode's next pattern after this one
                    newLp = emergencyFSM.nextPattern(newMode.getNum(),
                                                     currentPatternIdx);
                } else {
                    // case 3 - Scheduler has been called at least once before
                    // and the mode has not changed
                    //pdm(newMode.toString() + ", time elapsed, same mode");
                    // retrieve the next pattern for this mode
                    newLp = emergencyFSM.nextPattern(newMode.getNum());
                }
                // update timer
                switchTime = getSwitchTime(newLp, currentMode);
                stopwatch.resetTime();
                //pdm("new switchtime is " + switchTime);
            } else {
                // timer is still running
                // return the presently set pattern only
                //pdm(newMode.toString() + ", remaining time " + timeRemaining);
                newLp = emergencyFSM.getPreviousPattern();
            }
        }
        // return the new pattern
        return newLp;

    }

}