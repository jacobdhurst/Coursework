package ControlSystem;

/**
 * Class for containing multiple LightPatterns which form a Sequence GREEN->YELLOW->RED or some other combination that
 * ends in a RED State(Assuming proper inputs)
 */
public class LightSequence
{
  private String ID; //Light Sequence ID
  private final LightPattern[] sequence;//array of LightPatterns which form the sequence
  private final int[] indexs;//Array of LightPattern Index's which allow for proper communication with other modes
  private int current = 0;//current position in the sequence


  /**
   * constructor
   * @param id
   * @param sequence
   * @param indexs
   */
  LightSequence(String id, LightPattern[] sequence,int[] indexs)
  {
    this.ID = id;
    this.sequence = sequence;
    this.indexs = indexs;

  }

  /**
   * method for returning the ID
   * @return
   */
  public String getID()
  {
    return ID;
  }

  /**
   * method for getting the current LightPattern
   * @return
   */
  public LightPattern currentpattern()
  {
    return sequence[current];
  }

  /**
   * method for getting the current LightPattern Index
   * @return
   */
  public int currentindex()
  {
    return indexs[current];
  }

  /**
   * method for getting the next LightPattern in the sequence which increments the current value and makes sure it
   * cannot go out of bounds by taking the modulus of the new current divided by the length of the sequence array
   * although the pattern can loop by doing so.
   * @return
   */
  public LightPattern nextpattern()
  {
    current = (current+1)%sequence.length;
    return sequence[current];
  }

  /**
   * resets the sequence to its start
   */
  public void reset()
  {
    current = 0;
  }

  /**
   * checks to see if the sequence is over which is the last element of the array returning true if it is and false if
   * it is not
   * @return
   */
  public Boolean isOver()
  {
    return (current == sequence.length - 1);
  }

  /**
   * used for finding a pattern in the sequence and setting the current value to that pattern in order to continue a
   * sequence begun in another mode
   * @param pattern
   */
  public void update(int pattern)
  {
    for(int i = 0; i < indexs.length; i++)
    {
      if(indexs[i] == pattern)
      {
        current = i;
        return;
      }
    }

  }

}
