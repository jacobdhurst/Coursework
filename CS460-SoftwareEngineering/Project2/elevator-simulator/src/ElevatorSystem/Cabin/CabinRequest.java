/*
Filename:   CabinRequest.java
Student(s): Team 05
Course:     CS 460
*/

// package
package ElevatorSystem.Cabin;

// imports
import ElevatorSystem.Shared.ElevatorEnum;
import ElevatorSystem.Shared.FloorEnum;

/**
 * class for a CabinRequest object
 * one request per instance; CabinManager tracks all cabin
 * button pushes and creates one cabin request object per each call
 * to CabinManager's getCabinRequest() method
 */
public class CabinRequest {

    // private fields
    private ElevatorEnum elevatorID;
    private FloorEnum destination;
    private Boolean fireKeyOn;

    // constructor for new CabinRequest
    public CabinRequest(ElevatorEnum eID,
                        FloorEnum destFloor,
                        Boolean fireKeyState) {
        this.elevatorID = eID;
        this.destination = destFloor;
        this.fireKeyOn = fireKeyState;
    }

    /**
     * getter for elevator ID (E1 - E3)
     * @return ElevatorEnum
     */
    public ElevatorEnum getElevatorID() {
        return this.elevatorID;
    }

    /**
     * getter for destination floor (F1 - F10)
     * @return FloorEnum
     */
    public FloorEnum getDestID() {
        return this.destination;
    }

    /**
     * getter for elevator number (1-4)
     * @return int
     */
    public int getElevatorNum() {
        return this.elevatorID.getElevatorNum();
    }

    /**
     * getter for elevator index (0-3)
     * @return int
     */
    public int getElevatorIdx() {
        return this.elevatorID.getIdx();
    }

    /**
     * getter for destination floor number (1-10)
     * @return int
     */
    public int getDestNum() {
        return this.destination.getFloorNum();
    }

    /**
     * getter for destination floor index (0-9)
     * @return int
     */
    public int getDestIdx() {
        return this.destination.getIdx();
    }

    /**
     * getter for fire key state
     * @return Boolean
     */
    public Boolean getEmergency() {
        return this.fireKeyOn;
    }

    @Override
    /**
     * toString override method
     */
    public String toString() {
        return (this.elevatorID + "," +
                this.destination + "," +
                this.fireKeyOn);
    }

}