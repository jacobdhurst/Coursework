/*
Filename:   LightPatternMix.java
Student(s): Team 05
Course:     CS 460
*/

// package
package ControlSystem;

/**
 * enum to describe light color mixes in a LightPattern
 * which can be used by other classes to determine timer settings
 */
public enum LightPatternMix {
    GREEN_RED_ONLY, // at least one green , reds possible, no yellow or black
    YELLOW_PRESENT, // at least one yellow, reds & greens possible, no black
    ALL_RED, // only red lights
    UNDEFINED // some other combination (eg, some mix of red, black)
}
