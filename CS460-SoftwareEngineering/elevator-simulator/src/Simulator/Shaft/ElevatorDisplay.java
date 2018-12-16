package Simulator.Shaft;

import ElevatorCabin.DoorStatus;
import javafx.animation.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

/**
 * Visual representation of the elevator on the shaft.
 */
class ElevatorDisplay {
    private double shaftWidth, shaftHeight, floorHeight, location, destination;
    private double elevatorTranslate, elevatorOffset, elevatorSpeed;
    private double separationL, separationR, doorSpeed, opened, x;

    private boolean openingLeft, openingRight;

    private Timeline motorTimeline;
    private Timeline leftDoorTimeline, rightDoorTimeline;
    private Shape elevator;
    private Rectangle frame, leftDoor, rightDoor;
    private DoorStatus status = DoorStatus.CLOSED;

    /**
     * Default constructor.
     * @param shaftWidth - Width of the shaft.
     * @param shaftHeight - Height of the shaft.
     */
    ElevatorDisplay(double shaftWidth, double shaftHeight)
    {
        this.shaftWidth = shaftWidth;
        this.shaftHeight = shaftHeight;
        floorHeight = shaftHeight/10;
        location = shaftHeight-(floorHeight-10);
        destination = location;

        initializeElevatorMotor();
        initializeDoorMotors();
    }

    /**
     * Method to begin smoothly translating the elevator upwards.
     * Translates upwards until stopped or until top is reached.
     * Should only be called after first idling the elevator.
     */
    void moveUp(double speed, double destination)
    {
        this.destination = shaftHeight-(destination*floorHeight);
        if(speed == 1.75) speed = 1.25; //1.2175;
        else if(speed == 0.875) speed = 0.6; //0.5875;

        elevatorOffset = -1*speed;
        motorTimeline.play();
    }

    /**
     * Method to begin smoothly translating the elevator downwards.
     * Translates downwards until stopped or until bottom is reached.
     * Should only be called after first idling the elevator.
     */
    void moveDown(double speed, double destination)
    {
        this.destination = shaftHeight-(destination*floorHeight);
        if(speed == 1.75) speed = 1.25;
        else if(speed == 0.875) speed = 0.6;

        elevatorOffset = 1*speed;
        motorTimeline.play();
    }

    /**
     * Method to stop/idle the elevator.
     * CabinID will briefly decelerate before completely idling.
     */
    void idle()
    {

        elevatorOffset = 0;
        motorTimeline.pause();
    }

    /**
     * Method to smoothly open the doors of the elevator.
     * Note: Does not check whether the elevator is in motion.
     */
    void openLeftDoor()
    {
        openingLeft = true;
        leftDoorTimeline.play();
    }


    /**
     * Method to smoothly close the doors of the elevator.
     * Note: Does not check whether the elevator is in motion.
     */
    void closeLeftDoor()
    {
        openingLeft = false;
        leftDoorTimeline.play();
    }

    /**
     * Method to smoothly open the doors of the elevator.
     * Note: Does not check whether the elevator is in motion.
     */
    void openRightDoor()
    {
        status = DoorStatus.OPENING;
        openingRight = true;
        rightDoorTimeline.play();
    }


    /**
     * Method to smoothly close the doors of the elevator.
     * Note: Does not check whether the elevator is in motion.
     */
    void closeRightDoor()
    {
        status = DoorStatus.CLOSING;
        openingRight = false;
        rightDoorTimeline.play();
    }

    /**
     * @return location - The top Y coordinate of the elevator, to get the bottom Y coordinate, just add floorHeight.
     */
    double getLocation() {
        return location;
    }

    /**
     * @return doors - The rectangle representing the elevators doors.
     */
    Rectangle[] getDoors()
    {
        Rectangle doors[] = {frame, leftDoor, rightDoor};
        return doors;
    }

    /**
     * @return elevator - The shape representing our elevator.
     */
    Shape getElevator()
    {
        return elevator;
    }

    /**
     * @return status - The status of the doors { open, closed, opening, closing }.
     */
    DoorStatus getDoorStatus() {
        return status;
    }

    /**
     * Helper method to handle the contstruction of the elevator.
     */
    private void initializeElevatorMotor()
    {
        Rectangle base = new Rectangle(shaftWidth*.3, location, shaftWidth*.4, floorHeight*.8);
        Rectangle latch = new Rectangle(shaftWidth*.45, location-5, shaftWidth*.1, floorHeight*.2);

        elevator = Shape.union(base, latch);
        elevator.setFill(Color.valueOf("#C0C0C0"));

        elevatorTranslate = 0;
        elevatorOffset = 0;
        elevatorSpeed = 20;

        motorTimeline = new Timeline(new KeyFrame(Duration.millis(elevatorSpeed), e -> {
            if(location+elevatorOffset <= shaftHeight-60 && location+elevatorOffset >= 10) {
                if(location <= destination && location >= destination-10)
                {
//                    System.out.println(location + " OF " + (destination) + " / " + (destination-10) + "  " + (10 - destination/floorHeight));
//                    System.out.println("EARLY STOP");
                    motorTimeline.pause();
                }

                elevatorTranslate += elevatorOffset;
                location += elevatorOffset;

                elevator.setTranslateY(elevatorTranslate);
                frame.setTranslateY(elevatorTranslate);
                leftDoor.setTranslateY(elevatorTranslate);
                rightDoor.setTranslateY(elevatorTranslate);
            }
            else motorTimeline.pause();
        }));
        motorTimeline.setCycleCount(Animation.INDEFINITE);
    }

    /**
     * Helper method to handle the construction of the elevator doors.
     */
    private void initializeDoorMotors()
    {
        frame = new Rectangle(shaftWidth*.4, location+7, 30, floorHeight*.6);
        leftDoor = new Rectangle(shaftWidth*.4, location+7, 0, floorHeight*.6);
        rightDoor = new Rectangle(shaftWidth*.6, location+7, 0, floorHeight*.6);
        frame.setFill(Color.BLACK);
        leftDoor.setFill(Color.valueOf("#C0C0C0"));
        rightDoor.setFill(Color.valueOf("#C0C0C0"));

        openingLeft = false;
        opened = 5;
        doorSpeed = 10;

        leftDoorTimeline = new Timeline(new KeyFrame(Duration.millis(doorSpeed), e -> {
            separationL = leftDoor.getWidth();

            if (openingLeft && separationL > opened-5) {
                leftDoor.setWidth(separationL - 0.5);
            } else if (!openingLeft && separationL < opened+10) {
                leftDoor.setWidth(separationL + 0.5);
            } else {
                leftDoorTimeline.pause();
            }
        }));
        leftDoorTimeline.setCycleCount(Animation.INDEFINITE);
        leftDoorTimeline.play();

        rightDoorTimeline = new Timeline(new KeyFrame(Duration.millis(doorSpeed), e -> {
            x = rightDoor.getLayoutX();
            separationR = rightDoor.getWidth();

            if(openingRight && separationR > opened-5) {
                rightDoor.setWidth(separationR - 0.5);
                rightDoor.setLayoutX(x + 0.5);
            }
            else if(!openingRight && separationR < opened+10) {
                rightDoor.setWidth(separationR + 0.5);
                rightDoor.setLayoutX(x - 0.5);
            } else {
                if (openingRight) {           //just assuming the doors are closed/and opened at the
                    status = DoorStatus.OPEN; // same time so only update door status in right animation
                } else {
                    status = DoorStatus.CLOSED;
                }
                rightDoorTimeline.pause();
            }
        }));
        rightDoorTimeline.setCycleCount(Animation.INDEFINITE);
        rightDoorTimeline.play();
    }
}
