/*
 * CS351L Project #2: Boggle
 * Jacob Hurst
 * 09/16/17
 *
 * Tray.java - Tray object builds randomized char[][] with alphabetical characters.
 * also provides functionality for searching if a word exists in the generated tray.
 */

package Model;

import java.util.Random;

public class Tray
{
  /** Decided to use a char[][] array to represent the board,
   *  a char[][] array to track what's been visited,
   *  a letterCount[] to track letter usage,
   *  the String alphabet to pull randomly from.
   */
  private final char[][] tray;
  private final char[][] visited;
  private final int dimensions;
  private final int[] letterCount;
  private final String alphabet = "abcdefghijklmnopqrstuvwxyz";
  private final Random random = new Random();
  private final int n = alphabet.length();
  
  /**
   * Tray constructor.
   * @param dimensions
   */
  public Tray(int dimensions)
  {
    this.dimensions = dimensions;
    tray = new char[dimensions][dimensions];
    visited = new char[dimensions][dimensions];
    letterCount = new int[n];
    initialize(dimensions);
  }
  
  /**
   * @return current state of tray.
   */
  public char[][] getTray()
  {
    return tray;
  }
  
  /**
   * Searches the tray for the first character in the given word,
   * then calls the recursive search on that character.
   *
   * if the given word is found, function calls calculatePoints and
   * returns the number of points earned.
   * @param word
   */
  public int search(String word)
  {
    int points = 0;
    word = word.toLowerCase();
    if(word.length() < 3) return points;
    for(int r = 0; r < dimensions; r++)
    {
      for(int c = 0; c < dimensions; c++)
      {
        if(tray[r][c] == word.charAt(0))
        {
          if(recursiveSearch(word, r, c))
          {
            points = calculatePoints(word);
            
            setVisited(' ');
          }
        }
      }
    }
    return points;
  }
  
  /**
   * Helper function for search(String word).
   * Calculates the number of points earned for a given word.
   * @param word
   * @return
   */
  public int calculatePoints(String word)
  {
    if(word.length() >= 3) return (word.length() - 2);
    else return 0;
  }
  
  /**
   * Initializes the board randomly,
   * if 'q' is added to the board populates each adjacent cell with an increased probability
   * of (17/25) chance for 'u' until a 'u' is placed or we run out of adjacent cells.
   *
   * Increases letterCount[indexOfChar] for each letter added and verifies that no more than
   * 4 of each appear on tray.
   *
   * Sets all cells as unvisited ' '.
   * @param dimensions
   */
  private void initialize(int dimensions)
  {
    int randomIndex;
    for(int r = 0; r < dimensions; r++)
    {
      for(int c = 0; c < dimensions; c++)
      {
        if(tray[r][c] != 0) continue;
        randomIndex = getRandom(n);
        
        tray[r][c] = alphabet.charAt(randomIndex);
        letterCount[randomIndex]++;
  
        if(alphabet.charAt(randomIndex) == 'q') populateAdjacent(r, c);
        
        while(letterCount[randomIndex] > 3)
        {
          int tempI = randomIndex;
          char tempC = tray[r][c];
  
          randomIndex = getRandom(n);
          
          tray[r][c] = alphabet.charAt(randomIndex);
          if(tray[r][c] != tempC) letterCount[tempI]--;
        }
        visited[r][c] = ' ';
      }
    }
  }
  
  /**
   * Called by the initializer, populates all possible cells that are
   * adjacent to 'q' with an increased probability of (17/25) for 'u' until
   * either a 'u' is placed or there are no more adjacent cells to populate.
   * @param r
   * @param c
   */
  private void populateAdjacent(int r, int c)
  {
    int tempRandom;
    boolean uPlaced = false;
    for(int tempR = (r - 1); tempR < (r + 2); tempR++)
    {
      for(int tempC = (c - 1); tempC < (c + 2); tempC++)
      {
        if(uPlaced) continue;
        if(tempR == r && tempC == c) continue;
        if(0 > tempR || tempR > (dimensions - 1)) continue;
        if(0 > tempC || tempC > (dimensions - 1)) continue;
        if(letterCount[20] > 3) continue;
        
        tempRandom = getRandom(n, 17);
        
        if(tempRandom == 20) uPlaced = true;
        tray[tempR][tempC] = alphabet.charAt(tempRandom);
        letterCount[tempRandom]++;
      }
    }
  }
  
  /**
   * Helper function for search(String word).
   * Sets all cells to given character (Most frequently ' ' - unvisited).
   * @param value
   */
  private void setVisited(char value)
  {
    for(int r = 0; r < dimensions; r++)
    {
      for(int c = 0; c < dimensions; c++)
      {
        visited[r][c] = value;
      }
    }
  }
  
  /**
   * Searches the tray from the given (r, c) point
   * and the given substring of the word recursively
   * for the next character in the word.
   *
   * Returns true when the length of the word reaches 0.
   * @param word
   * @param r
   * @param c
   * @return boolean
   */
  private boolean recursiveSearch(String word, int r, int c)
  {
    if(word.length() == 0) return true;
    if(r < 0 || c < 0 || r > (dimensions - 1) || c > (dimensions - 1)) return false;
    if (visited[r][c] != 'x') {
      visited[r][c] = 'x';
      if(tray[r][c] == word.charAt(0)) {
        for(int tempR = (r - 1); tempR < (r + 2); tempR++)
        {
          for(int tempC = (c - 1); tempC < (c + 2); tempC++)
          {
            if(recursiveSearch(word.substring(1), tempR, tempC)) return true;
          }
        }
        visited[r][c] = ' ';
        return false;
      }
      visited[r][c] = ' ';
      return false;
    }
    return false;
  }
  
  /**
   * @param n
   * @return Random integer between 0 & n.
   */
  private int getRandom(int n)
  {
    return random.nextInt(n);
  }
  
  /**
   * @param n
   * @param uProbability
   * @return (uProbability/n) chance for index of 'U'
   * ((n - uProbability)/n) chance for index of other char.
   */
  private int getRandom(int n, int uProbability)
  {
    int rand = random.nextInt(n);
    if(rand > uProbability) return rand;
    else return 20;
  }
}
