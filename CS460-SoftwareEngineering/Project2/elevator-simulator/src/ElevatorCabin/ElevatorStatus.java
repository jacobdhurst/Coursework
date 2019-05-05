package ElevatorCabin;

import ElevatorSystem.Direction;
import ElevatorSystem.Shared.FloorEnum;

public class ElevatorStatus {

    private Direction direction;
    private FloorEnum floor;
    private FloorEnum dest;
    private int cabinId;

    public ElevatorStatus(Direction direction, FloorEnum floor, FloorEnum dest, int cabinId) {
        this.direction = direction;
        this.floor = floor;
        this.dest = dest;
        this.cabinId = cabinId;
    }


    public Direction getDirection() {
        return direction;
    }

    public FloorEnum getFloor() {
        return floor;
    }

    public FloorEnum getDest() {
        return dest;
    }

    public int getCabinId() {
        return  cabinId;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setCurrentFloor(FloorEnum floor) {
        this.floor = floor;
    }

    public void setDestFloor(FloorEnum dest) {
        this.dest = dest;
    }

    public void getCabinId(int id) {
        this.cabinId = id;
    }
}
