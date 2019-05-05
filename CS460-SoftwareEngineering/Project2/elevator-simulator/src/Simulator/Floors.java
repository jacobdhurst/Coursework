package Simulator;

import Simulator.Floor.FloorDisplay;

import java.util.LinkedList;

public enum Floors {

    F1(false, false, false),
    F2(false, false, false),
    F3(false, false, false),
    F4(false, false, false),
    F5(false, false, false),
    F6(false, false, false),
    F7(false, false, false),
    F8(false, false, false),
    F9(false, false, false),
    F10(false, false, false);

    private boolean upRequest, downRequest, sentRequest;
    private boolean fireKeyTurned = false;

    private FloorDisplay floor;

    Floors(boolean upRequest, boolean downRequest, boolean sent){
        this.upRequest = upRequest;
        this.downRequest = downRequest;
        this.sentRequest = sent;
    }

    public void requestUp(){
        this.upRequest = true;
        this.sentRequest = false;
        floor.makeExternalRequest(true);
    }

    public void requestDown() {
        this.downRequest = true;
        this.sentRequest = false;
        floor.makeExternalRequest(false);
    }

    public boolean[] requestStatus(){
        boolean status[] = new boolean[3];
        status[0] = upRequest;
        status[1] = downRequest;
        status[2] = sentRequest;
        return status;
    }

    public void requestSent(){
        this.sentRequest = false;
    }

    public void clear(boolean up) {
        if(up){
            this.upRequest = false;
        }
        else this.downRequest = false;
        this.sentRequest = false;
        floor.clearRequest(up);
    }

    public void setDoorSpeed(int index, boolean left, double speed) {
        floor.setDoor(index, left, speed);
    }

    public void setFloorPanel(int index, String label) {
        floor.setDoorPanel(index, label);
    }

    // 0 = closed, 1 = open, 2 = closing, 3 = opening
    public void getFloorDoorStatus(int index) {
        floor.getDoorStatus(index);
    }

    public boolean getFireKeyStatus(){
        return this.fireKeyTurned;
    }

    public void setArrivalLights(int index, boolean up) {
        floor.setArrivalLight(index, up);
    }

    public void clearArrivalLights(int index, boolean up) {
        floor.clearArrivalLight(index, up);
    }

    public void setFloor(FloorDisplay floor) {
        this.floor = floor;
    }

    public void turnFireKey(boolean enabled) {
        floor.turnKey(enabled);
        fireKeyTurned = enabled;
    }

}
