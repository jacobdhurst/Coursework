/*
Filename:   Utility.java
Student(s): Team 05
Course:     CS 460
*/

// package
package ControlSystem;

// imports
import java.sql.Timestamp;
import java.util.Date;

/**
 * class of miscellaneous static utility methods
 */
public class Utility {

    /**
     * print debug message (pdm)
     * utility method for printing debug messages with timestamps
     * @param s - String to print
     */
    public static void pdm(String s) {
        Date date = new Date();
        System.out.println(new Timestamp(date.getTime()) + ": " + s);
    }

}
