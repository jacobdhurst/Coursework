/*
Filename:   ModeType.java
Student(s): Team 05
Course:     CS 460
*/

// package
package ControlSystem;

/**
 * common interface for all Schedule implementations
 */
public interface Schedule {

    /**
     * Returns the current pattern index.
     *
     * @return current pattern index
     */
    public int getPatternIndex();

    /**
     * Returns the light pattern last set by this mode.
     *
     * @return light pattern last set by this mode
     */
    public LightPattern getPreviousPattern();

}