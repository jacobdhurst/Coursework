/*
Filename:   CabinFloorButtons.java
Student(s): Team 05
Course:     CS 460
*/

// package
package ElevatorSystem.Cabin;

// imports
import ElevatorSystem.Cabin.SimDevices.CabinFloorButtonDevice;
import ElevatorSystem.Shared.FloorEnum;
import ElevatorSystem.Shared.ElevatorEnum;
import static ElevatorSystem.Shared.Utility.pdm;

/**
 * multiplexing handler of floor-button devices for all cabins
 */
public class CabinFloorButtons {

    // private fields
    private CabinFloorButtonDevice[][] buttonDevices;
    private Boolean debugMode;

    /**
     * constructor for the floor buttons handler
     */
    public CabinFloorButtons(Boolean debugMode) {
        this.debugMode = debugMode;
        // populate references to the floor buttons
        this.buttonDevices = new CabinFloorButtonDevice[4][10];
        for (ElevatorEnum elName: ElevatorEnum.values()) {
            for (FloorEnum flName: FloorEnum.values()) {
                this.buttonDevices[elName.getIdx()][flName.getIdx()] =
                        new CabinFloorButtonDevice(elName, flName,debugMode);
            }
        }
    }

    /**
     * check the button push status of one of a cabin's floor-buttons
     * @param cID - elevator cabin being checked
     * @param fID - floor button being checked in that cabin
     * @return - true if pushed, false if not
     */
    public Boolean getButton(ElevatorEnum cID, FloorEnum fID) {
        Boolean result = this.buttonDevices[cID.getIdx()][fID.getIdx()]
                .getButtonPush();
        if (debugMode) {
            pdm("cabin " + cID + " floor button " + fID +
                        " push status - reports " + result);
        }
        return result;
    }

}



