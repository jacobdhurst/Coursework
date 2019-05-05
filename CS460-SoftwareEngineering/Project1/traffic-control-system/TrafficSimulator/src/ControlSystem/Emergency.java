package ControlSystem;

import Primary.*;

/**
 * Polls the emergency sensor to return the emergency state.
 */
public class Emergency {
    private static Emergencies prevEmergency = Emergencies.NO_EMERGENCY;

    /**
     * Returns the emergency state.
     *
     * @return emergency state
     */
    public static Emergencies getEmergency() {
        boolean nEmerg = Lanes.N1.getEmergencyOnLane() ||
                         Lanes.N2.getEmergencyOnLane() ||
                         Lanes.N3.getEmergencyOnLane();

        boolean eEmerg = Lanes.E1.getEmergencyOnLane() ||
                         Lanes.E2.getEmergencyOnLane() ||
                         Lanes.E3.getEmergencyOnLane();

        boolean sEmerg = Lanes.S1.getEmergencyOnLane() ||
                         Lanes.S2.getEmergencyOnLane() ||
                         Lanes.S3.getEmergencyOnLane();

        boolean wEmerg = Lanes.W1.getEmergencyOnLane() ||
                         Lanes.W2.getEmergencyOnLane() ||
                         Lanes.W3.getEmergencyOnLane();
        
        /* Prioritize preivous emergency */
        if (prevEmergency.equals(Emergencies.NORTH_EMERGENCY) && nEmerg) {
            return Emergencies.NORTH_EMERGENCY;
        }

        if (prevEmergency.equals(Emergencies.EAST_EMERGENCY) && eEmerg) {
            return Emergencies.EAST_EMERGENCY;
        }
        
        if (prevEmergency.equals(Emergencies.SOUTH_EMERGENCY) && sEmerg) {
            return Emergencies.SOUTH_EMERGENCY;
        }

        if (prevEmergency.equals(Emergencies.WEST_EMERGENCY) && wEmerg) {
            return Emergencies.WEST_EMERGENCY;
        }

        /* Return new emergency */
        if (nEmerg) {
            prevEmergency = Emergencies.NORTH_EMERGENCY;
            return Emergencies.NORTH_EMERGENCY;
        }

        if (eEmerg) {
            prevEmergency = Emergencies.EAST_EMERGENCY;
            return Emergencies.EAST_EMERGENCY;
        }

        if (sEmerg) {
            prevEmergency = Emergencies.SOUTH_EMERGENCY;
            return Emergencies.SOUTH_EMERGENCY;
        }

        if (wEmerg) {
            prevEmergency = Emergencies.WEST_EMERGENCY;
            return Emergencies.WEST_EMERGENCY;
        }

        return Emergencies.NO_EMERGENCY;
    }
}
