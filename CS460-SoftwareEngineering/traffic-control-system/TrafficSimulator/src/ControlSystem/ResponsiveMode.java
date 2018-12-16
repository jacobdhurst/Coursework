package ControlSystem;

import Primary.Lanes;
import Primary.Lights;

import java.util.Arrays;

public class ResponsiveMode implements Schedule
{
  private LightSequence currentsequence;
  private final ResponsivePatternTable sequences;
  private final LightPatternTable patternTable;




  /**
   * Constructs a new ResponsiveMode object with the given light pattern table
   * Initializes the ResponsivePatternTable
   * Sets the first Sequence to all RED
   * @param lightPatterns light pattern table to use
   */
  public ResponsiveMode(LightPatternTable lightPatterns) {
    this.patternTable = lightPatterns;
    this.sequences = new ResponsivePatternTable(lightPatterns);
    currentsequence = sequences.getSequence("R");
  }

  /**
   * Returns the current pattern index.
   *
   * @return current pattern index
   */
  public int getPatternIndex() {
    return currentsequence.currentindex();
  }


  public LightPattern getPreviousPattern() {
    return currentsequence.currentpattern();
  }

  /**
   * Returns the next light pattern in responsive mode.
   *
   * @return next light pattern
   */
  public LightPattern nextPattern(int[] CarInput, int[] PedInput) {
    //checks if current sequence is over/completed
    //Boolean variables representing sensor states for wanted sensor groups
    boolean ns_through;//North/South Through Sensors
    boolean ew_through;//East/West Through Sensors
    boolean ns_left;//North/South Left Turn Sensors
    boolean ew_left;//East/West Left Turn Sensors
    boolean ns_ped;//North/South Pedestrian Sensors
    boolean ew_ped;//East/West Pedestrian Sensors

    //parsing boolean values for sensor groups
    if(CarInput[2] == 1 || CarInput[1] == 1 || CarInput[8] == 1 || CarInput[7] == 1) ns_through = true;
    else ns_through = false;
    if(CarInput[0] == 1|| CarInput[6] == 1) ns_left = true;
    else ns_left = false;
    if(PedInput[1] == 1 || PedInput[3] == 1) ns_ped = true;
    else ns_ped = false;
    if(CarInput[5] == 1 || CarInput[4] == 1 || CarInput[11] == 1 || CarInput[10] == 1) ew_through = true;
    else ew_through = false;
    if(CarInput[3] == 1 || CarInput[9] == 1) ew_left = true;
    else ew_left = false;
    if(PedInput[0] == 1 || PedInput[2] == 1) ew_ped = true;
    else ew_ped = false;

    if (currentsequence.isOver())
    {
      //getting current sequence id
      String id = currentsequence.getID();
      //SENSOR PRIORITY ORDERING::: North/South > East/West, Left Turns > Through > Pedestrians
      //if no sensors are active defaults to all RED sequence
      if(!(ns_left || ns_through || ns_ped || ew_through || ew_ped || ew_left))
      {
        currentsequence = sequences.getSequence("R");
      }
      //if current sequence is all RED if statements look for active sensors in order of priority
      else if(id == "R")
      {
        if(ns_left) currentsequence = sequences.getSequence("NS LT");
        else if(ew_left) currentsequence = sequences.getSequence("EW LT");
        else if(ns_through) nsThroughSequence(ns_ped);
        else if(ew_through) ewThroughSequence(ew_ped);
        else if(ns_ped) currentsequence = sequences.getSequence("NS T P");
        else if(ew_ped) currentsequence = sequences.getSequence("EW T P");
      }
      //if current sequence is North/South Left Turns checks to see if N/S Through Sensors are active before checking
      //N/S Pedestrian Sensors and then checking East/West using priority ordering
      else if(id == "NS LT")
      {
        if(ns_through) nsThroughSequence(ns_ped);
        else if(ns_ped) currentsequence = sequences.getSequence("NS T P");
        else if(ew_left) currentsequence = sequences.getSequence("EW LT");
        else if(ew_through) ewThroughSequence(ew_ped);
        else currentsequence = sequences.getSequence("EW T P");
      }
      //if current sequence is East/West Left Turns checks to see if E/W Through Sensors are active before checking
      //E/W Pedestrian Sensors and then checking North/South using priority ordering
      else if(id == "EW LT")
      {
        if(ew_through) ewThroughSequence(ew_ped);
        else if(ew_ped) currentsequence = sequences.getSequence("EW T P");
        else if(ns_left) currentsequence = sequences.getSequence("NS LT");
        else if(ns_through) nsThroughSequence(ns_ped);
        else currentsequence = sequences.getSequence("NS T P");
      }
      //if current sequence is North/South Through checks East/West sensors in order of priority before checking
      //North/South in order of priority.
      else if(id == "NS T" || id == "NS T P")
      {
        if(ew_left) currentsequence = sequences.getSequence("EW LT");
        else if(ew_through) ewThroughSequence(ew_ped);
        else if (ew_ped) currentsequence = sequences.getSequence("EW T P");
        else if(ns_left) currentsequence = sequences.getSequence("NS LT");
        else nsThroughSequence(ns_ped);
      }
      //if current sequence is East/West Through checks North/South sensors in order of priority before checking
      //East/West in order of priority.
      else if(id == "EW T" || id == "EW T P")
      {
        if(ns_left) currentsequence = sequences.getSequence("NS LT");
        else if(ns_through) nsThroughSequence(ns_ped);
        else if (ns_ped) currentsequence = sequences.getSequence("NS T P");
        else if(ew_left) currentsequence = sequences.getSequence("EW LT");
        else ewThroughSequence(ew_ped);
      }
      //if current sequence is not a normal sequence the system goes to all red and a new normal sequence is chosen
      //with normal sequences being non emergency sequences
      else currentsequence = sequences.getSequence("R");
      currentsequence.reset();//resets the sequence to its beginning
    }
    else currentsequence.nextpattern();
    return currentsequence.currentpattern();
  }

  /**
   * returns the next pattern by finding where the current pattern is in one of the defined sequences and returning the
   * next pattern if the sequence is not completed or choosing a new sequence if it has been completed
   * @param patternIndex
   * @param CarInput
   * @param PedInput
   * @return
   */
  public LightPattern nextPattern(int patternIndex, int[] CarInput, int[] PedInput) {
    currentsequence = sequences.findPattern(patternIndex);
    return nextPattern(CarInput, PedInput);
  }

  /**
   * internal method for determining which North/South Through sequence is needed based on Pedestrian Sensors
   * @param ns_ped
   */
  private void nsThroughSequence(boolean ns_ped)
  {
    if(ns_ped) currentsequence = sequences.getSequence("NS T P");
    else currentsequence = sequences.getSequence("NS T");
  }

  /**
   * internal method for determining which East/West Through sequence is needed based on Pedestrian Sensors
   * @param ew_ped
   */
  private void ewThroughSequence(boolean ew_ped)
  {
    if(ew_ped) currentsequence = sequences.getSequence("EW T P");
    else currentsequence = sequences.getSequence("EW T");
  }
}
