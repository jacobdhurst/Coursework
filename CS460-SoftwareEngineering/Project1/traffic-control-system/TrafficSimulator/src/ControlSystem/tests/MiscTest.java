package ControlSystem.tests;

// NOTE:
// placeholder file for T05 testing only, remove once we have a functioning
// proper mode controller

import ControlSystem.*;
import Primary.SignalColor;
import java.util.ArrayList;
import java.util.Arrays;
import static ControlSystem.Utility.pdm;

public class MiscTest {

    // private instance fields
    private Mode modeInst;
    private Scheduler schedInst;
    private LightPattern nextPattern;

    public MiscTest() {

        runSchedulerTests();
        //runLightPatternTests();

        System.exit(0);
    }

    private void runSchedulerTests() {

        modeInst = new Mode();
        schedInst = new Scheduler();
        ModeType newMode;
        // switchInst = new LightSwitcher();

        // start in timed mode, 3 cycles
        callScheduler(ModeType.TIMED,3,500);

        // change mode to Responsive, 20 cycles
        callScheduler(ModeType.RESPONSIVE,20,500);

        // change mode to North emergency, 20 cycles
        callScheduler(ModeType.NORTH_EMERGENCY,20,500);

        //back to timed
        callScheduler(ModeType.TIMED,3,500);
    }


    /**
     * looper for scheduler tests
     * @param newMode - the mode to use
     * @param countmax - max number of loops
     * @param cycletime - time to wait between calls (ms)
     */
    private void callScheduler(
            ModeType newMode, int countmax, int cycletime){
        int counter = 0;
        while (counter < countmax) {
            try {
                // get the mode and get the next pattern
                nextPattern = schedInst.getNextPattern(newMode.getNum());
                // give it to the LightSwitcher
                // switchInst(nextPattern);
                pdm(newMode.toString() + " (" + nextPattern.getIdx() + ": " +
                                     nextPattern.getAllLights().toString() +
                                     " )");
                Thread.sleep(cycletime);
                counter++;
            } catch (InterruptedException e) {
                pdm("Caught " + e);
            }
        }
    }

    // LightPattern tests
    private void runLightPatternTests() {

        // create a new lightpattern
        LightPattern lp1 = new LightPattern(1);

        // print out what the settings of the light pattern are
        pdm(lp1.getAllLights().toString());
        pdm(lp1.getCounts());
        pdm(lp1.getMix().toString());

        // set N1, cn to green
        lp1.setLight(LightName.N1, SignalColor.GREEN);
        lp1.setLight(LightName.CN, SignalColor.GREEN);
        // print out what the settings of the light pattern are
        pdm(lp1.getAllLights().toString());
        pdm(lp1.getCounts());
        pdm(lp1.getMix().toString());

        // set E1 to yellow
        lp1.setLight(LightName.E1, SignalColor.YELLOW);
        // print out what the settings of the light pattern are
        pdm(lp1.getAllLights().toString());
        pdm(lp1.getCounts());
        pdm(lp1.getMix().toString());

        // set all lights at once
        lp1.setAllLights(new ArrayList<SignalColor>(Arrays.asList
                (SignalColor.RED,
                 SignalColor.YELLOW,
                 SignalColor.RED,
                 SignalColor.GREEN,
                 SignalColor.GREEN,
                 SignalColor.YELLOW,
                 SignalColor.YELLOW,
                 SignalColor.RED,
                 SignalColor.BLACK,
                 SignalColor.GREEN,
                 SignalColor.RED,
                 SignalColor.YELLOW,
                 SignalColor.RED,
                 SignalColor.YELLOW,
                 SignalColor.RED,
                 SignalColor.GREEN)));

        // print out what the settings of the light pattern are
        pdm(lp1.getAllLights().toString());
        pdm(lp1.getCounts());
        pdm(lp1.getMix().toString());

    }

    /**
     * stub main method for testing
     *
     * @param args
     */
    public static void main(String[] args) {
        new MiscTest();
    }

}

