package Simulator.Panel;

import Simulator.Panels;
import javafx.animation.AnimationTimer;
import javafx.animation.RotateTransition;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.awt.*;
import java.awt.Polygon;
import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.Stack;

public class PanelDisplay {

    private VBox root;
    //private Pane root;
    private final int FLOORS = 10;
    private LinkedList<Circle> circleArray = new LinkedList<>();
    private  LinkedList<Integer> requestList = new LinkedList<>();
    private LinkedList<StackPane> animators = new LinkedList<>();
    private Panels myPanel;
    private int num;
    private String name;
    private int currentFloor = -1;
    private Text displayText = new Text();

    private StackPane displayPane;


    public PanelDisplay(VBox panel, int myId){
        this.root = panel;
        this.num = myId;

        name = "P" + myId;
        this.myPanel = Panels.valueOf(name);



        init();
        initEmergency();
        initKeySlot();
        displayWindow();
        watcher();


    }

    public void watcher(){
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                buttonRequestListener();
                emergencyListener();
                keyTurnListener();
                floorListener();


            }
        };
        timer.start();
    }


    public void init(){
        Circle button;
        Circle outerButton;
        Text text;
        StackPane myPane;

        int index;
        for(int i = 1 ; i <= FLOORS; i++) {

            myPane = new StackPane();
            index = (FLOORS - i) + 1;
            outerButton = new Circle(15);
            button = new Circle(10);
            text = new Text(index + "");

            button.setFill(Color.GRAY);
            outerButton.setFill(Color.DARKGREY);

            myPane.setCenterShape(true);
            myPane.getChildren().addAll(outerButton, button, text);
            myPane.setId(i + "");
            outerButton.setId(index + "");
            circleArray.add( outerButton);



            myPane.setMargin(outerButton, new Insets(3, 3, 3, 0));
            myPane.setMargin(button, new Insets(3, 8, 3, 0));
            myPane.setMargin(text, new Insets(3, 13, 3, 0));

            myPane.setAlignment(Pos.CENTER_RIGHT);


            root.getChildren().add(myPane);

        }

    }

    public void initEmergency(){

        StackPane temp = (StackPane)root.getChildren().get(FLOORS - 1);
        StackPane emerg = new StackPane();
        Circle inner = new Circle(15);

        Circle outer = new Circle(20);

        Text txt = new Text("Emerg");

        txt.setFont(Font.font(10));

        outer.setFill(Color.DARKGREY);
        inner.setFill(Color.GRAY);


        emerg.setCenterShape(true);

        emerg.setMargin(outer, new Insets(0, 0, 5, 0));
        emerg.setMargin(inner, new Insets(0, 0, 10,0 ));
        emerg.setMargin(txt, new Insets(0, 0 ,18, 0));

        outer.setId("Emerg");
        circleArray.add(outer);
        emerg.getChildren().addAll(outer, inner, txt);
        emerg.setAlignment(Pos.BOTTOM_CENTER);
        temp.getChildren().add(emerg);


    }





    public void initKeySlot(){
        StackPane temp = (StackPane)root.getChildren().get(4);
        StackPane myKey = new StackPane();

        Circle key = new Circle(20);

        Rectangle innerRect = new Rectangle();
        Rectangle outerRect = new Rectangle();

        innerRect.setHeight(15);
        innerRect.setWidth(2);

        outerRect.setHeight(20);
        outerRect.setWidth(10);

        innerRect.setFill(Color.WHITE);
        key.setFill(Color.DARKGREY);
        outerRect.setFill(Color.BLACK);

        myKey.setCenterShape(true);
        myKey.getChildren().addAll(key, outerRect, innerRect);

        myKey.setMargin(outerRect, new Insets(5));
        myKey.setMargin(innerRect, new Insets(5));
        myKey.setId("Key Slot");
        temp.getChildren().add(myKey);
        animators.add(myKey);

        //root.getChildren().addAll(key, outerRect, innerRect);


    }

    public void buttonRequestListener(){
        boolean[] temp = myPanel.getButtons();
        Circle c;
        for(int i = 0; i < temp.length; i++){

            if(temp[i]){
                c = circleArray.get(FLOORS - i - 1);
                c.setFill(Color.RED);
            }
            else{
                c = circleArray.get(FLOORS - i - 1);
                c.setFill(Color.DARKGREY);
            }
        }
    }

    public void emergencyListener(){
        boolean emerg = myPanel.getEmergency();
        if(emerg){
            for(Circle c : circleArray){
                if(c.getId().equals("Emerg")){
                    c.setFill(Color.RED);
                }
            }
        }
    }

    public void keyTurnListener(){

        boolean keyTurn = myPanel.getFireKey();
        boolean resetKey = myPanel.getFireKeyReset();
        if (keyTurn) {
            keyTurnAnimation(false);
            myPanel.fireKeyTurned();
        }
        else if(resetKey){
            keyTurnAnimation(true);
            myPanel.fireKeyResetTurned();

        }


    }

    public void floorListener(){
        if(currentFloor != myPanel.getCurrentFloor()) {


            currentFloor = myPanel.getCurrentFloor();
            displayCurrentFloor(currentFloor);
        }
    }





    public void displayWindow(){


        StackPane temp = (StackPane)root.getChildren().get(1);

        Rectangle display = new Rectangle();
        Rectangle outerDispay = new Rectangle();
        //StackPane myPane = new StackPane();
        displayPane = new StackPane();


        display.setFill(Color.WHITE);
        outerDispay.setFill(Color.BLACK);

        outerDispay.setWidth(60);
        outerDispay.setHeight(40);

        display.setWidth(50);
        display.setHeight(30);

        displayPane.setMargin(display, new Insets(5));
        displayPane.setCenterShape(true);

        displayPane.setAlignment(Pos.TOP_LEFT);

        displayPane.getChildren().addAll(outerDispay, display);

        animators.add(displayPane);

        temp.getChildren().add(displayPane);

    }

    public void displayCurrentFloor(int currentFloor) {

        displayPane.getChildren().remove(displayText);
        displayText.setText(currentFloor + "");
        displayText.setFont(Font.font(20));
        displayText.setId(currentFloor+"");


        displayPane.setMargin(displayText, new Insets(5, 15, 0, 25));
        displayPane.getChildren().add(displayText);



    }

    public void keyTurnAnimation(boolean isReset){

        StackPane p = animators.getFirst();


        Rectangle r1 = (Rectangle)p.getChildren().get(1);
        Rectangle r2 = (Rectangle)p.getChildren().get(2);




        RotateTransition animation1 = new RotateTransition(Duration.millis(3000), r1);
        RotateTransition animation2 = new RotateTransition(Duration.millis(3000), r2);

        if(!isReset) {

            animation1.setByAngle(90);
            animation2.setByAngle(90);
            animation1.play();
            animation2.play();
            r1.setFill(Color.RED);
        }
        else{
            animation1.setByAngle(-90);
            animation2.setByAngle(-90);
            animation1.play();
            animation2.play();
            r1.setFill(Color.BLACK);
        }



    }

    public void resetKeyTurnAnimation(){

    }


}
