package ElevatorSystem.ElevatorComponents;

import java.util.Random;

import static ElevatorSystem.SystemValues.obstructionChance;
import static ElevatorSystem.SystemValues.obstructionChanceBound;

public class ObstructionSensor
{
  private final Random rnd = new Random();

  /**
   * Returns status of mechanical failure.
   *
   * @return true if there is a mechanical failure; false otherwise
   */
  public boolean getStatus() {
    return (rnd.nextInt(obstructionChanceBound) <= obstructionChance);
  }
}
