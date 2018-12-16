package ElevatorSystem.Floor;

import java.util.Random;

public class FloorFireKey
{
    private boolean floorKeyUsed[];
    private Random rnd = new Random();

    public FloorFireKey(){
        this.floorKeyUsed = new boolean[10];
    }

    /*Set the floor fire key to true for the given floor*/
    public void floorKeyEngaged(int floor){
        this.floorKeyUsed[floor] = true;
    }

    /*Set the floor fire key to false for the given floor*/
    public void floorKeyDisengaged(int floor){
        this.floorKeyUsed[floor] = false;
    }

    /*Sends the status of the fire key used at a floor*/
    public boolean getFloorKeyStatus(int floor){
        this.floorKeyUsed[floor] = rnd.nextBoolean();
        return this.floorKeyUsed[floor];
    }

    public boolean anyFloorKeysEngaged(){
        int i;
        for(i = 0; i < 10 ; i++){
            if(floorKeyUsed[i]) return true;
        }
        return false;
    }

}

