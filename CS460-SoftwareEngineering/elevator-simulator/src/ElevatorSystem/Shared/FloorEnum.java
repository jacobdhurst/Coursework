/*
Filename:   FloorEnum.java
Student(s): Team 05
Course:     CS 460
*/

// package
package ElevatorSystem.Shared;

/**
 * shared enum describing valid floors in the elevator control system
 */
public enum FloorEnum {
    F1 (1),
    F2 (2),
    F3 (3),
    F4 (4),
    F5 (5),
    F6 (6),
    F7 (7),
    F8 (8),
    F9 (9),
    F10 (10);

    private final int floorNum;

    /**
     * constructor for new enum instance
     * @param fNum - integer value of the floor number
     */
    FloorEnum(int fNum) {
        this.floorNum = fNum;
    }

    /**
     * getter for the floor number as an integer (1-10)
     * @return - floor number as an int
     */
    public int getFloorNum() {
        return this.floorNum;
    }

    /**
     * getter for the floor number as an index (0-9)
     * @return - floor number as an index
     */
    public int getIdx() { return (this.floorNum - 1); }

}
