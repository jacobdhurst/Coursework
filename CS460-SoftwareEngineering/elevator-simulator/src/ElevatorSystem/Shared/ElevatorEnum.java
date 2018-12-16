/*
Filename:   ElevatorEnum.java
Student(s): Team 05
Course:     CS 460
*/

// package
package ElevatorSystem.Shared;

/**
 * shared enum describing valid cabins in the elevator control system
 */
public enum ElevatorEnum {
    E1 (1),
    E2 (2),
    E3 (3),
    E4 (4);

    private final int elevatorNum;

    /**
     * constructor for new enum instance
     * @param elNum - integer value of the elevator number
     */
    ElevatorEnum(int elNum) {
        this.elevatorNum = elNum;
    }

    /**
     * getter for the cabin number as an integer (1-4)
     * @return - cabin number as an int
     */
    public int getElevatorNum() {
        return this.elevatorNum;
    }

    /**
     * getter for the cabin number as an index (0-3)
     * @return - cabin number as an index
     */
    public int getIdx() { return (this.elevatorNum - 1); }

}

