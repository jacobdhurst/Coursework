package Simulator.Floor;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;


public class FloorDoorDisplay extends Pane {

    private Rectangle door1;
    private Rectangle door2;
    private Polygon upArrival;
    private Polygon downArrival;
    private Label floorLabel;

    private double offset = 24;
    private int status = 0; // 0 = closed, 1 = open, 2 = closing, 3 = opening

    private RightDoorAnimation rDoorAnimation;
    private LeftDoorAnimation lDoorAnimation;

    public FloorDoorDisplay(int index) {
        setUp();
    }

    public int getStatus() {
        return status;
    }

    public void setFloorLabel(String label) {
        floorLabel.setText(label);
    }

    public void setDoorSpeed(double speed, boolean left) {
        if (left) {
            if (speed == 0) {
                // stop animation if there is one
                if (lDoorAnimation != null) lDoorAnimation.stop();
            } else {
                lDoorAnimation = new LeftDoorAnimation(door1, speed);
                lDoorAnimation.start();
            }
        } else {
            if (speed == 0) {
                // stop animation if there is one
                if (rDoorAnimation != null) rDoorAnimation.stop();
            } else {
                rDoorAnimation = new RightDoorAnimation(door2, speed);
                rDoorAnimation.start();
            }
        }
    }


    public void arrive(boolean up) {
        if (up) {
            upArrival.setFill(Paint.valueOf("#1a75ff"));
        } else {
            downArrival.setFill(Paint.valueOf("#1a75ff"));
        }
    }

    public void clearLight(boolean up) {
        if (up) {
            upArrival.setFill(Paint.valueOf("#66ff33"));
        } else {
            downArrival.setFill(Paint.valueOf("#66ff33"));
        }
    }

    private void setUp() {

        Rectangle floorPanel = new Rectangle();
        floorPanel.setFill(Paint.valueOf("#ffffff"));
        this.floorLabel = new Label("0");
        floorLabel.setStyle("-fx-text-fill: black; -fx-font: 5px Calibri-Bold;");

        Rectangle frame = new Rectangle();
        Rectangle back = new Rectangle();
        this.door1 = new Rectangle();
        this.door2 = new Rectangle();

        this.upArrival = new Polygon();
        this.downArrival = new Polygon();
        upArrival.getPoints().addAll(new Double[]{0.0, 0.0, -4.0, 4.0, 4.0, 4.0});
        downArrival.getPoints().addAll(new Double[]{0.0, 0.0, 4.0, -4.0, -4.0, -4.0});

        upArrival.setFill(Paint.valueOf("#66ff33"));
        downArrival.setFill(Paint.valueOf("#66ff33"));

        door1.setHeight(30);
        door1.setWidth(15);
        door2.setHeight(30);
        door2.setWidth(15);
        frame.setWidth(30);
        frame.setHeight(46);
        back.setWidth(30);
        back.setHeight(30);

        frame.setFill(Paint.valueOf("#9494b8"));
        back.setFill(Color.BLACK);
        door1.setFill(Paint.valueOf("#9494b8"));
        door2.setFill(Paint.valueOf("#9494b8"));

        door1.setLayoutX(0);
        door2.setLayoutX(15);
        door1.setLayoutY(16 + offset);
        door2.setLayoutY(16 + offset);

        upArrival.setLayoutY(offset + 2);
        upArrival.setLayoutX(10);
        downArrival.setLayoutY(offset + 6);
        downArrival.setLayoutX(20);

        floorPanel.setWidth(14);
        floorPanel.setHeight(5);
        floorPanel.setLayoutY(offset + 9);
        floorPanel.setLayoutX(frame.getWidth() / 2 - 7);
        floorLabel.setLayoutX(frame.getWidth() / 2 - 2);
        floorLabel.setLayoutY(offset + 8.5);

        back.setLayoutY(16 + offset);
        frame.setLayoutY(offset);

        this.getChildren().addAll(frame, back, door1, door2, upArrival, downArrival, floorPanel, floorLabel);
    }


    class LeftDoorAnimation extends AnimationTimer {

        private Rectangle door;
        private boolean blocked = false;
        private double speed;

        public LeftDoorAnimation(Rectangle door, double speed) {
            this.door = door;
            this.speed = speed;
        }

        @Override
        public void handle(long l) {

            double width = door.getWidth();

            if (speed > 0 && width > 4) {
                door.setWidth(width - speed);
            } else if (speed < 0 && width < 15) {
                door.setWidth(width - speed);
            } else {
                stop();
            }

        }
    }

    class RightDoorAnimation extends AnimationTimer {

        private Rectangle door;
        private boolean blocked = false;
        private double speed;

        public RightDoorAnimation(Rectangle door, double speed) {
            if (speed > 0) {
                status = 3;
            } else {
                status = 2;
            }
            this.door = door;
            this.speed = speed;
        }

        @Override
        public void handle(long l) {

            double width = door.getWidth();
            double x = door.getLayoutX();


            if (speed > 0 && width > 4) {
                door.setWidth(width - speed);
                door.setLayoutX(x + speed);
            } else if (speed < 0 && width < 15) {
                door.setWidth(width - speed);
                door.setLayoutX(x + speed);
            } else {
                if (speed > 0) {
                    status = 1;
                } else {
                    status = 0;
                }
                stop();
            }

        }
    }
}
