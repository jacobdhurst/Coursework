package Simulator.Shaft;

import ElevatorCabin.DoorStatus;
import Simulator.Elevators;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.util.LinkedList;
import java.util.List;

/**
 * Visual representation of the shaft, includes sensors & elevatorDisplay.
 */
public class ShaftDisplay {
    private double shaftWidth, shaftHeight, floorHeight;
    private int floors;

    private Elevators elevator;

    private Pane layers;
    private VBox shaft;
    private Canvas canvas;
    private GraphicsContext gc;

    private ElevatorDisplay elevatorDisplay;
    private List<SensorDisplay> sensors;

    /**
     * Default constructor.
     * @param shaft - Unit containing the shaft.
     * @param floors - Number of floors in shaft.
     */
    public ShaftDisplay(VBox shaft, int index, int floors)
    {
        this.shaft = shaft;
        this.shaftWidth = shaft.getPrefWidth();
        this.shaftHeight = shaft.getPrefHeight();
        this.floorHeight = shaftHeight/floors;
        this.floors = floors;
        this.elevator = Elevators.valueOf("E" + index);
        elevator.setShaft(this);

        layers = new Pane();

        initialize();

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                refresh();
            }
        };
        animationTimer.start();
    }

    /**
     * @return layers - A pane with elevatorDisplay shape over canvas representation of shaft, lines to represent sensors.
     */
    public Pane getShaft()
    {
        return layers;
    }

    /**
     * This method wraps interactions with the sensors contained in the shaft.
     * @return active[] - An array containing the indices of thmay note (at most) two sensors that have been triggered.
     */
    public int[] getActiveSensors()
    {
        int[] active = {-1, -1};
        int j = 0;
        for(int i = 0; i < sensors.size(); i++)
        {
            if(sensors.get(i).getStatus())
            {
                active[j] = (10 - i);
                j++;
            }
        }
        return active;
    }

    /**
     * This method wraps interactions with the elevatorDisplay contained in the shaft.
     * @return elevatorDisplay.getLocation() - The top Y coordinate of the elevatorDisplay.
     */
    public double getElevatorLocation()
    {
        return elevatorDisplay.getLocation();
    }

    /**
     * This method wraps interactions with the elevatorDisplay contained in the shaft.
     * Moves the elevatorDisplay upwards.
     */
    public void moveElevatorUp(double speed, double destination)
    {
        elevatorDisplay.moveUp(speed, destination);
    }

    /**
     * This method wraps interactions with the elevatorDisplay contained in the shaft.
     * Moves the elevatorDisplay downwards.
     */
    public void moveElevatorDown(double speed, double destination)
    {
        elevatorDisplay.moveDown(speed, destination);
    }

    /**
     * This method wraps interactions with the elevatorDisplay contained in the shaft.
     * Idles/stops the elevatorDisplay.
     */
    public void idleElevator()
    {
        elevatorDisplay.idle();
    }

    /**
     * This method wraps interactions with the elevatorDisplay contained in the shaft.
     * Opens the elevatorDisplay doors.
     */
    public void openElevatorLeftDoor()
    {
        elevatorDisplay.openLeftDoor();
    }

    /**
     * This method wraps interactions with the elevatorDisplay contained in the shaft.
     * Closes the elevatorDisplay doors.
     */
    public void closeElevatorLeftDoor()
    {
        elevatorDisplay.closeLeftDoor();
    }

    /**
     * This method wraps interactions with the elevatorDisplay contained in the shaft.
     * Opens the elevatorDisplay doors.
     */
    public void openElevatorRightDoor()
    {
        elevatorDisplay.openRightDoor();
    }

    /**
     * This method wraps interactions with the elevatorDisplay contained in the shaft.
     * Closes the elevatorDisplay doors.
     */
    public void closeElevatorRightDoor()
    {
        elevatorDisplay.closeRightDoor();
    }

    /**
     * This method asks the elevator display for the status of the doors
     */
    public DoorStatus getElevatorDoorStatus() {
        return elevatorDisplay.getDoorStatus();
    }


    /**
     * Initializes canvas and sets up the display.
     */
    private void initialize()
    {
        canvas = new Canvas(shaftWidth, shaftHeight);
        gc = canvas.getGraphicsContext2D();

        //Filling the shaft.
        gc.setFill(Color.valueOf("#303030"));
        gc.fillRect(0, 0, shaftWidth, shaftHeight);

        //Line to represent cable.
        gc.setFill(Color.valueOf("#F0F0F0"));
        gc.fillRect(((shaftWidth/2) - 2.5), 0, 5, shaftHeight);

        //Linked list of sensor displays
        sensors = new LinkedList<>();
        for(int i = 0; i < floors; i++)
        {
            sensors.add(new SensorDisplay(gc, shaftWidth, shaftHeight, i));
        }

        //Single elevatorDisplay display
        elevatorDisplay = new ElevatorDisplay(shaftWidth, shaftHeight);

        //Layering shaft with elevatorDisplay for smooth translation of elevatorDisplay.
        layers.getChildren().addAll(canvas, elevatorDisplay.getElevator(), elevatorDisplay.getDoors()[0],
                                    elevatorDisplay.getDoors()[1], elevatorDisplay.getDoors()[2]);
    }

    /**
     * Refreshes display of the shaft, sensors, and elevatorDisplay.
     */
    private void refresh()
    {
        double eLocation, sLocation;
        boolean topStatus, bottomStatus;

        for(SensorDisplay sensor : sensors)
        {
            eLocation = elevatorDisplay.getLocation();
            sLocation = sensor.getLocation();
            topStatus = (eLocation >= (sLocation-10) && eLocation <= (sLocation+10));
            bottomStatus = (eLocation+floorHeight >= (sLocation-10) && eLocation+floorHeight <= (sLocation+10));

            sensor.setStatus(topStatus || bottomStatus);
        }

        //System.out.println(getActiveSensors()[0] + " " + getActiveSensors()[1]);
    }
}
