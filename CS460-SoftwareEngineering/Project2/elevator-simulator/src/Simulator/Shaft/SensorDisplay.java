package Simulator.Shaft;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Visual representation of a single sensor within the shaft.
 */
class SensorDisplay {
    private double shaftWidth, shaftHeight, location;

    private GraphicsContext gc;
    private Color status;

    /**
     * Default constructor.
     * @param gc - The graphics context.
     * @param shaftWidth - Width of the shaft.
     * @param shaftHeight - Height of the shaft.
     * @param floor - Which floor this sensor is associated with.
     */
    SensorDisplay(GraphicsContext gc, double shaftWidth, double shaftHeight, int floor)
    {
        this.gc = gc;
        this.shaftWidth = shaftWidth;
        this.shaftHeight = shaftHeight;

        location = (shaftHeight/10)*floor;
        status = Color.RED;

        refresh();
    }

    /**
     * @return location - The Y coordinate of the sensor.
     */
    double getLocation()
    {
        return location;
    }

    /**
     * @return A boolean regarding whether the sensor has been triggered.
     */
    boolean getStatus()
    {
        return status.equals(Color.GREEN);
    }

    /**
     * Updates the status of the sensor, red => not triggered, green => triggered.
     * @param active - Whether the sensor has been triggered by elevator.
     */
    void setStatus(boolean active)
    {
        if(active) status = Color.GREEN;
        else status = Color.RED;
        refresh();
    }

    /**
     * Refreshes the sensor line by redrawing it.
     */
    private void refresh()
    {
        gc.setLineWidth(2.0);
        gc.setStroke(status);
        gc.strokeLine(0, location, shaftWidth, location);
    }
}
