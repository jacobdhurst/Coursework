/*
Filename:   CabinFireKeyDevice.java
Student(s): Team 05
Course:     CS 460
*/

// package
package ElevatorSystem.Cabin.SimDevices;

// imports
import ElevatorSystem.Shared.ElevatorEnum;
import static ElevatorSystem.Shared.Utility.pdm;
import java.util.Random;

/**
 * sim device for setting the fire key to on, some percent of the time
 */
public class CabinFireKeyDevice {

    // change percentage chance by modifying TRUENUM (currently 33%)
    private static final int TRUENUM = 33; // rolls below this value are "true"
    private static final int MAXNUM = 100; // max number for rolls
    private Random rnd;
    private ElevatorEnum cabinName;
    private Boolean debugMode;

    /**
     * constructor for the fire key sim device
     */
    public CabinFireKeyDevice(ElevatorEnum eID, Boolean debugSetting) {
        this.debugMode = debugSetting;
        this.cabinName = eID;
        this.rnd = new Random();
    }

    /**
     * getter for status of fire key
     *
     * @return true if the fire key has been turned, false otherwise
     */
    public boolean getStatus() {
        Boolean result = (rnd.nextInt(MAXNUM) < TRUENUM);
        if (debugMode) {
            pdm(cabinName + " - reports " + result);
        }
        return result;
    }

}
