package ControlSystem;

import Primary.*;

/**
 * This table contains all possible light patterns for our traffic control
 * system. Light patterns are listed in order of green, yellow, and red. That
 * is, each light pattern containing green lights will be followed by the same
 * light pattern with the green lights set to yellow and that will be followed
 * by an all red light pattern.
 * 
 * Timed mode loops over indices 0 through 11. Indices 12 through 17 are special
 * light patterns used by responsive mode. Indices 18 through 45 are special
 * light patterns used by emergency mode.
 */
public class LightPatternTable {
    private final LightPattern[] lightPatterns = new LightPattern[46];

    public LightPatternTable() {
        /* *********************** */
        /* Standard Configurations */
        /* *********************** */

        /* NS Left Turns - Green
         * Pedestrian Crossings - Red */
        LightPattern lp0 = new LightPattern(0);
        lp0.setLight(LightName.N1, SignalColor.GREEN);
        lp0.setLight(LightName.S1, SignalColor.GREEN);
        lightPatterns[0] = lp0;

        /* NS Left Turns - Yellow
         * Pedestrian Crossings - Red */
        LightPattern lp1 = new LightPattern(1);
        lp1.setLight(LightName.N1, SignalColor.YELLOW);
        lp1.setLight(LightName.S1, SignalColor.YELLOW);
        lightPatterns[1] = lp1;

        /* All red */
        LightPattern lp2 = new LightPattern(2);
        lightPatterns[2] = lp2;

        /* NS Through - Green
         * Parallel Pedestrian Crossings - Green */
        LightPattern lp3 = new LightPattern(3);
        lp3.setLight(LightName.N2, SignalColor.GREEN);
        lp3.setLight(LightName.N3, SignalColor.GREEN);
        lp3.setLight(LightName.S2, SignalColor.GREEN);
        lp3.setLight(LightName.S3, SignalColor.GREEN);
        lp3.setLight(LightName.CE, SignalColor.GREEN);
        lp3.setLight(LightName.CW, SignalColor.GREEN);
        lightPatterns[3] = lp3;

        /* NS Through - Yellow
         * Pedestrian Crossings - Red */
        LightPattern lp4 = new LightPattern(4);
        lp4.setLight(LightName.N2, SignalColor.YELLOW);
        lp4.setLight(LightName.N3, SignalColor.YELLOW);
        lp4.setLight(LightName.S2, SignalColor.YELLOW);
        lp4.setLight(LightName.S3, SignalColor.YELLOW);
        lightPatterns[4] = lp4;

        /* All red */
        LightPattern lp5 = new LightPattern(5);
        lightPatterns[5] = lp5;

        /* EW Left Turns - Green
         * Pedestrian Crossings - Red */
        LightPattern lp6 = new LightPattern(6);
        lp6.setLight(LightName.E1, SignalColor.GREEN);
        lp6.setLight(LightName.W1, SignalColor.GREEN);
        lightPatterns[6] = lp6;

        /* EW Left Turns - Yellow
         * Pedestrian Crossings - Red */
        LightPattern lp7 = new LightPattern(7);
        lp7.setLight(LightName.E1, SignalColor.YELLOW);
        lp7.setLight(LightName.W1, SignalColor.YELLOW);
        lightPatterns[7] = lp7;

        /* All red */
        LightPattern lp8 = new LightPattern(8);
        lightPatterns[8] = lp8;

        /* EW Through - Green
         * Parallel Pedestrian Crossings - Green */
        LightPattern lp9 = new LightPattern(9);
        lp9.setLight(LightName.E2, SignalColor.GREEN);
        lp9.setLight(LightName.E3, SignalColor.GREEN);
        lp9.setLight(LightName.W2, SignalColor.GREEN);
        lp9.setLight(LightName.W3, SignalColor.GREEN);
        lp9.setLight(LightName.CN, SignalColor.GREEN);
        lp9.setLight(LightName.CS, SignalColor.GREEN);
        lightPatterns[9] = lp9;

        /* EW Through - Yellow
         * Pedestrian Crossings - Red */
        LightPattern lp10 = new LightPattern(10);
        lp10.setLight(LightName.E2, SignalColor.YELLOW);
        lp10.setLight(LightName.E3, SignalColor.YELLOW);
        lp10.setLight(LightName.W2, SignalColor.YELLOW);
        lp10.setLight(LightName.W3, SignalColor.YELLOW);
        lightPatterns[10] = lp10;

        /* All red */
        LightPattern lp11 = new LightPattern(11);
        lightPatterns[11] = lp11;


        /* ****************************************** */
        /* Special Configurations for Responsive Mode */
        /* ****************************************** */

        /* NS Through - Green
         * Pedestrian Crossings - Red */
        LightPattern lp12 = new LightPattern(12);
        lp12.setLight(LightName.N2, SignalColor.GREEN);
        lp12.setLight(LightName.N3, SignalColor.GREEN);
        lp12.setLight(LightName.S2, SignalColor.GREEN);
        lp12.setLight(LightName.S3, SignalColor.GREEN);
        lightPatterns[12] = lp12;

        /* NS Through - Yellow
         * Pedestrian Crossings - Red */
        LightPattern lp13 = new LightPattern(13);
        lp13.setLight(LightName.N2, SignalColor.YELLOW);
        lp13.setLight(LightName.N3, SignalColor.YELLOW);
        lp13.setLight(LightName.S2, SignalColor.YELLOW);
        lp13.setLight(LightName.S3, SignalColor.YELLOW);
        lightPatterns[13] = lp13;

        /* All red */
        LightPattern lp14 = new LightPattern(14);
        lightPatterns[14] = lp14;

        /* EW Through - Green
         * Pedestrian Crossings - Red */
        LightPattern lp15 = new LightPattern(15);
        lp15.setLight(LightName.E2, SignalColor.GREEN);
        lp15.setLight(LightName.E3, SignalColor.GREEN);
        lp15.setLight(LightName.W2, SignalColor.GREEN);
        lp15.setLight(LightName.W3, SignalColor.GREEN);
        lightPatterns[15] = lp15;

        /* EW Through - Yellow
         * Pedestrian Crossings - Red */
        LightPattern lp16 = new LightPattern(16);
        lp16.setLight(LightName.E2, SignalColor.YELLOW);
        lp16.setLight(LightName.E3, SignalColor.YELLOW);
        lp16.setLight(LightName.W2, SignalColor.YELLOW);
        lp16.setLight(LightName.W3, SignalColor.YELLOW);
        lightPatterns[16] = lp16;

        /* All red */
        LightPattern lp17 = new LightPattern(17);
        lightPatterns[17] = lp17;


        /* ***************************************** */
        /* Special Configurations for Emergency Mode */
        /* ***************************************** */
        
        /* Northbound Through and Left Turn - Green */
        LightPattern lp18 = new LightPattern(18);
        lp18.setLight(LightName.N1, SignalColor.GREEN);
        lp18.setLight(LightName.N2, SignalColor.GREEN);
        lp18.setLight(LightName.N3, SignalColor.GREEN);
        lightPatterns[18] = lp18;

        /* Northbound Through and Left Turn - Yellow */
        LightPattern lp19 = new LightPattern(19);
        lp19.setLight(LightName.N1, SignalColor.YELLOW);
        lp19.setLight(LightName.N2, SignalColor.YELLOW);
        lp19.setLight(LightName.N3, SignalColor.YELLOW);
        lightPatterns[19] = lp19;

        /* All red */
        LightPattern lp20 = new LightPattern(20);
        lightPatterns[20] = lp20;

        /* Eastbound Through and Left Turn - Green */
        LightPattern lp21 = new LightPattern(21);
        lp21.setLight(LightName.E1, SignalColor.GREEN);
        lp21.setLight(LightName.E2, SignalColor.GREEN);
        lp21.setLight(LightName.E3, SignalColor.GREEN);
        lightPatterns[21] = lp21;

        /* Eastbound Through and Left Turn - Yellow */
        LightPattern lp22 = new LightPattern(22);
        lp22.setLight(LightName.E1, SignalColor.YELLOW);
        lp22.setLight(LightName.E2, SignalColor.YELLOW);
        lp22.setLight(LightName.E3, SignalColor.YELLOW);
        lightPatterns[22] = lp22;

        /* All red */
        LightPattern lp23 = new LightPattern(23);
        lightPatterns[23] = lp23;

        /* Southbound Through and Left Turn - Green */
        LightPattern lp24 = new LightPattern(24);
        lp24.setLight(LightName.S1, SignalColor.GREEN);
        lp24.setLight(LightName.S2, SignalColor.GREEN);
        lp24.setLight(LightName.S3, SignalColor.GREEN);
        lightPatterns[24] = lp24;

        /* Southbound Through and Left Turn - Yellow */
        LightPattern lp25 = new LightPattern(25);
        lp25.setLight(LightName.S1, SignalColor.YELLOW);
        lp25.setLight(LightName.S2, SignalColor.YELLOW);
        lp25.setLight(LightName.S3, SignalColor.YELLOW);
        lightPatterns[25] = lp25;

        /* All red */
        LightPattern lp26 = new LightPattern(26);
        lightPatterns[26] = lp26;

        /* Westbound Through and Left Turn - Green */
        LightPattern lp27 = new LightPattern(27);
        lp27.setLight(LightName.W1, SignalColor.GREEN);
        lp27.setLight(LightName.W2, SignalColor.GREEN);
        lp27.setLight(LightName.W3, SignalColor.GREEN);
        lightPatterns[27] = lp27;

        /* Westbound Through and Left Turn - Yellow */
        LightPattern lp28 = new LightPattern(28);
        lp28.setLight(LightName.W1, SignalColor.YELLOW);
        lp28.setLight(LightName.W2, SignalColor.YELLOW);
        lp28.setLight(LightName.W3, SignalColor.YELLOW);
        lightPatterns[28] = lp28;

        /* All red */
        LightPattern lp29 = new LightPattern(29);
        lightPatterns[29] = lp29;

        /*BELOW : SPECIAL EMERGENCY USE ONLY*/


        /* Northbound Through - Green
         * Southbound Through - Yellow */
        LightPattern lp30 = new LightPattern(30);
        lp30.setLight(LightName.N2, SignalColor.GREEN);
        lp30.setLight(LightName.N3, SignalColor.GREEN);
        lp30.setLight(LightName.S2, SignalColor.YELLOW);
        lp30.setLight(LightName.S3, SignalColor.YELLOW);
        lightPatterns[30] = lp30;

        /* Northbound Through - Green */
        LightPattern lp31 = new LightPattern(31);
        lp31.setLight(LightName.N2, SignalColor.GREEN);
        lp31.setLight(LightName.N3, SignalColor.GREEN);
        lightPatterns[31] = lp31;

        /* Go to index 18 to set all north to green */

        /* Eastbound Through - Green
         * Westbound Through - Yellow */
        LightPattern lp32 = new LightPattern(32);
        lp32.setLight(LightName.E2, SignalColor.GREEN);
        lp32.setLight(LightName.E3, SignalColor.GREEN);
        lp32.setLight(LightName.W2, SignalColor.YELLOW);
        lp32.setLight(LightName.W3, SignalColor.YELLOW);
        lightPatterns[32] = lp32;

        /* Eastbound Through - Green */
        LightPattern lp33 = new LightPattern(33);
        lp33.setLight(LightName.E2, SignalColor.GREEN);
        lp33.setLight(LightName.E3, SignalColor.GREEN);
        lightPatterns[33] = lp33;

        /* Go to index 21 to set all east to green */

        /* Southbound Through - Green
         * Northbound Through - Yellow */
        LightPattern lp34 = new LightPattern(34);
        lp34.setLight(LightName.S2, SignalColor.GREEN);
        lp34.setLight(LightName.S3, SignalColor.GREEN);
        lp34.setLight(LightName.N2, SignalColor.YELLOW);
        lp34.setLight(LightName.N3, SignalColor.YELLOW);
        lightPatterns[34] = lp34;

        /* Southbound Through - Green */
        LightPattern lp35 = new LightPattern(35);
        lp35.setLight(LightName.S2, SignalColor.GREEN);
        lp35.setLight(LightName.S3, SignalColor.GREEN);
        lightPatterns[35] = lp35;

        /* Go to index 24 to set all south to green */

        /* Westbound Through - Green
         * Eastbound Through - Yellow */
        LightPattern lp36 = new LightPattern(36);
        lp36.setLight(LightName.W2, SignalColor.GREEN);
        lp36.setLight(LightName.W3, SignalColor.GREEN);
        lp36.setLight(LightName.E2, SignalColor.YELLOW);
        lp36.setLight(LightName.E3, SignalColor.YELLOW);
        lightPatterns[36] = lp36;

        /* Westbound Through - Green */
        LightPattern lp37 = new LightPattern(37);
        lp37.setLight(LightName.W2, SignalColor.GREEN);
        lp37.setLight(LightName.W3, SignalColor.GREEN);
        lightPatterns[37] = lp37;

        /* Go to index 27 to set all west to green */

        /* Northbound Left Turn - Green
         * Southbound Left Turn - Yellow */
        LightPattern lp38 = new LightPattern(38);
        lp38.setLight(LightName.N1, SignalColor.GREEN);
        lp38.setLight(LightName.S1, SignalColor.YELLOW);
        lightPatterns[38] = lp38;

        /* Northbound Left Turn - Green */
        LightPattern lp39 = new LightPattern(39);
        lp39.setLight(LightName.N1, SignalColor.GREEN);
        lightPatterns[39] = lp39;

        /* Go to index 18 to set all north to green */

        /* Eastbound Left Turn - Green
         * Westbound Left Turn - Yellow */
        LightPattern lp40 = new LightPattern(40);
        lp40.setLight(LightName.E1, SignalColor.GREEN);
        lp40.setLight(LightName.W1, SignalColor.YELLOW);
        lightPatterns[40] = lp40;

        /* Eastbound Left Turn - Green */
        LightPattern lp41 = new LightPattern(41);
        lp41.setLight(LightName.E1, SignalColor.GREEN);
        lightPatterns[41] = lp41;

        /* Go to index 21 to set all east to green */

        /* Southbound Left Turn - Green
         * Northbound Left Turn - Yellow */
        LightPattern lp42 = new LightPattern(42);
        lp42.setLight(LightName.S1, SignalColor.GREEN);
        lp42.setLight(LightName.N1, SignalColor.YELLOW);
        lightPatterns[42] = lp42;

        /* Southbound Left Turn - Green */
        LightPattern lp43 = new LightPattern(43);
        lp43.setLight(LightName.S1, SignalColor.GREEN);
        lightPatterns[43] = lp43;

        /* Go to index 24 to set all south to green */

        /* Westbound Left Turn - Green
         * Eastbound Left Turn - Yellow */
        LightPattern lp44 = new LightPattern(44);
        lp44.setLight(LightName.W1, SignalColor.GREEN);
        lp44.setLight(LightName.E1, SignalColor.YELLOW);
        lightPatterns[44] = lp44;

        /* Westbound Left Turn - Green */
        LightPattern lp45 = new LightPattern(45);
        lp45.setLight(LightName.W1, SignalColor.GREEN);
        lightPatterns[45] = lp45;

        /* Go to index 27 to set all west to green */
    }

    /**
     * Returns the light pattern at the given index. The index must be a value
     * between 0 and 45.
     * 
     * @param index index of the light pattern to return
     * 
     * @return the light pattern at the given index
     */
    public LightPattern getLightPattern(int index) {
        return lightPatterns[index];
    }
}
