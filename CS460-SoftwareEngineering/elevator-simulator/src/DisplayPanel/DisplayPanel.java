package DisplayPanel;

import ElevatorCabin.CabinID;
import ElevatorSystem.Direction;
import Simulator.Elevators;

public class DisplayPanel {

    public void setDisplayPanel(){
        updateElevators();
    }

    public void updateElevators(){
        for(CabinID cabin : CabinID.values()){
            Direction cabinDirection = cabin.getDirection();
            if(cabinDirection.equals(Direction.DOWN)){
                Elevators.valueOf("E"+cabin.getCabinID()).movingDown(cabin.getSpeed(), cabin.getDestination());
            }else if(cabinDirection.equals(Direction.UP)){
                Elevators.valueOf("E"+cabin.getCabinID()).movingUp(cabin.getSpeed(), cabin.getDestination());
            }else{
                Elevators.valueOf("E"+cabin.getCabinID()).idle();
            }
        }
    }
}
