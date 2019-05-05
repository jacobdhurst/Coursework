/*
 * CS351L Project #2: Boggle
 * Jacob Hurst
 * 09/17/17
 *
 * Player.java - Player object holds multiple lists of guesses and a score.
 */

package Model;

import java.util.ArrayList;

public class Player
{
  private final ArrayList<String> guesses;
  private final ArrayList<String> incorrect;
  private final ArrayList<String> correct;
  private int score;
  
  /**
   * Player constructor.
   * @param ID
   */
  public Player(String ID)
  {
    String playerID = ID;
    guesses = new ArrayList<>();
    incorrect = new ArrayList<>();
    correct = new ArrayList<>();
    
    score = 0;
  }
  
  /**
   * Adds the given word to the correct list
   * and the total guess list.
   * @param guess
   */
  public void setCorrect(String guess)
  {
    correct.add(guess);
    guesses.add(guess);
  }
  
  /**
   * Adds the given word to the incorrect list
   * and the total guess list.
   * @param guess
   */
  public void setIncorrect(String guess)
  {
    incorrect.add(guess);
    guesses.add(guess);
  }
  
  /**
   * Adds points to the players current score.
   * @param
   */
  public void addPoints(int points)
  {
    score += points;
  }
  
  /**
   * @return Current list of all guesses.
   */
  public ArrayList<String> getGuesses()
  {
    return guesses;
  }
  
  /**
   * @return Current list of correct guesses.
   */
  public ArrayList<String> getCorrect()
  {
    return correct;
  }
  
  /**
   * @return Current score of player.
   */
  public int getScore()
  {
    return score;
  }
}
