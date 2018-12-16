package ControlSystem;

import static ControlSystem.Utility.*;

/*Mode to handle when an emergency vehicle is present*/
public class EmergencyMode implements Schedule {
    private int currentPatternIndex;
    private final LightPatternTable lightPatterns;

    /*Constructor to set global variables*/
    public EmergencyMode (LightPatternTable lightPatterns) {
        this.lightPatterns = lightPatterns;
        this.currentPatternIndex = 17; //default initialization
    }

    /*@return the current pattern index
    * NOTE: this can be an old pattern if EmergencyMode was not updated*/
    public int getPatternIndex()
    {
        return currentPatternIndex;
    }

    /**
     * @return light pattern last set by this mode
     */
    public LightPattern getPreviousPattern() {
        return lightPatterns.getLightPattern(currentPatternIndex);
    }

    /**
     * Returns the next light pattern in timed mode.
     * @param modeNum - bound direction that emergency vehicle is approaching represented by an Int
     * @return next light pattern
     */
    public LightPattern nextPattern(int modeNum) {
        return nextPattern(modeNum, currentPatternIndex); /*will make nextPattern an all green lights in desired direction */
    }

    /**
     * find out if already in the steady state (Green for Through and Left Turn) for this mode
     * @param modeNum - modenumber of the new mode
     * @param patternIndex - pattern index currently in effect
     * @return
     */
    public Boolean isSteadyStateReached(int modeNum, int patternIndex){
        if ( (modeNum == 2 && patternIndex == 18 ) ||
                (modeNum == 3 && patternIndex == 21)  ||
                (modeNum == 4 && patternIndex == 24)  ||
                (modeNum == 5 && patternIndex == 27) ) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * Returns the next light pattern in emergency mode, depending on the given
     * light pattern index and emergency mode direction. Takes care of starting at any index in LightPatternTable.
     * It will transition to the "Emergency Patterns" (LightPatterns indices 18 - 29).
     * It will properly transition between different emergency patterns (>18 indices, for when resent to emergency mode
     * aka indices not coming from the regular patters <=18)
     *
     * @param patternIndex index of the light pattern previously set by a previous
     * @param modeNum the number associated with the direction-bound of the emergency vehicle
     * @return next light pattern*/
    public LightPattern nextPattern(int modeNum, int patternIndex){
        int next = patternIndex + 1;
        if(patternIndex % 3 == 0 && patternIndex < 18){
            if (patternIndex == 0){
                if (modeNum == 2) currentPatternIndex = 38; /*To keep lights green that are in the desired direction*/
                else if (modeNum == 4)currentPatternIndex = 42;/*To keep lights green that are in the desired direction*/
                else currentPatternIndex = next; /*Don't need to keep any of these lights green, go to all red*/
            }
            else if (patternIndex == 3) {
                if (modeNum == 2) currentPatternIndex = 30;
                else if (modeNum == 4) currentPatternIndex = 34;
                else currentPatternIndex = next;
            }
            else if (patternIndex == 6){
                if (modeNum == 3) currentPatternIndex = 40;
                else if (modeNum == 5)currentPatternIndex = 44;
                else currentPatternIndex = next;
            }
            else if (patternIndex == 9){
                if (modeNum == 3) currentPatternIndex = 32;
                else if (modeNum == 5)currentPatternIndex = 36;
                else currentPatternIndex = next;
            }
            else if (patternIndex == 12){
                if (modeNum == 2) currentPatternIndex = 30;
                else if (modeNum == 4)currentPatternIndex = 34;
                else currentPatternIndex = next;
            }
            else if (patternIndex == 15){
                if (modeNum == 3) currentPatternIndex = 32;
                else if (modeNum == 5)currentPatternIndex = 36;
                else currentPatternIndex = next;
            }
        }
        else if (!isRed(patternIndex) && patternIndex < 18)currentPatternIndex = next; /*If not red yet, go to next pattern in "regular patterns" (< 18)*/
        else if (!isDone(modeNum, patternIndex) && patternIndex >= 18 && !isRed(patternIndex) && patternIndex < 45) currentPatternIndex = next;
        /*ABOVE: Special cases: not yet at all green fro T and LT. check that it has not reached a red, since don't want to pass into next transition. */
        else {
            /*is all red (need to turn all direction bound green)OR
            *transition is done (need to turn yellow lights to red and turn rest of direction bound green)*/
            if (modeNum == 2 ) {
                if (patternIndex != 18) currentPatternIndex = 18;/* At all red or not in EMode pattern, need to be NBound Green */
                else currentPatternIndex = patternIndex; /*DON"T Go through yellow then red*/
            } else if (modeNum == 3) {
                if (patternIndex != 21) currentPatternIndex = 21;/* At all red, need to be EBound Green */
                else currentPatternIndex = patternIndex; /*DON"T Go through yellow then red*/
            } else if (modeNum == 4) {
                if (patternIndex != 25) currentPatternIndex = 24;/* At all red, need to be SBound Green */
                else currentPatternIndex = patternIndex; /*DON"T Go through yellow then red*/
            } else if (modeNum == 5) {
                if (patternIndex !=27) currentPatternIndex = 27;/* At all red, need to be WBound Green */
                else currentPatternIndex = patternIndex; /*DON"T Go through yellow then red*/
            }

        }
        //System.out.println("New pattern index = " + currentPatternIndex);
        return lightPatterns.getLightPattern(currentPatternIndex);
    }


    /*Checks if index matches special cases are set to green in desired direction and rest are red
    * @param patternIndex that represents the index of the light pattern
    * @return true if meets special case (ready to change to all green in desired direction)*/
    private boolean isDone(int modeNum ,int patternIndex ){
        if(modeNum == 2 && patternIndex == 31) return true;
        else if(modeNum == 3 && patternIndex == 33) return true;
        else if(modeNum == 4 && patternIndex == 35) return true;
        else if(modeNum == 5 && patternIndex == 37) return true;
        else if(modeNum == 2 && patternIndex == 39) return true;
        else if(modeNum == 3 && patternIndex == 41) return true;
        else if(modeNum == 4 && patternIndex == 43) return true;
        else if(modeNum == 5 && patternIndex == 45) return true;
        return false;
    }

    /*Checks if all lights are set to red
    * @param currentPatternIndex - current index that relates to light pattern
    * @return true if input is all red lights*/
    private boolean isRed(int patternIndex){
        int i;
        for (i = 2; i < 30; i += 3 ){
            if (i == patternIndex) return true;
        }
        return false;
    }

}
