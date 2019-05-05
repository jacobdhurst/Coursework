/*
Filename:   Utility.java
Student(s): Team 05
Course:     CS 460
Note:       this class adapted from previous T05 code written for
            the CS 460 traffic control system project
*/

// package
package ElevatorSystem.Shared;

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
        String caller = new Exception().getStackTrace()[1].getClassName();
        String simpleClassName = caller.substring(caller.lastIndexOf('.') + 1);
        Date date = new Date();
        System.out.println(new Timestamp(date.getTime()) +
                                   " [" + simpleClassName + "] " + s);
    }

}