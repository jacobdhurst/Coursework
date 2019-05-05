/*
Filename:   CabinFireKey.java
Student(s): Team 05
Course:     CS 460
*/

// package
package ElevatorSystem.Cabin;

// imports
import ElevatorSystem.Cabin.SimDevices.CabinFireKeyDevice;
import ElevatorSystem.Shared.ElevatorEnum;

import static ElevatorSystem.Shared.Utility.pdm;

/**
 * multiplexing handler for all cabin fire key devices
 */
public class CabinFireKey {

    // private fields
    private CabinFireKeyDevice[] keyDevices;
    private Boolean debugMode;

    /**
     * constructor for cabin fire key handler
     */
    public CabinFireKey(Boolean debugSetting){
        this.debugMode = debugSetting;
        // instantiate objects
        this.keyDevices = new CabinFireKeyDevice[4];
        // populate device references
        for (ElevatorEnum elName : ElevatorEnum.values()) {
            this.keyDevices[elName.getIdx()] =
                    new CabinFireKeyDevice(elName, debugMode);
        }
    }

    /**
     * getter to determine a cabin's fire key status
     * @param eID - elevator cabin to be checked
     * @return - true if on, false if not
     */
    public Boolean hasFireKey(ElevatorEnum eID) {

        Boolean result = this.keyDevices[eID.getIdx()].getStatus();
        if (debugMode) {
            pdm("cabin " + eID + " fire key state - reports " + result);
        }
        return result;
    }

}