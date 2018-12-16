package ElevatorCabin;

import DestinationScheduler.DestinationScheduler;
import DisplayPanel.DisplayPanel;
import ElevatorSystem.Cabin.CabinManager;
import ElevatorSystem.Cabin.CabinRequest;
import ElevatorSystem.Floor.FloorManager;
import ElevatorSystem.Floor.FloorRequest;
import ElevatorSystem.Mode.Mode;
import ElevatorSystem.Mode.OperatingMode;
import ElevatorSystem.Shared.ElevatorEnum;
import Simulator.Floors;
import javafx.animation.AnimationTimer;

import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class BuildingControl extends AnimationTimer {
    static LinkedList<Elevator> cabins = new LinkedList<Elevator>();

    private ScheduledExecutorService updater;
    private ScheduledFuture<?> updateInterval;

    private LinkedList<FloorRequest> floorRequests = new LinkedList<>();

    private DisplayPanel dp;
    private DestinationScheduler destinationScheduler;
    private CabinManager cabinManager;
    private FloorManager floorManager;
    private OperatingMode operatingMode;
    private FloorRequest floorRequest;


    public BuildingControl() {
        dp = new DisplayPanel();
        cabinManager = new CabinManager(false);
        floorManager = new FloorManager();
        destinationScheduler = new DestinationScheduler();
        operatingMode = new OperatingMode();

        cabins.add(new Elevator(CabinID.E1, cabinManager, ElevatorEnum.E1, floorManager));
        cabins.add(new Elevator(CabinID.E2, cabinManager, ElevatorEnum.E2, floorManager));
        cabins.add(new Elevator(CabinID.E3, cabinManager, ElevatorEnum.E3, floorManager));
        cabins.add(new Elevator(CabinID.E4, cabinManager, ElevatorEnum.E4, floorManager));
    }

    public void run() {
        updater = Executors.newScheduledThreadPool(1);
        updateInterval = updater.scheduleAtFixedRate(() -> updateBuilding(), 100, 500, TimeUnit.MILLISECONDS);

    }

    public void updateBuilding() {
        floorRequest = floorManager.getFloorRequest();
        if (floorRequest != null) {
            floorRequests.add(floorRequest);
            Floors floors = Floors.valueOf("F" + (floorRequest.getFloor().getIdx() + 1));
            if (floorRequest.getDirection()) {
                floors.requestUp();
            } else {
                floors.requestDown();
            }
        }

        Mode[] modes = operatingMode.getModes();

        for (ElevatorEnum eID : ElevatorEnum.values()) {
            CabinRequest cabinRequest = cabinManager.getCabinRequest(eID);
            Elevator cabin = cabins.get(eID.getIdx());
            if (cabinRequest != null) {
                cabin.addRequest(cabinRequest);
                if (cabin.getStatus().getDest() == null) {
                    cabin.makeStopAt(destinationScheduler.getNextDestination(floorRequests, cabin.getRequests(), cabin.getStatus()));
                    break;
                } else {
                    cabin.updateCabin();
                }
            }
        }

    }

    @Override
    public void handle(long l) {
        dp.updateElevators();
    }
}
