/*
Filename:   CabinManager.java
Student(s): Team 05
Course:     CS 460
*/

// package
package ElevatorSystem.Cabin;

// imports
import ElevatorSystem.Shared.FloorEnum;
import ElevatorSystem.Shared.ElevatorEnum;
import java.util.*;
import static ElevatorSystem.Shared.Utility.pdm;

/**
 * class for the CabinManager object
 */
public class CabinManager {

    // private fields
    private ArrayList<LinkedList<FloorEnum>> buttonQueues; // queues
    private LinkedList<FloorEnum> allFloors; // list of defined floors
    private CabinFloorButtons buttonsHandler; // ref to button handler
    private CabinDisplay displayHandler; // ref to display handler
    private CabinFireKey keyHandler; // ref to key handler
    private CabinFloorButtonLights lightsHandler; // ref to lights handler
    private Boolean debugMode;

    /**
     * constructor for the Cabin Manager
     */
    public CabinManager(Boolean dM) {
        // instantiate objects
        this.debugMode = dM; // if true, print debug messages
        this.buttonQueues = new ArrayList<LinkedList<FloorEnum>>();
        this.allFloors = new LinkedList<FloorEnum>();
        this.buttonsHandler = new CabinFloorButtons(debugMode);
        this.displayHandler = new CabinDisplay(debugMode);
        this.keyHandler = new CabinFireKey(debugMode);
        this.lightsHandler = new CabinFloorButtonLights(debugMode);
        // populate the floor list
        this.allFloors.addAll(Arrays.asList(FloorEnum.values()));
        // populate the queue arraylist
        for (ElevatorEnum eID: ElevatorEnum.values()) {
            buttonQueues.add(new LinkedList<FloorEnum>());
        }
    }

    /**
     * Queries the Cabin Manager for the next queued passenger request
     * @return CabinRequest - either a CabinRequest, or null if no request
     */
    public CabinRequest getCabinRequest(ElevatorEnum eID) {
        // stage 1
        // check all floor-buttons in this cabin in random order
        if (debugMode) {
            pdm("Making CabinRequest for cabin " + eID);
            pdm("Initial queue for cabin " + eID + ": " +
                        buttonQueues.get(eID.getIdx()).toString());
        }
        Collections.shuffle(this.allFloors);
        for (FloorEnum fID: this.allFloors) {
            if (buttonsHandler.getButton(eID, fID)) {
                // this floor-button has been pressed in this cabin
                if (buttonQueues.get(eID.getIdx()).contains(fID)) {
                    // this floor is already in queue, don't add it again
                    // and don't turn on the button light again
                    if (debugMode) {
                        pdm("Skipping cabin " + eID + " button-push " +
                                    fID + ", already in queue");
                    }
                } else {
                    // add it to this cabin's queue
                    buttonQueues.get(eID.getIdx()).add(fID);
                    // and turn on its button-light
                    this.lightsHandler.setCabinFloorLights(eID, fID, true);
                    if (debugMode) {
                        pdm("Added cabin " + eID + " button-push " +
                                    fID + " to queue");
                    }
                }
            }
        }
        // stage 2 - create the CabinRequest
        if (debugMode) {
            pdm("Updated queue for cabin " + eID + ": " +
                        buttonQueues.get(eID.getIdx()).toString());
        }
        CabinRequest nextRequest;
        if (buttonQueues.get(eID.getIdx()).size() > 0) {
            // there is at least one item in the queue for this cabin
            FloorEnum nextFloor = buttonQueues.get(eID.getIdx()).pop();
            nextRequest = new CabinRequest(eID,
                                           nextFloor,
                                           false);
            if (debugMode) {
                pdm("Returning new CabinRequest " + nextRequest.toString());
            }
        } else {
            // there is nothing in the queue
            nextRequest = null;
            if (debugMode) {
                pdm("No CabinRequest built since nothing in queue, returning " +
                            "null");
            }
        }
        if (debugMode) {
            pdm("Final queue for cabin " + eID + ": " +
                        buttonQueues.get(eID.getIdx()).toString());
        }
        return nextRequest;
    }

    /**
     * Lets the Cabin Manager know that a cabin has arrived at its
     * destination and sets relevant buttons, arrival lights, and displays
     * @param eID - the ID of the cabin which has arrived
     * @param fID - the ID of the floor just arrived at
     * @param destReached - true if this is the destination floor, false if not
     */
    public void cabinArrival(ElevatorEnum eID,
                             FloorEnum fID,
                             Boolean destReached) {
        if (debugMode) {
            pdm("Cabin " + eID + " has arrived at floor " + fID +
                        " (destination=" + destReached + ")");
        }
        this.displayHandler.setDisplay(eID,fID);
        if (destReached) {
            // elevator has reached destination floor, turn off button light
            this.lightsHandler.setCabinFloorLights(eID, fID, false);
        }
    }

}


