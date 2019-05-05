/*
 * CS351L Project #2: Boggle
 * Jacob Hurst
 * 09/16/17
 *
 * ModelController.java - Controls the state of the game.
 */

package Controller;

import Model.Dictionary;
import Model.Player;
import Model.Tray;
import java.io.FileNotFoundException;
import java.util.ArrayList;

class ModelController
{
  private final Dictionary dictionary;
  private final Tray tray;
  private final Player player;
  private final ArrayList<String> possible;
  private int possibleScore;
  
  /**
   * Default constructor.
   * Loads the dictionary, initializes the tray,
   * initializes the player, and finds all possible words.
   * @param dimensions
   * @throws FileNotFoundException
   */
  public ModelController(int dimensions)
  {
    /* loading the dictionary */
    dictionary = new Dictionary("Resources/OpenEnglishWordList.txt");
    
    /* loading the player */
    player = new Player("User");
    
    /* loading the tray */
    tray = new Tray(dimensions);
  
    /* finding all possible words & possible score */
    ArrayList<String> temp = dictionary.getDictionary();
    possible = new ArrayList<>();
    possibleScore = 0;
    for(String word : temp)
    {
      int points = tray.search(word);
      if(points > 0) possible.add(word);
      possibleScore += points;
    }
  }
  
  /**
   * Checks validity of inputted word.
   * @param word
   * @return true if word is in possible list, has not already been guessed, and length > 2;
   * false otherwise.
   */
  public boolean handleWord(String word)
  {
    boolean valid = false;
    int points;
    if(possible.contains(word) && !player.getCorrect().contains(word) && word.length() > 2)
    {
      player.setCorrect(word);
      points = tray.calculatePoints(word);
      player.addPoints(points);
      valid = true;
    }
    else if(!possible.contains(word) && !player.getGuesses().contains(word))
    {
      player.setIncorrect(word);
    }
    return valid;
  }
  
  /**
   * @return list of correct guesses.
   */
  public ArrayList<String> getCorrect()
  {
    return player.getCorrect();
  }
  
  /**
   * @return list of possible words in tray & dictionary.
   */
  public ArrayList<String> getPossible()
  {
    return possible;
  }
  
  /**
   * @return possible player score.
   */
  public int getPossibleScore()
  {
    return possibleScore;
  }
  
  /**
   * @return current score of player.
   */
  public int getScore()
  {
    return player.getScore();
  }
  
  /**
   * @return current tray.
   */
  public char[][] getTray()
  {
    return tray.getTray();
  }
}