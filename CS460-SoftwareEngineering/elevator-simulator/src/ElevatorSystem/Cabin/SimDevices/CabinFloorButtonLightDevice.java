/*
Filename:   CabinFloorButtonLightDevice.java
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
 * sim device for the light behind one floor-push button in a cabin
 */
public class CabinFloorButtonLightDevice {

    // private fields
    private ElevatorEnum cabinName;
    private FloorEnum floorName;
    private Boolean debugMode;
    private Boolean lightIsOn;

    /**
     * constructor for the sim device
     */
    public CabinFloorButtonLightDevice(ElevatorEnum eID,
                                       FloorEnum fID,
                                       Boolean debugSetting) {
        this.lightIsOn = false;
        this.cabinName = eID;
        this.floorName = fID;
        this.debugMode = debugSetting;
    }

    /**
     * setter for button lights
     * @param turnLightOn - true to set on, false to set off
     */
    public void setLight(Boolean turnLightOn) {
        this.lightIsOn = turnLightOn;
        if (debugMode) {
            pdm(cabinName + " button " + floorName + " lit state is " +
                        this.lightIsOn);
        }
    }

}
