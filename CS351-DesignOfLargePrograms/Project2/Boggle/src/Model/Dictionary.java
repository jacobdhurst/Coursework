/*
 * CS351L Project #2: Boggle
 * Jacob Hurst
 * 09/16/17
 *
 * Dictionary.java - Dictionary loads in dictionary file and provides search/edit functionality.
 */

package Model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Dictionary
{
  private final ArrayList<String> dictionary;
  
  /**
   * Dictionary constructor.
   * @param file
   */
  public Dictionary(String file)
  {
    dictionary = new ArrayList<>();
    write(file);
  }
  
  /**
   * @return ArrayList<String> dictionary.
   */
  public ArrayList<String> getDictionary()
  {
    return dictionary;
  }
  
  /**
   * Writes each line from the given file to an ArrayList.
   * @param filename
   */
  private void write(String filename)
  {
    ClassLoader classLoader = getClass().getClassLoader();
    InputStream file = (classLoader.getResourceAsStream(filename));
    Scanner scan = new Scanner(file);
    
    while(scan.hasNext()) dictionary.add(scan.nextLine());
  }
}
