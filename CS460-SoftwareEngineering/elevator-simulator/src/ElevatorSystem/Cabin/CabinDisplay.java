/*
Filename:   CabinDisplay.java
Student(s): Team 05
Course:     CS 460
*/

// package
package ElevatorSystem.Cabin;

// imports
import ElevatorSystem.Cabin.SimDevices.CabinDisplayDevice;
import ElevatorSystem.Shared.ElevatorEnum;
import ElevatorSystem.Shared.FloorEnum;
import static ElevatorSystem.Shared.Utility.pdm;

/**
 * multiplexing handler for all cabin display devices
 */
public class CabinDisplay
{
    // private fields
    private CabinDisplayDevice[] displayDevices; // refs to sim devices
    private Boolean debugMode;

    /**
     * constructor for the cabin display handler
     */
    public CabinDisplay(Boolean debugSetting){
        // instantiate objects
        this.displayDevices = new CabinDisplayDevice[4];
        this.debugMode = debugSetting;
        // populate device references
        for (ElevatorEnum elName : ElevatorEnum.values()) {
            this.displayDevices[elName.getIdx()] =
                    new CabinDisplayDevice(elName, debugMode);
        }
    }

    /**
     * setter to update a cabin display
     * @param eID - the cabin display to be updated
     * @param fID - the floor number to be displayed
     */
    public void setDisplay(ElevatorEnum eID, FloorEnum fID){
        if (debugMode) {
            pdm("setting cabin " + eID + " display to " + fID);
        }
        this.displayDevices[eID.getIdx()].setFloor(fID);
    }

}