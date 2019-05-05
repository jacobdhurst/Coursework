package ElevatorSystem.Mode;

/**
 * Used to test OperatingMode.
 */
public class ModeTest {
    private static final int ITER = 400;
    private static final int PAD = 10;

    public static void main(String[] args) {
        OperatingMode om = new OperatingMode();

        String msg = "";
        msg += padRight("E1:");
        msg += padRight("E2:");
        msg += padRight("E3:");
        msg += padRight("E4:");

        System.out.println(msg);

        for(int i = 0; i < ITER; i++) {
            Mode[] modes = om.getModes();

            msg = "";
            msg += padRight(modes[0].toString());
            msg += padRight(modes[1].toString());
            msg += padRight(modes[2].toString());
            msg += padRight(modes[3].toString());

            System.out.println(msg);
        }
    }

    private static String padRight(String s) {
        return String.format("%1$-" + PAD + "s", s);
    }
}
