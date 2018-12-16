/*
Filename:   LightPattern.java
Student(s): Team 05
Course:     CS 460
*/

// package
package ControlSystem;

// imports
import Primary.SignalColor;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

/**
 * class to store a complete LightPattern
 */
public class LightPattern {

    // private instance fields
    private HashMap<String, SignalColor> lightMap;
    private EnumMap<SignalColor, Integer> colorCounts;
    private int lpIndex;

    /**
     * constructor
     */
    public LightPattern(int index) {

        // store the index number
        this.lpIndex = index;

        // create new enummap to track color counts
        this.colorCounts =
                new EnumMap<SignalColor, Integer>(SignalColor.class);
        // all counters initialized to 0 on creation
        for (SignalColor sc: SignalColor.values()) {
            colorCounts.put(sc,0);
        }

        // create new hashmap for value storage
        this.lightMap = new HashMap<String, SignalColor>();
        // all lights initialized to red on creation
        for (LightName ln: LightName.values()) {
            lightMap.put(ln.toString(),SignalColor.RED);
            colorCounts.merge(SignalColor.RED, 1, (a,b) -> (a + b));
        }

    }

    /**
     * method to get the index number of this pattern
     * @return integer of the pattern's index
     */
    public int getIdx () {
        return this.lpIndex;
    }

    /**
     * getter method to determine the mix of light colors for this pattern
     * @return LightPatternMix - some value defined in LightPatternMix enum
     */
    public LightPatternMix getMix() {
        if ( (colorCounts.get(SignalColor.GREEN) > 0) &&
             (colorCounts.get(SignalColor.YELLOW) == 0) &&
             (colorCounts.get(SignalColor.BLACK) == 0) ) {
            return LightPatternMix.GREEN_RED_ONLY;
        } else if ( (colorCounts.get(SignalColor.YELLOW) > 0) &&
                    (colorCounts.get(SignalColor.BLACK) == 0) ) {
            return LightPatternMix.YELLOW_PRESENT;
        } else if (colorCounts.get(SignalColor.RED) == 16) {
            return LightPatternMix.ALL_RED;
        } else {
            return LightPatternMix.UNDEFINED;
        }
    }

    /**
     * utility getter method to print out the counts for each light color
     * @return String representation of the colorCounts map
     */
    public String getCounts() {
        return colorCounts.toString();
    }

    /**
     * setter method to set all light values in order per LightName enum
     * @param ll - array list of SignalColors to set
     */
    public Boolean setAllLights(ArrayList<SignalColor> ll) {
        if (ll.size() != LightName.values().length) {
            return false; // array list must have values for all lights
        } else {
           int idx = 0;
           for (LightName ln: LightName.values()) {
               setLight(ln,ll.get(idx));
               idx++;
           }
           return true;
        }
    }

    /**
     * setter method to set one light value
     * @return void
     */
    public void setLight(LightName ln, SignalColor sc) {
        SignalColor currentColor = lightMap.get(ln.toString());
        if (currentColor != sc) {
            lightMap.put(ln.toString(), sc);
            colorCounts.merge(currentColor, -1, Integer::sum);
            colorCounts.merge(sc, 1, Integer::sum);
        }
    }

    /**
     * getter method to get one light value
     * @return SignalColor of the specified light
     */
    public SignalColor getLight(LightName ln) {
        return lightMap.get(ln.toString());
    }

    /**
     * getter method to get all light values in order per LightName enum
     * @return ArrayList<SignalColor> list of 12 light colors in order
     */
    public ArrayList<SignalColor> getAllLights() {
        ArrayList<SignalColor> al = new ArrayList<SignalColor>();
        for (LightName ln: LightName.values()) {
            al.add(getLight(ln));
        }
        return al;
    }

}