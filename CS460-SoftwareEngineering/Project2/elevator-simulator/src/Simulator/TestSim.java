package Simulator;

import ElevatorSystem.Floor.FloorButtons;
import ElevatorSystem.Floor.FloorManager;
import ElevatorSystem.Shared.ElevatorEnum;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

class TestSim extends Stage {

    private Stage primaryStage;

    private FloorManager floorManager= new FloorManager();
    private FloorButtons floorButtons = floorManager.getFloorButtons();

    public TestSim(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setup();
    }

    private void setup() {
        final Stage test = new Stage();
        test.initOwner(primaryStage);
        VBox testVbox = new VBox(20);
        Scene testScene = new Scene(testVbox, 750, 600);

        // Floor display tests
        HBox openCloseTest = doorsTestBox();
        HBox extRequestTest = extRequestTestBox();
        HBox lightTest = lightTestBox();
        HBox floorPanelTest = floorPanelTestBox();
        HBox keyTurnTest = keyTestBox();

        HBox elevatorTest = elevatorTestBox();
        HBox panelTestBox = panelTestBox();

        Font font = new Font(15);

        Text openClose = new Text("Open/Close Floor Doors");
        openClose.setFont(font);
        Text upDown = new Text("Up/Down Floor Request");
        upDown.setFont(font);
        Text arrival = new Text("Floor Arrival Lights");
        arrival.setFont(font);
        Text floorPanel = new Text("Floor Panel Input");
        floorPanel.setFont(font);
        Text floorKey = new Text("Floor Fire Key");
        floorKey.setFont(font);
        Text elevatorControls = new Text("CabinID Controls");
        elevatorControls.setFont(font);
        Text panelControls = new Text("Panel Controls");
        panelControls.setFont(font);

        testVbox.getChildren().addAll(openClose, openCloseTest,
                                      upDown, extRequestTest,
                                      arrival, lightTest,
                floorPanel, floorPanelTest,
                                      floorKey, keyTurnTest,
                                      elevatorControls, elevatorTest,
                                      panelControls, panelTestBox);


        test.setScene(testScene);
        test.show();
    }

    private void testDoors(String floor, String elevatorIndex, boolean open) {
        try{
            Floors f = Floors.valueOf("F"+floor);
            int index = Integer.parseInt(elevatorIndex) - 1;
            if(index >= 0 && index < 4) {
                double speed = -0.5;
                if (open) speed = 0.5;
                f.setDoorSpeed(index, true, speed);
                f.setDoorSpeed(index, false, speed);
            } else {
                System.err.println("Please input a valid elevator: 1-4");
            }
        }catch(NumberFormatException e){
            System.err.println("Please input a valid elevator: 1-4");
        }catch(IllegalArgumentException e){
            System.err.println("Please input a valid floor: 1-10");
        }
    }

    private void testExtRequest(String floor, boolean up) {
        try {
            int floorNum = Integer.valueOf(floor);
            floorButtons.turnOnCallButtonLight(floorNum,up);
            /*Testing rejecting request to ensure reacts fast enough to not lightup button
            floorManager.rejectFloorRequest(floorNum,up);
            System.out.println("Rejected floor "+ floorNum + " in up direction: "+up);
            */
        }catch(IllegalArgumentException e){
            System.err.println("Please input a valid floor: 1-10");
        }
    }

    private void testExtClear(String floor, boolean up) {
        try {
            //Floors f = Floors.valueOf("F" + floor);
            //f.clear(up);
            int floorNum = Integer.valueOf(floor);
            floorButtons.turnOffCallButtonLight(floorNum,up);
            floorButtons.removeFloorRequest(floorNum,up);
        }catch(IllegalArgumentException e){
            System.err.println("Please input a valid floor: 1-10");
        }
    }

    private void testLights(String floor, String elevatorIndex, boolean up) {
        try {
            //int index = Integer.parseInt(elevatorIndex) - 1;
            int floorNum = Integer.valueOf(floor);
            ElevatorEnum elev = ElevatorEnum.valueOf("E" + elevatorIndex);
            floorManager.elevatorArrivalLights(elev,floorNum,up);

        }catch(IllegalArgumentException e){
            System.err.println("Please input a valid floor: 1-10");
        }
    }

    private void testFloorPanel(String floor, String elevatorIndex, String label) {
        try {
            Floors f = Floors.valueOf("F" + floor);
            int index = Integer.parseInt(elevatorIndex) - 1;
            if (index >= 0 && index < 4) {
                f.setFloorPanel(index, label);
            } else {
                System.err.println("Please input a valid elevator: 1-4");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Please input a valid floor: 1-10");
        }
    }

    private void testClearLights(String floor, String elevatorIndex, boolean up) {
        try {

            int floorNum = Integer.valueOf(floor);
            ElevatorEnum elev = ElevatorEnum.valueOf("E" + elevatorIndex);
            floorManager.elevatorArrivalLightsOff(elev,floorNum,up);
            System.out.println("TurnedOff floor" + floor+" , elev "+ elevatorIndex);
           /*Floors f = Floors.valueOf("F" + floor);
            int index = Integer.parseInt(elevatorIndex) - 1;
            if(index >= 0 && index < 4) {
                f.clearArrivalLights(index, up);
            } else {
                System.err.println("Please input a valid elevator: 1-4");
            }*/
        }catch (IllegalArgumentException e){
            System.err.println("Please input a valid floor: 1-10");
        }
    }

    private void testKeyTurn(String floor, boolean enable) {
        try {
            Floors f = Floors.valueOf("F" + floor);
            f.turnFireKey(enable);
        } catch (IllegalArgumentException e) {
            System.err.println("Please input a valid floor: 1-10");
        }
    }

    private void testElevator(String elevatorId, int cmd) {
        try {
            Elevators e = Elevators.valueOf("E" + elevatorId);
            switch (cmd)
            {
                case 0:
                    e.movingUp(1, 9);
                    break;
                case 1:
                    e.movingDown(1, 9);
                    break;
                case 2:
                    e.idle();
                    break;
                case 3:
                    e.leftDoorOpen();
                    e.rightDoorOpen();
                    break;
                case 4:
                    e.leftDoorClosed();
                    e.rightDoorClosed();
                    break;
                default:
                    break;
            }
        }catch(IllegalArgumentException e){
            System.err.println("Please input a valid elevator index: 1-4");
        }
    }

    private HBox doorsTestBox() {
        HBox openCloseTest = new HBox(10);
        TextField floorInput = new TextField();
        TextField elevatorIndex = new TextField();

        floorInput.setPromptText("Floor #");
        elevatorIndex.setPromptText("CabinID #");

        floorInput.setPrefWidth(70);
        elevatorIndex.setPrefWidth(70);
        Button close = new Button("Close");
        Button open = new Button("Open");

        open.setOnMousePressed(e -> testDoors(floorInput.getText(), elevatorIndex.getText(), true));
        close.setOnMousePressed(e -> testDoors(floorInput.getText(), elevatorIndex.getText(), false));
        openCloseTest.getChildren().addAll(floorInput, elevatorIndex, close, open);
        return openCloseTest;
    }

    private HBox floorPanelTestBox() {
        HBox panelTest = new HBox(10);
        TextField floorInput = new TextField();
        TextField elevatorIndex = new TextField();
        TextField value = new TextField();

        floorInput.setPromptText("Floor #");
        elevatorIndex.setPromptText("CabinID #");
        value.setPromptText("Panel value");

        floorInput.setPrefWidth(70);
        elevatorIndex.setPrefWidth(70);
        value.setPrefWidth(100);

        Button enter = new Button("Enter");

        enter.setOnMousePressed(e -> testFloorPanel(floorInput.getText(), elevatorIndex.getText(), value.getText()));
        panelTest.getChildren().addAll(floorInput, elevatorIndex, value, enter);
        return panelTest;
    }

    private HBox extRequestTestBox() {
        HBox extRequestTest = new HBox(10);
        TextField floorInput = new TextField();
        floorInput.setPromptText("Floor #");
        floorInput.setPrefWidth(70);

        Button up = new Button("Request up");
        Button down = new Button("Request down");
        Button clearUp = new Button("Clear up");
        Button clearDown = new Button("Clear down");

        up.setOnMousePressed(e -> testExtRequest(floorInput.getText(), true));
        down.setOnMousePressed(e -> testExtRequest(floorInput.getText(), false));
        clearUp.setOnMousePressed(e -> testExtClear(floorInput.getText(), true));
        clearDown.setOnMousePressed(e -> testExtClear(floorInput.getText(), false));
        extRequestTest.getChildren().addAll(floorInput, up, down, clearUp, clearDown);
        return extRequestTest;
    }

    private HBox lightTestBox() {
        HBox lightTest = new HBox(10);
        TextField floorInput = new TextField();
        TextField elevatorInput = new TextField();
        floorInput.setPromptText("Floor #");
        elevatorInput.setPromptText("CabinID #");
        floorInput.setPrefWidth(70);
        elevatorInput.setPrefWidth(70);

        Button lightUp = new Button("Light up");
        Button lightDown = new Button("Light down");
        Button clearUpLight = new Button("Clear up");
        Button clearDownLight = new Button("Clear down");

        lightUp.setOnMousePressed(e -> testLights(floorInput.getText(), elevatorInput.getText(), true));
        lightDown.setOnMousePressed(e -> testLights(floorInput.getText(), elevatorInput.getText(), false));
        clearUpLight.setOnMousePressed(e -> testClearLights(floorInput.getText(), elevatorInput.getText(), true));
        clearDownLight.setOnMousePressed(e -> testClearLights(floorInput.getText(), elevatorInput.getText(), false));
        lightTest.getChildren().addAll(floorInput, elevatorInput, lightUp, lightDown, clearUpLight, clearDownLight);
        return lightTest;
    }

    private HBox keyTestBox() {
        HBox lightTest = new HBox(10);
        TextField floorInput = new TextField();
        floorInput.setPromptText("Floor #");
        floorInput.setPrefWidth(70);

        Button turnKey = new Button("Turn fire key");
        Button resetKey = new Button("Reset fire key");

        turnKey.setOnMousePressed(e -> testKeyTurn(floorInput.getText(), true));
        resetKey.setOnMousePressed(e -> testKeyTurn(floorInput.getText(), false));

        lightTest.getChildren().addAll(floorInput, turnKey, resetKey);
        return lightTest;
    }

    private HBox elevatorTestBox()
    {
        HBox elevatorTest = new HBox(10);
        TextField elevatorId = new TextField();
        elevatorId.setPromptText("CabinID");
        elevatorId.setPrefWidth(70);

        Button moveUp = new Button("Up");
        Button moveDown = new Button("Down");
        Button idle = new Button("Idle");
        Button openDoors = new Button("Open");
        Button closeDoors = new Button("Close");

        moveUp.setPrefWidth(100);
        moveDown.setPrefWidth(100);
        idle.setPrefWidth(100);
        openDoors.setPrefWidth(100);
        closeDoors.setPrefWidth(100);

        moveUp.setOnMousePressed(e -> testElevator(elevatorId.getText(), 0));
        moveDown.setOnMousePressed(e -> testElevator(elevatorId.getText(), 1));
        idle.setOnMousePressed(e -> testElevator(elevatorId.getText(), 2));
        openDoors.setOnMousePressed(e -> testElevator(elevatorId.getText(), 3));
        closeDoors.setOnMousePressed(e -> testElevator(elevatorId.getText(), 4));

        elevatorTest.getChildren().addAll(elevatorId, moveUp, moveDown, idle, openDoors, closeDoors);
        return elevatorTest;
    }

    private HBox panelTestBox() {
        HBox panelTest = new HBox(2);
        TextField panelId = new TextField();
        TextField floorNum = new TextField();
        TextField currentFloor = new TextField();

        panelId.setPrefWidth(70);
        floorNum.setPrefWidth(70);
        currentFloor.setPrefWidth(70);

        panelId.setPromptText("Panel #");
        floorNum.setPromptText("Floor #");
        currentFloor.setPromptText("Current Floor #");

        Button floorRequest = new Button("Floor On");
        Button floorRequestHandled = new Button("Floor Off");

        Button emergencyButton = new Button("Emergency");
        Button fireKey = new Button("Fire Key");

        Button resetFireKey = new Button("Reset Fire Key");

        Button currentFloorDisplay = new Button("Current Floor");



        currentFloorDisplay.setOnAction(event -> {
            String panelNum = panelId.getCharacters().toString();
            String current = currentFloor.getCharacters().toString();
            try {
                int p = Integer.parseInt(panelNum);
                int c = Integer.parseInt(current);
                if (p >= 1 && p <= 4) {
                    Panels panel = Panels.valueOf("P" + panelNum);
                    panel.setCurrentFloor(c);
                }
            }catch (NumberFormatException e){
                System.err.println("Enter a panel between 1 - 4.\nEnter a floor between 1 - 10");
            }
        });



        floorRequest.setOnAction(event -> {
            try {
                String panelNum = panelId.getCharacters().toString();
                String floorReq = floorNum.getCharacters().toString();
                int panel = Integer.parseInt(panelNum);
                int floor = Integer.parseInt(floorReq);
                if (panel >= 1 && panel <= 4) {
                    requestFloorTestPanel(panel, floor);
                }
            }catch (NumberFormatException e){
                System.err.println("Enter a panel between 1 - 4.\nEnter a floor between 1 - 10");
            }

        });

        emergencyButton.setOnAction(event -> {
            try {
                String panelNum = panelId.getCharacters().toString();
                int panel = Integer.parseInt(panelNum);
                if (panel >= 1 && panel <= 4) {
                    Panels p = Panels.valueOf("P" + panel);
                    p.emergencyRequest();
                }
            }catch (NumberFormatException e){
                System.err.println("Enter a panel between 1 - 4.\nEnter a floor between 1 - 10");
            }
        });

        resetFireKey.setOnAction(event -> {
            String panel = panelId.getCharacters().toString();
            int num = Integer.parseInt(panel);
            if(num >= 1 && num <= 4){
                Panels p = Panels.valueOf("P"+num);
                p.setFireKeyReset();
            }
        });

        fireKey.setOnAction(event -> {
            try {
                String panel = panelId.getCharacters().toString();
                int num = Integer.parseInt(panel);
                if (num >= 1 && num <= 4) {
                    Panels p = Panels.valueOf("P" + num);
                    p.turnFireKey();
                }
            }catch (NumberFormatException e){
                System.err.println("Enter a panel between 1 - 4.\nEnter a floor between 1 - 10");
            }

        });

        floorRequestHandled.setOnAction(event -> {
            try {
                String panel = panelId.getCharacters().toString();
                String floor = floorNum.getCharacters().toString();
                int request = Integer.parseInt(floor);
                int num = Integer.parseInt(panel);
                if (num >= 1 && num <= 4) {
                    Panels p = Panels.valueOf("P" + num);
                    p.requestHandled(request);
                }
            }catch (NumberFormatException e){
                System.err.println("Enter a panel between 1 - 4.\nEnter a floor between 1 - 10");
            }
        });
        panelTest.getChildren().addAll(panelId, floorNum, currentFloor, currentFloorDisplay, floorRequestHandled, floorRequest, fireKey,resetFireKey, emergencyButton);

        return panelTest;

    }


    public void requestFloorTestPanel(int index, int floor){

        Panels p = Panels.valueOf("P"+index);
        p.newRequest(floor);
    }
}
