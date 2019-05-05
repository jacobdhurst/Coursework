package ElevatorSystem.ElevatorComponents;

import java.util.Random;

import static ElevatorSystem.SystemValues.weightOverloadChance;
import static ElevatorSystem.SystemValues.weightOverloadChanceBound;

public class WeightSensor
{
  private final Random rnd = new Random();

  /**
   * Returns status of mechanical failure.
   *
   * @return true if there is a mechanical failure; false otherwise
   */
  public boolean getStatus() {
    return (rnd.nextInt(weightOverloadChanceBound) <= weightOverloadChance);
  }
}
