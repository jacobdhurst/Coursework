/*
Filename:   CabinDisplayDevice.java
Student(s): Team 05
Course:     CS 460
*/

// package
package ElevatorSystem.Cabin.SimDevices;

// imports
import ElevatorSystem.Shared.FloorEnum;
import ElevatorSystem.Shared.ElevatorEnum;
import static ElevatorSystem.Shared.Utility.pdm;

/**
 * sim device which displays current floor location of this cabin
 * Note: this information is not actually displayed anywhere in
 * this implementation
 */
public class CabinDisplayDevice {

    // private fields
    private FloorEnum currentFloor;
    private ElevatorEnum cabinName;
    private Boolean debugMode;

    /**
     * constructor for the display sim device
     */
    public CabinDisplayDevice(ElevatorEnum eID, Boolean debugSetting) {
        this.cabinName = eID;
        this.debugMode = debugSetting;
        this.currentFloor = null; // no location information at power-on
    }

    /**
     * setter for the value of the current floor display
     */
    public void setFloor(FloorEnum fID) {
        this.currentFloor = fID;
        if (debugMode) {
            pdm(cabinName + " display reads " + this.currentFloor);
        }
    }

}
