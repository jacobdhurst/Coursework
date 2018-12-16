package ControlSystem.tests;

import ControlSystem.*;
import Primary.*;

/**
 * Used to test timed mode.
 */
public class ResponsiveTest {

  /**
   * Tests timed mode.
   */
  public static void run() {
    LightPatternTable lightPatterns = new LightPatternTable();
    ResponsiveMode timedMode = new ResponsiveMode(lightPatterns);
    CarInput carinput = new CarInput();
    PedestrianInput pedinput = new PedestrianInput();

    while (true) {
      try {
        setLights(timedMode.nextPattern(carinput.getStatus(),pedinput.getStatus()));
        Thread.sleep(3000);
        setLights(timedMode.nextPattern(carinput.getStatus(),pedinput.getStatus()));
        Thread.sleep(3000);
        setLights(timedMode.nextPattern(carinput.getStatus(),pedinput.getStatus()));
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        System.out.println("Caught " + e);
      }
    }
  }

  /**
   * Sets the lights to the given light pattern.
   *
   * @param lightPattern light pattern to set the lights to
   */
  private static void setLights(LightPattern lightPattern) {
        /* Traffic signals */

//    System.out.println("East Turn:"+Lanes.E3.isCarOnLane());
//    System.out.println("West Turn:"+Lanes.W3.isCarOnLane());
//    System.out.println("North Turn:"+Lanes.N3.isCarOnLane());
//    System.out.println("South Turn:"+Lanes.S3.isCarOnLane());
//    System.out.println("North Through:"+Lanes.N1.isCarOnLane()+"  "+Lanes.N2.isCarOnLane());
//    System.out.println("South Through:"+Lanes.S1.isCarOnLane()+"  "+Lanes.S2.isCarOnLane());
//    System.out.println("East Through:"+Lanes.E1.isCarOnLane()+"  "+Lanes.E2.isCarOnLane());
//    System.out.println("West Through:"+Lanes.W1.isCarOnLane()+"  "+Lanes.W2.isCarOnLane());
//    System.out.println("East Pedestrian:"+Lights.EAST.isPedestrianAt());
//    System.out.println("South Pedestrain:"+Lights.SOUTH.isPedestrianAt());
//    System.out.println("North Pedestrain:"+Lights.NORTH.isPedestrianAt());
//    System.out.println("West Pedestrain:"+Lights.WEST.isPedestrianAt());

    Lanes.N1.setColor(lightPattern.getLight(LightName.N1));
    Lanes.N2.setColor(lightPattern.getLight(LightName.N2));
    Lanes.N3.setColor(lightPattern.getLight(LightName.N3));
    Lanes.E1.setColor(lightPattern.getLight(LightName.E1));
    Lanes.E2.setColor(lightPattern.getLight(LightName.E2));
    Lanes.E3.setColor(lightPattern.getLight(LightName.E3));
    Lanes.S1.setColor(lightPattern.getLight(LightName.S1));
    Lanes.S2.setColor(lightPattern.getLight(LightName.S2));
    Lanes.S3.setColor(lightPattern.getLight(LightName.S3));
    Lanes.W1.setColor(lightPattern.getLight(LightName.W1));
    Lanes.W2.setColor(lightPattern.getLight(LightName.W2));
    Lanes.W3.setColor(lightPattern.getLight(LightName.W3));

        /* Pedestrian signals */
    Lights.NORTH.setColor(lightPattern.getLight(LightName.CN));
    Lights.EAST.setColor(lightPattern.getLight(LightName.CE));
    Lights.SOUTH.setColor(lightPattern.getLight(LightName.CS));
    Lights.WEST.setColor(lightPattern.getLight(LightName.CW));

//    Lights.NORTH.setColor(SignalColor.GREEN);
//    Lights.EAST.setColor(SignalColor.GREEN);
//    Lights.SOUTH.setColor(lightPattern.getLight(LightName.CS));
//    Lights.WEST.setColor(lightPattern.getLight(LightName.CW));
  }
}