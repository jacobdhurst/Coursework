package ControlSystem;

import javafx.scene.effect.Light;

import java.util.ArrayList;
import java.util.HashMap;
/**
*Class for holding sequences of light patterns(LightSequence's) used to reduce coding in Responsive Mode
 *by grouping the various patterns into the correct sequence of patterns
 */
public class ResponsivePatternTable
{
  private HashMap<String,LightSequence> table = new HashMap<>();
  private HashMap<Integer,LightSequence> lookup = new HashMap<>();

  /**
   * builds the various light sequences by using an array of keys to generate an array of light patterns which are
   * used to create the sequence. Afterwards the keys are added to the lookup hashmap so that specific sequences can be
   * found and continued to completion
   * @param lightpatterns
   */
  public ResponsivePatternTable(LightPatternTable lightpatterns)
  {
    int[] keys = new int[]{2};//All Red(Normal Sequence)
    LightSequence sequence = new LightSequence("R", getpatterns(keys,lightpatterns), keys);
    table.put("R", sequence);
    lookupput(keys,sequence);

    keys = new int[]{0, 1, 2};//North/South Left Turn (Normal Sequence)
    sequence = new LightSequence("NS LT", getpatterns(keys,lightpatterns), keys);
    table.put("NS LT", sequence);
    lookupput(keys,sequence);

    keys = new int[]{3, 4, 5};//North/South Through with Pedestrians(Normal Sequence)
    sequence = new LightSequence("NS T P", getpatterns(keys,lightpatterns), keys);
    table.put("NS T P", sequence);
    lookupput(keys,sequence);

    keys = new int[]{6, 7, 8};//East/West Left Turn(Normal Sequence)
    sequence = new LightSequence("EW LT", getpatterns(keys,lightpatterns), keys);
    table.put("EW LT", sequence);
    lookupput(keys,sequence);

    keys =  new int[]{9, 10, 11};//East/West Through with Pedestrians(Normal Sequence)
    sequence = new LightSequence("EW T P", getpatterns(keys,lightpatterns),keys);
    table.put("EW T P", sequence);
    lookupput(keys,sequence);

    keys = new int[]{12, 13, 14};//North/South Through without Pedestrians(Normal Sequence)
    sequence = new LightSequence("NS T", getpatterns(keys,lightpatterns), keys);
    table.put("NS T", sequence);
    lookupput(keys,sequence);

    keys = new int[]{15, 16, 17};//East/West Through without Pedestrians(Normal Sequence)
    sequence = new LightSequence("EW T", getpatterns(keys,lightpatterns),keys);
    table.put("EW T", sequence);
    lookupput(keys,sequence);

    keys = new int[]{18, 19, 20};//North Through/Left Turn(Emergency Sequence)
    sequence = new LightSequence("N T/LT", getpatterns(keys,lightpatterns), keys);
    table.put("N T/LT", sequence);
    lookupput(keys,sequence);

    keys =  new int[]{21, 22, 23};//East Through/Left Turn(Emergency Sequence)
    sequence = new LightSequence("E T/LT", getpatterns(keys,lightpatterns),keys);
    table.put("E T/LT", sequence);
    lookupput(keys,sequence);

    keys = new int[]{24, 25, 26};//South Through/Left Turn(Emergency Sequence)
    sequence = new LightSequence("S T/LT", getpatterns(keys,lightpatterns), keys);
    table.put("S T/LT", sequence);
    lookupput(keys,sequence);

    keys = new int[]{27, 28, 29};//West Through/Left Turn(Emergency Sequence)
    sequence = new LightSequence("W T/LT", getpatterns(keys,lightpatterns), keys);
    table.put("W T/LT", sequence);
    lookupput(keys,sequence);

    keys = new int[]{30, 31, 18, 19, 20};//Northbound (Emergency Sequence) with Southbound through light being turned off
    sequence = new LightSequence("N Emerg S T", getpatterns(keys,lightpatterns),keys);
    table.put("N Emerg S T",sequence);
    lookupput(new int[]{30,31},sequence);

    keys = new int[]{32, 33, 21, 22, 23};//Eastbound (Emergency Sequence) with Westbound through light being turned off
    sequence = new LightSequence("E Emerg W T", getpatterns(keys,lightpatterns),keys);
    table.put("E Emerg W T",sequence);
    lookupput(new int[]{32,33},sequence);

    keys = new int[]{34, 35, 24, 25, 26};//Southbound (Emergency Sequence) with Northbound through light being turned off
    sequence = new LightSequence("S Emerg N T", getpatterns(keys,lightpatterns),keys);
    table.put("S Emerg N T",sequence);
    lookupput(new int[]{34,35},sequence);

    keys = new int[]{36, 37, 27, 28, 29};//Westbound (Emergency Sequence) with Eastbound through light being turned off
    sequence = new LightSequence("W Emerg E T", getpatterns(keys,lightpatterns),keys);
    table.put("W Emerg E T",sequence);
    lookupput(new int[]{36,37},sequence);

    keys = new int[]{38, 39, 18, 19, 20};//Northbound (Emergency Sequence) with Southbound left turn light being turned off
    sequence = new LightSequence("N Emerg S LT", getpatterns(keys,lightpatterns),keys);
    table.put("N Emerg S LT",sequence);
    lookupput(new int[]{38,39},sequence);

    keys = new int[]{40, 41, 21, 22, 23};//Eastbound (Emergency Sequence) with Westbound left turn light being turned off
    sequence = new LightSequence("E Emerg W LT", getpatterns(keys,lightpatterns),keys);
    table.put("E Emerg W LT",sequence);
    lookupput(new int[]{40,41},sequence);

    keys = new int[]{42, 43, 24, 25, 26};//Southbound (Emergency Sequence) with Northbound left turn light being turned off
    sequence = new LightSequence("S Emerg N LT", getpatterns(keys,lightpatterns),keys);
    table.put("S Emerg N LT",sequence);
    lookupput(new int[]{42,43},sequence);

    keys = new int[]{44, 45, 27, 28, 29};//Westbound (Emergency Sequence) with Eastbound left turn light being turned off
    sequence = new LightSequence("W Emerg E LT", getpatterns(keys,lightpatterns),keys);
    table.put("W Emerg E LT",sequence);
    lookupput(new int[]{44,45},sequence);
  }

  /**
   * internal method for adding the various keys to the lookup hashmap
   * @param keys
   * @param sequence
   */
  private void lookupput(int[] keys, LightSequence sequence)
  {
    for(int i = 0; i < keys.length; i++)
    {
      lookup.put(keys[i],sequence);
    }
  }

  /**
   * internal method for creating an array of light patterns using the given keys
   * @param keys
   * @param lightpatterns
   * @return
   */
  private LightPattern[] getpatterns(int[] keys, LightPatternTable lightpatterns)
  {
    LightPattern[] patterns = new LightPattern[keys.length];
    for(int i = 0; i < keys.length; i++)
    {
      patterns[i] = lightpatterns.getLightPattern(keys[i]);
    }
    return patterns;
  }

  /**
   * method for getting a specific sequence using the sequences key/id
   * @param key
   * @return
   */
  public LightSequence getSequence(String key)
  {
    return table.get(key);
  }

  /**
   * method for getting a sequence that contains a specific LightPattern which may need to be completed
   * by finding the appropriate sequence and setting that sequences current LightPattern to the input LightPattern
   * @param pattern
   * @return
   */
  public LightSequence findPattern(int pattern)
  {
    LightSequence sequence;
    if(lookup.containsKey(pattern))
    {
      sequence = lookup.get(pattern);
      sequence.update(pattern);
    }
    else sequence = table.get("R");
    return sequence;
  }


}
