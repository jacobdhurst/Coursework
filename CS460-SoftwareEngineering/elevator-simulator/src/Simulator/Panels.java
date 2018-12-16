package Simulator;

import Simulator.Panel.PanelDisplay;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.LinkedList;

public enum Panels {

    P1(new boolean[]{false, false, false, false, false, false, false, false, false, false}, false, false),
    P2(new boolean[]{false, false, false, false, false, false, false, false, false, false}, false, false),
    P3(new boolean[]{false, false, false, false, false, false, false, false, false, false}, false, false),
    P4(new boolean[]{false, false, false, false, false, false, false, false, false, false}, false, false);


    private boolean[] buttons;
    private boolean emergency;
    private boolean fireKey;
    private boolean fireKeyReset;
    private int currentFloor;



    Panels(boolean[] buttons, boolean emergency, boolean fireKey ) {
        this.buttons = buttons;
        this.emergency = emergency;
        this.fireKey = fireKey;
        this.currentFloor = 1;


    }

    public void newRequest(int floor) {
        if (floor <= 10 && floor >= 1) {
            this.buttons[floor-1] = true;
//            System.out.println("new request for floor " + floor);
            //this.setFloorRequest(floor);

        } else {
            System.err.println("ERROR: That is not a valid floor.");
        }
    }

    public void requestHandled(int floor){
        if(floor <= 10 && floor >= 1){
            this.buttons[floor-1] = false;
        }
    }

    public void setCurrentFloor(int floor){
        this.currentFloor = floor;

    }

    public void emergencyRequest(){
        //panel.setToEmergency();
        this.emergency = true;

    }

    public void turnFireKey(){
        //panel.keyTurnAnimation();
        this.fireKey = true;

    }
    public void fireKeyTurned(){
        this.fireKey = false;
    }

    public void setFireKeyReset(){
        this.fireKeyReset = true;
    }
    public boolean getFireKeyReset(){
        return fireKeyReset;
    }

    public void fireKeyResetTurned(){
        this.fireKeyReset = false;
    }


    public boolean[] getButtons(){
        return buttons;
    }

    public boolean getEmergency(){
        return emergency;
    }

    public boolean getFireKey(){
        return fireKey;
    }


    public int getCurrentFloor(){
        return currentFloor;
    }

}




