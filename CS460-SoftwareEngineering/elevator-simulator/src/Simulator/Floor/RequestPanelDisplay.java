package Simulator.Floor;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


public class RequestPanelDisplay extends Pane {

    private boolean upPressed = false;
    private boolean downPressed = false;

    private Circle upRequest;
    private Circle downRequest;
    private Pane fireKey;


    public RequestPanelDisplay() {
        setUp();
    }

    public void press(boolean up) {
        if (up && !upPressed) {
            upPressed = true;
            upRequest.setFill(Paint.valueOf("#ff4d4d"));
        } else if (!up && !downPressed){
            downPressed = true;
            downRequest.setFill(Paint.valueOf("#ff4d4d"));
        }
    }

    public void clearRequest(boolean up) {
        if (up) {
            upRequest.setFill(Paint.valueOf("#ffffff"));
            upPressed = false;
        } else {
            downRequest.setFill(Paint.valueOf("#ffffff"));
            downPressed = false;
        }
    }



    private void setUp(){

        Rectangle buttonPane = new Rectangle();
        buttonPane.setWidth(8);
        buttonPane.setHeight(25);
        buttonPane.setLayoutY(35);
        buttonPane.setFill(Paint.valueOf("#135225"));

        this.upRequest = new Circle(2);
        upRequest.setCenterX(buttonPane.getWidth() / 2);
        upRequest.setCenterY(40);
        upRequest.setFill(Paint.valueOf("#ffffff"));

        this.downRequest = new Circle(2);
        downRequest.setCenterX(buttonPane.getWidth() / 2);
        downRequest.setCenterY(46);
        downRequest.setFill(Paint.valueOf("#ffffff"));

        this.fireKey = new Pane();
        fireKey.setLayoutX(buttonPane.getWidth() / 2 - 1);
        fireKey.setLayoutY(52);
        fireKey.getChildren().addAll();

        Rectangle r = new Rectangle(2, 6);
        r.setFill(Paint.valueOf("#66b3ff"));
        fireKey.getChildren().addAll(r);

        this.getChildren().addAll(buttonPane, upRequest, downRequest, fireKey);
    }


    public void turnKey(boolean enable) {
        KeyTurnAnimation t = new KeyTurnAnimation(fireKey, enable);
        t.start();

    }


    class KeyTurnAnimation extends AnimationTimer {

        private Pane keySlot;
        private boolean enabling;

        private double rotSpeed = 2;


        public KeyTurnAnimation(Pane keyslot, boolean enabling) {
            this.keySlot = keyslot;
            this.enabling = enabling;

        }

        @Override
        public void handle(long l) {
            double rot = keySlot.getRotate();

            if (enabling) {
                keySlot.setRotate(rot + rotSpeed);

                if (rot > 90) {
                    stop();
                    Rectangle r = (Rectangle) keySlot.getChildren().get(0);
                    r.setFill(Paint.valueOf("#ff0000"));
                }
            } else {
                keySlot.setRotate(rot - rotSpeed);

                if (rot < 0) {
                    stop();
                    Rectangle r = (Rectangle) keySlot.getChildren().get(0);
                    r.setFill(Paint.valueOf("#66b3ff"));
                }
            }

        }
    }


}
