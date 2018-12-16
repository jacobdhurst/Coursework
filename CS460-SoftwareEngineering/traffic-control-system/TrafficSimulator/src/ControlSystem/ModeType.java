/*
Filename:   ModeType.java
Student(s): Team 05
Course:     CS 460
*/

// package
package ControlSystem;

// imports
import java.util.Map;
import java.util.HashMap;

/**
 * enum to denote the traffic control system mode
 */
public enum ModeType {

    TIMED(0),
    RESPONSIVE(1),
    NORTH_EMERGENCY(2),
    EAST_EMERGENCY(3),
    SOUTH_EMERGENCY(4),
    WEST_EMERGENCY(5);

    // set up reverse lookup by value hashmap
    // idea adapted & updated to use generics from
    // http://www.xyzws.com/Javafaq/
    // how-to-reverse-lookup-an-enum-from-its-values-in-java/187
    private static final Map<Integer,ModeType> lookup =
            new HashMap<Integer, ModeType>();
    static {
        for(ModeType m : ModeType.values())
            lookup.put(m.getNum(), m);
    }

    private int modeNum;

    private ModeType(int modeNum) {
        this.modeNum = modeNum;
    }

    // get the integer value of a named mode
    public int getNum() { return modeNum; }

    // reverse lookup modetype by integer value
    public static ModeType getByNum(int modeNum) {
        return lookup.get(modeNum);
    }

}