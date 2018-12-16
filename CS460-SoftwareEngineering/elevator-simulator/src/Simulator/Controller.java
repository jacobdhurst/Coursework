package Simulator;

import ElevatorCabin.BuildingControl;
import Simulator.Floor.FloorDisplay;
import Simulator.Panel.PanelDisplay;
import Simulator.Shaft.ShaftDisplay;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.List;

public class Controller extends AnimationTimer {

    private final int FLOOR_HEIGHT = 70;
    private final int SECTION_WIDTH = 150; //FLOORS ARE 200


    //-----------------------------------------------------------//
    // Here are some linked lists that hold VBoxes or HBoxes for //
    // each of your sections. I just guessed what I think would  //
    // work best. Try not to change it, and just add different   //
    // panes inside if you need to do something different. A VBox//
    // with only an HBox in it will be essentially just an HBox, //
    // but it will save us from having to change skeleton.       //
    //-----------------------------------------------------------//

    List<VBox> shafts = new LinkedList<>();
    List<VBox> internalPanels = new LinkedList<>();
    List<HBox> floors = new LinkedList<>();

    public Controller(ScrollPane main){

        GridPane primaryPane = new GridPane();
        Group root = new Group();
        primaryPane.setMinSize(200.0, 100.0);


        //More primary stuff just to get javafx working.
        root.getChildren().add(primaryPane);
        main.setContent(root);

        // Filling the lists made earlier with the correct amount of Nodes.
        for(int i = 0; i < 10; i++) {
            if (i < 4) {
                internalPanels.add(new VBox());
                shafts.add(new VBox());
            }
            floors.add(new HBox());
        }

        //Setting up some initial colors. JUST FOR DEBUGGING and VISUALIZATION.
        BackgroundFill one = new BackgroundFill(Color.LIGHTCYAN, null, null);
        BackgroundFill two = new BackgroundFill(Color.WHEAT, null, null);
        BackgroundFill three = new BackgroundFill(Color.SKYBLUE, null, null);
        BackgroundFill four = new BackgroundFill(Color.GRAY, null, null);


        //-----------------------------------------------------------//
        // Three enhanced for loops to fill in all the info for each //
        // of your areas. These are just setting some initial colors //
        // for you guys to easily see what's going on. IMPORTANT to  //
        // note that the sizes must remain constant. Don't change it //
        // unless we all agree on it. You can fill these with what-  //
        // ever you guys see fit. This is just the set up.           //
        //-----------------------------------------------------------//

        int i = 0, j = 1;
        // Useful for interacting with the shaft. Ex: shaftDisplay.moveElevatorUp(), etc.
        for(VBox shaft : shafts){
            shaft.setPrefWidth(SECTION_WIDTH);
            shaft.setPrefHeight(FLOOR_HEIGHT*10);
            ShaftDisplay shaftDisplay = new ShaftDisplay(shaft, j, 10);
            if(i%2 == 0) shaft.setBackground(new Background(one));
            else shaft.setBackground(new Background(four));
            shaft.getChildren().add(shaftDisplay.getShaft());
            primaryPane.add(shaft, i, 0, 1, 10);
            i++;
            if(i == 2) i++;
            j++;
        }
        i = 0;
        int k = 0;
        for(VBox panel : internalPanels){
            panel.setPrefWidth(SECTION_WIDTH);
            panel.setPrefHeight(FLOOR_HEIGHT*4);
            if(i%2 == 0) panel.setBackground(new Background(four));
            else panel.setBackground(new Background(one));
            PanelDisplay panelDisplay = new PanelDisplay(panel, k + 1);
            primaryPane.add(panel, i, 10, 1, 2);
            k++;
            i++;
            if(i == 2) i++;
        }
        i = 0;
        for(HBox floor : floors){
            floor.setPrefWidth(SECTION_WIDTH+50);
            floor.setPrefHeight(FLOOR_HEIGHT);
            if(i%2 == 0) floor.setBackground(new Background(two));
            else floor.setBackground(new Background(three));
            FloorDisplay floorDisplay = new FloorDisplay(floor, i + 1);
            primaryPane.add(floor, 2, 9-i, 1, 1);
            i++;
        }
        BuildingControl bc = new BuildingControl();
        bc.start();
        bc.run();
    }

    @Override
    public void handle(long l) {

    }
}
