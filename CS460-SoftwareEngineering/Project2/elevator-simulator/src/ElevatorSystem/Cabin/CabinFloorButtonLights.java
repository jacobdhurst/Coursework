/*
Filename:   CabinFloorButtonLights.java
Student(s): Team 05
Course:     CS 460
*/

// package
package ElevatorSystem.Cabin;

// imports
import ElevatorSystem.Cabin.SimDevices.CabinFloorButtonLightDevice;
import ElevatorSystem.Shared.ElevatorEnum;
import ElevatorSystem.Shared.FloorEnum;
import static ElevatorSystem.Shared.Utility.pdm;

/**
 * multiplexing handler for all cabin floor-button light devices
 */
public class CabinFloorButtonLights {

    // private fields
    private CabinFloorButtonLightDevice[][] lightDevices;
    private Boolean debugMode;

    /**
     * constructor for the cabin button lights handler
     */
    public CabinFloorButtonLights(Boolean debugSetting) {
        this.debugMode = debugSetting;
        // instantiate objects
        this.lightDevices = new CabinFloorButtonLightDevice[4][10];
        // populate device references
        for (ElevatorEnum elName : ElevatorEnum.values()) {
            for (FloorEnum flName : FloorEnum.values()) {
                this.lightDevices[elName.getIdx()][flName.getIdx()] =
                    new CabinFloorButtonLightDevice(elName, flName, debugMode);
            }
        }
    }

    /**
     * setter to change one floor-button light state
     * @param eID - elevator cabin where the light is located
     * @param fID - floor number of the button to change
     */
    public void setCabinFloorLights(ElevatorEnum eID,
                                    FloorEnum fID,
                                    Boolean lightState) {
        if (debugMode) {
            pdm("setting cabin " + eID + " button light " + fID +
                        " to " + lightState);
        }
        this.lightDevices[eID.getIdx()][fID.getIdx()].setLight(lightState);
    }

}