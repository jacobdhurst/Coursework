package ElevatorSystem.Cabin.Tests;

// imports
import ElevatorSystem.Cabin.SimDevices.*;
import ElevatorSystem.Shared.ElevatorEnum;
import ElevatorSystem.Shared.FloorEnum;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import static ElevatorSystem.Shared.Utility.pdm;

public class DeviceTest {

    private LinkedList<FloorEnum> allFloors; // list of defined floors
    private LinkedList<ElevatorEnum> allCabins; // list of defined cabins
    private CabinFloorButtonDevice[][] buttonDevices;
    private CabinFireKeyDevice[] keyDevices;
    private CabinDisplayDevice[] displayDevices;
    private CabinFloorButtonLightDevice[][] lightDevices;
    private Boolean debugMode;
    private Random rnd;

    /**
     * constructor for DeviceTest
     */
    public DeviceTest() {

        this.debugMode = true;
//        this.debugMode = false;

        // instantiate objects
        this.rnd = new Random();
        this.allFloors = new LinkedList<FloorEnum>();
        this.allCabins = new LinkedList<ElevatorEnum>();
        this.buttonDevices = new CabinFloorButtonDevice[4][10];
        this.keyDevices = new CabinFireKeyDevice[4];
        this.displayDevices = new CabinDisplayDevice[4];
        this.lightDevices = new CabinFloorButtonLightDevice[4][10];

        // populate the floor and cabin lists
        this.allFloors.addAll(Arrays.asList(FloorEnum.values()));
        this.allCabins.addAll(Arrays.asList(ElevatorEnum.values()));
        // populate device references
        for (ElevatorEnum elName : ElevatorEnum.values()) {
            for (FloorEnum flName : FloorEnum.values()) {
                this.buttonDevices[elName.getIdx()][flName.getIdx()] =
                   new CabinFloorButtonDevice(elName, flName, debugMode);
                this.lightDevices[elName.getIdx()][flName.getIdx()] =
                   new CabinFloorButtonLightDevice(elName, flName, debugMode);
            }
            this.keyDevices[elName.getIdx()] =
                    new CabinFireKeyDevice(elName, debugMode);
            this.displayDevices[elName.getIdx()] =
                    new CabinDisplayDevice(elName, debugMode);
        }
        Boolean result;
        Collections.shuffle(this.allCabins);
        for (ElevatorEnum currentCabin : this.allCabins) {
            // check all floor-buttons in random order
            Collections.shuffle(this.allFloors);
            for (FloorEnum currentFloor : this.allFloors) {
                // button push test
                result = this.buttonDevices[currentCabin.getIdx()
                        ][currentFloor.getIdx()].getButtonPush();
                // update light test
                this.lightDevices[currentCabin.getIdx()
                        ][currentFloor.getIdx()].setLight(result);
                if (debugMode) {
                    pdm("button push test - " + currentCabin + "," +
                                currentFloor + " - reports " + result);
                    pdm("light update test - " + currentCabin + "," +
                                currentFloor + " - set to " + result);

                }
            }
            // fire key test
            result = this.keyDevices[currentCabin.getIdx()].getStatus();
            if (debugMode) {
                pdm("fire key test - " + currentCabin + " - reports " + result);
            }
            // display test
            FloorEnum newFloor = FloorEnum.values()[rnd.nextInt(10)];
            if (debugMode) {
                pdm("display update test - setting " + currentCabin +
                            " to " + newFloor);
            }
            this.displayDevices[currentCabin.getIdx()].setFloor(newFloor);
        }
    }

    /**
     * main method
     */
    public static void main(String args[]) {
        DeviceTest dTest = new DeviceTest();
    }

}