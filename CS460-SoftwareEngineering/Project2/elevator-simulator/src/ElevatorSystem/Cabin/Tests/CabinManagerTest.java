package ElevatorSystem.Cabin.Tests;

import ElevatorSystem.Cabin.CabinRequest;
import ElevatorSystem.Shared.ElevatorEnum;
import ElevatorSystem.Shared.FloorEnum;
import ElevatorSystem.Cabin.CabinManager;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Arrays;

public class CabinManagerTest {

    private LinkedList<FloorEnum> allFloors; // list of defined floors
    private LinkedList<ElevatorEnum> allCabins; // list of defined cabins
    private Boolean debugMode;
    private CabinManager cMgr;

    /**
     * constructor
     */
    public CabinManagerTest() {

        this.debugMode = true;

        // instantiate objects
        this.cMgr = new CabinManager(debugMode);
        this.allFloors = new LinkedList<FloorEnum>();
        this.allCabins = new LinkedList<ElevatorEnum>();

        // populate the floor and cabin lists
        this.allFloors.addAll(Arrays.asList(FloorEnum.values()));
        this.allCabins.addAll(Arrays.asList(ElevatorEnum.values()));

        CabinRequest newRequest;
        Collections.shuffle(this.allCabins);

//        for (ElevatorEnum currentCabin : this.allCabins) {
//            newRequest = this.cMgr.getCabinRequest(currentCabin);
//        }

        // repeated calls to elevator 1, located at floor 1
        ElevatorEnum eID = ElevatorEnum.E1;
        for (int i = 1; i < 5; i++) {
            newRequest = this.cMgr.getCabinRequest(eID);
            for (FloorEnum fID : FloorEnum.values()) {
                if (newRequest.getDestID().equals(fID)) {
                    this.cMgr.cabinArrival(newRequest.getElevatorID(),
                            fID,
                            true);
                    break;
                } else {
                    this.cMgr.cabinArrival(newRequest.getElevatorID(),
                            fID,
                            false);
                }
            }
        }

    }


    /**
     * main method
     */
    public static void main(String args[]) {
        CabinManagerTest cmTest = new CabinManagerTest();
    }

}