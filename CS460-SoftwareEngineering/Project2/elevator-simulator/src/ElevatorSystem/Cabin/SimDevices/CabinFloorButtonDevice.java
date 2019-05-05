/*
Filename:   CabinFloorButtonDevice.java
Student(s): Team 05
Course:     CS 460
*/

// package
package ElevatorSystem.Cabin.SimDevices;

// imports
import ElevatorSystem.Shared.ElevatorEnum;
import ElevatorSystem.Shared.FloorEnum;
import java.util.Random;
import static ElevatorSystem.Shared.Utility.pdm;

/**
 * sim device for one floor-button in an elevator cabin
 * button pushes are throttled by "true" rolls percentage
 */
public class CabinFloorButtonDevice {

    // change % chance of getting a button push by modifying TRUENUM
    private static final int TRUENUM = 25; // rolls below this are "true"
    private static final int MAXNUM = 100; // max number for rolls
    private Random rnd;
    private ElevatorEnum cabinName;
    private FloorEnum floorName;
    private Boolean debugMode;

    /**
     * constructor for the sim device
     */
    public CabinFloorButtonDevice(ElevatorEnum eID, FloorEnum fID, Boolean
            debugSetting) {
        this.cabinName = eID;
        this.floorName = fID;
        this.rnd = new Random();
        this.debugMode = debugSetting;
    }

    /**
     * getter for the device to see if a floor's button has been pushed
     * @return - true if pushed, false if not
     */
    public Boolean getButtonPush() {
        Boolean result = (rnd.nextInt(MAXNUM) < TRUENUM);
        if (debugMode) {
            pdm(cabinName + "," + floorName + " - reports " + result);
        }
        return result;
    }

}