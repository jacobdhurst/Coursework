package ControlSystem;
/**
 * TimeInput will have two methods: getTime() and resetTime().
 * The getTime returns an integer for how many seconds have passed, and resetTime starts the counter over.
 * Set verbose to true when constructing the TimeInput to get debug info printed to stdout.
 */

public class TimeInput implements Runnable {
    int value = 0;
    String[] tick = {"tock", "tick"};
    boolean verbose = false; // change to true for testing.

    public TimeInput() {
        Thread thread = new Thread(this);
        thread.start();
    }
    public TimeInput(boolean verbose) {
        this.verbose = verbose;
        Thread thread = new Thread(this);
        thread.start();
    }
    public int getTime() {
        if (verbose) System.out.println("Timer value: "+value);
        return value;
    }


    public void run() {
        while (true) {
            try {
                this.value += 1;
                if (verbose) System.out.println(tick[value%2]);
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                System.out.println("Timer interrupted:"+e.getMessage());
            }
        }
    }

    public void resetTime() {
        if (verbose) System.out.println("Timer reset");
        value = 0;
    }
}
