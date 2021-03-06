package ControlSystem.tests;
import ControlSystem.LightName;
import ControlSystem.LightPattern;
import ControlSystem.LightPatternTable;
import ControlSystem.EmergencyMode;
import Primary.*;

import java.util.ArrayList;

public class EmergencyModeTest {
    /**
     * Tests timed mode.
     */
    public static void run() {
        LightPatternTable lightPatterns = new LightPatternTable();
        EmergencyMode eMode = new EmergencyMode(lightPatterns);
        int m = 5;
        LightPattern updated = eMode.nextPattern(m, 6);
        ArrayList<SignalColor> lights;
        setLights(updated);
        lights = updated.getAllLights();
        System.out.println(lights.toString());

        while (true) {
            try {
                Thread.sleep(2000);
                updated = eMode.nextPattern(m);
                setLights(updated);
                lights = updated.getAllLights();
                System.out.println(lights.toString());
                Thread.sleep(2000);
                updated = eMode.nextPattern(m);
                setLights(updated);
                lights = updated.getAllLights();
                System.out.println(lights.toString());
                Thread.sleep(2000);
                updated = eMode.nextPattern(m);
                setLights(updated);
                lights = updated.getAllLights();
                System.out.println(lights.toString());
                Thread.sleep(2000);
                updated = eMode.nextPattern(m);
                setLights(updated);
                lights = updated.getAllLights();
                System.out.println(lights.toString());
                Thread.sleep(2000);
                updated = eMode.nextPattern(m);
                setLights(updated);
                lights = updated.getAllLights();
                System.out.println(lights.toString());
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("FAIL EMODETEST " + e);
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
    }
}
