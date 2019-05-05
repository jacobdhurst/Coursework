package Simulator.Floor;

import Simulator.Floors;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.LinkedList;


public class FloorDisplay {

    private LinkedList<FloorDoorDisplay> doors = new LinkedList<>();
    private RequestPanelDisplay requestPanel;
    private Floors floor;

    public FloorDisplay(HBox box, int num) {


        // Test panel for each floor
        Pane doors1 = addDoor(0);
        Pane doors2 = addDoor(1);
        Pane doors3 = addDoor(2);
        Pane doors4 = addDoor(3);
        Pane requestPanel = createReqButtons();

        String name = "F" + num;
        this.floor = Floors.valueOf(name);
        floor.setFloor(this);
        box.setPadding(new Insets(0, 0, 0, 15));

        box.getChildren().addAll(doors1, doors2, requestPanel, doors3, doors4);

    }

    public int getDoorStatus(int index) {
        int result = -1;
        if (index >= 0 && index < 4) {
            result = doors.get(index).getStatus();
        } else {
            System.out.println("door index must be from 1 -> 4");
        }
        return result;
    }

    private Pane addDoor(int index) {

        FloorDoorDisplay doors = new FloorDoorDisplay(index);
        doors.setPadding(new Insets(0, 0, 0, 10));
        this.doors.add(doors);
        return doors;
    }

    private Pane createReqButtons() {
        this.requestPanel = new RequestPanelDisplay();
        requestPanel.setPadding(new Insets(0, 0, 0, 10));
        return requestPanel;
    }

    public void setDoorPanel(int index, String label) {
        if (index >= 0 && index < 4) {
            doors.get(index).setFloorLabel(label);
        }
    }

    public void setDoor(int index, boolean left, double speed) {
        doors.get(index).setDoorSpeed(speed, left);
    }

    public void clearRequest(boolean up) {
        requestPanel.clearRequest(up);
    }

    public void makeExternalRequest(boolean up) {
        requestPanel.press(up);
    }

    public void setArrivalLight(int index, boolean up) {
        doors.get(index).arrive(up);
    }

    public void clearArrivalLight(int index, boolean up) {
        doors.get(index).clearLight(up);
    }

    public void turnKey(boolean enable) {
        requestPanel.turnKey(enable);
    }


}
