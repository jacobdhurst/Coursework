package ControlSystem;

/**
 * Contains the logic for timed mode.
 */
public class TimedMode implements Schedule {
    private int currentPatternIndex = 11;
    private final LightPatternTable lightPatterns;

    /**
     * Constructs a new TimedMode object with the given light pattern table
     * 
     * @param lightPatterns light pattern table to use
     */
    public TimedMode(LightPatternTable lightPatterns) {
        this.lightPatterns = lightPatterns;
    }

    /**
     * Returns the current pattern index.
     * 
     * @return current pattern index
     */
    public int getPatternIndex() {
        return currentPatternIndex;
    }

    /**
     * Returns the light pattern last set by this mode.
     * 
     * @return light pattern last set by this mode
     */
    public LightPattern getPreviousPattern() {
        return lightPatterns.getLightPattern(currentPatternIndex);
    }

    /**
     * Returns the next light pattern in timed mode.
     * 
     * @return next light pattern
     */
    public LightPattern nextPattern() {
        return nextPattern(currentPatternIndex);
    }

    /**
     * Returns the next light pattern in timed mode, depending on the given
     * light pattern index.
     * 
     * @param patternIndex index of the light pattern previously set by a
     *                     different mode
     * 
     * @return next light pattern
     */
    public LightPattern nextPattern(int patternIndex) {
        if (patternIndex == 11) {
            /* In this case, we have reached the end of timed mode so we loop
             * back to the beginning */
            currentPatternIndex = 0;
        } else if (patternIndex < 11) {
            /* In this case, we are in timed mode so we just go to the next
             * state */
            currentPatternIndex = patternIndex + 1;
        } else if (isAllRed(patternIndex)) {
            /* In this case, we are outside of timed mode but all lights are
             * red. So, we just go to the first state in timed mode */
            currentPatternIndex = 0;
        } else {
            /* In this case, we are outside of timed mode and the lights are not
             * all red. So, we just go the next state until we get to an all red
             * state so that we can transition to timed mode. However, indices
             * 31, 33, 35, 37, 39, 41, 43, and 45 require special handling.  */
             if ((patternIndex == 31) || (patternIndex == 39)) {
                 currentPatternIndex = 18;
             } else if ((patternIndex == 33) || (patternIndex == 41)) {
                 currentPatternIndex = 21;
             } else if ((patternIndex == 35) || (patternIndex == 43)) {
                 currentPatternIndex = 24;
             } else if ((patternIndex == 37) || (patternIndex == 45)) {
                 currentPatternIndex = 27;
             } else {
                 currentPatternIndex = patternIndex + 1;
             }
        }

        return lightPatterns.getLightPattern(currentPatternIndex);
    }

    /**
     * Returns true if the given pattern index represents an all red pattern.
     * Otherwise, it returns false.
     * 
     * @param patternIndex pattern index to check
     * 
     * @return true if the given pattern index represents an all red pattern;
     *         false otherwise
     */
    private boolean isAllRed(int patternIndex) {
        return (patternIndex == 2)  ||
               (patternIndex == 5)  ||
               (patternIndex == 8)  ||
               (patternIndex == 11) ||
               (patternIndex == 14) ||
               (patternIndex == 17) ||
               (patternIndex == 20) ||
               (patternIndex == 23) ||
               (patternIndex == 26) ||
               (patternIndex == 29);
    }
}
