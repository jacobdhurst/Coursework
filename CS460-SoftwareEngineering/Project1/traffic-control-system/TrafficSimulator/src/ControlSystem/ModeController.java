/**
 * ModeController has one method, run() which is called by the TestTCS class to begin running.
 * Mode Controller cycles between Mode calls, Schedule calls, and Light Switcher calls. There is a
 * short delay at the end of every cycle, the length of this delay is dictated by the sleepSeconds
 * parameter from the constructor.
 *
 * Each cycle, Mode Controller gets the current mode.
 * Then it will call Scheduler with getNextPattern(Mode).
 * Finally, the result from the getNextPattern will be a Light Pattern which will be sent to Light Switcher.
 */
package ControlSystem;

public class ModeController implements Runnable{
    private Scheduler scheduler;
    private int sleepSeconds;
    public ModeController(Scheduler scheduler, int sleepTime) {
        this.scheduler = scheduler;
        this.sleepSeconds = sleepTime * 1000;
    }
    public void run() {
        while (true) {
            // get mode
            int currentMode = Mode.getMode();

            // get next pattern from scheduler
            LightPattern pattern = scheduler.getNextPattern(currentMode);

            // TODO send pattern to light switcher
            LightSwitcher.setLights(pattern);

            //sleep
            try {
                Thread.sleep(sleepSeconds);
            }
            catch (InterruptedException e) {
                System.out.println("ModeController's slumber has been disturbed: "+e.getMessage());
            }
        }

    }
}
