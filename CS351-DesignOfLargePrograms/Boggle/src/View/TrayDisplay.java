/*
 * CS351L Project #2: Boggle
 * Jacob Hurst
 * 09/16/17
 *
 * TrayDisplay.java - Tray aspect of display.
 */

package View;

import Controller.ViewController;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class TrayDisplay
{
  private final ViewController viewController;
  private final StackPane layers;
  private final GraphicsContext gtx;
  private final Button[][] letters;
  
  private boolean dragging = false;
  
  private final int dimensions;
  private int prefSize;
  private int x1, y1, x2, y2;
  
  private String cssInitial, cssSelected;
  private String file, word;
  
  /**
   * Default Constructor.
   * @param dimensions
   * @param trayLetters
   */
  public TrayDisplay(ViewController viewController, int dimensions, char[][] trayLetters)
  {
    this.viewController = viewController;
    this.dimensions = dimensions;
    
    layers = new StackPane();
  
    GridPane tray = new GridPane();
    tray.setPrefSize(500, 500);
  
    Canvas canvas = new Canvas(500, 500);
    
    gtx = canvas.getGraphicsContext2D();
    gtx.setFill(Color.TRANSPARENT);
    gtx.fillRect(0,0, 500, 500);
    
    letters = new Button[dimensions][dimensions];
    
    format(dimensions);
    for(int r = 0; r < dimensions; r++)
    {
      for(int c = 0; c < dimensions; c++)
      {
        char temp = Character.toUpperCase(trayLetters[r][c]);
        letters[r][c] = setButton(letters[r][c], temp);
        tray.add(letters[r][c], c, r);
      }
    }
    layers.getChildren().addAll(canvas, tray);
  }
  
  /**
   * @return the StackPane containing tray & canvas.
   */
  public StackPane getTray() {
    return layers;
  }
  
  /**
   * @return current word if in drag, empty string otherwise.
   */
  public String currentWord()
  {
    if(dragging) return word;
    else return "";
  }
  
  /**
   * Handles the set up of each button
   * @param letter
   * @param c
   * @return
   */
  private Button setButton(Button letter, char c)
  {
    Insets insets = new Insets(5, 5, 5, 5);
    
    letter = new Button(String.valueOf(c));
    letter.setPrefSize(prefSize, prefSize);
    letter.setId(String.valueOf(Character.toLowerCase(c)));
    
    GridPane.setMargin(letter, insets);
    letter.setStyle(cssInitial);
    
    letter.setOnDragDetected(this::dragStarted);
    letter.setOnMouseDragOver(this::draggedOver);
    letter.setOnMouseReleased(this::dragEnded);
    
    return letter;
  }
  
  /**
   * Handles the start of a drag.
   * @param event
   */
  private void dragStarted(MouseEvent event)
  {
    dragging = true;
    Node currentNode = (Node) event.getSource();
    currentNode.startFullDrag();
    word = "" + currentNode.getId();
    disable(currentNode);
    
    x1 = (int)currentNode.getLayoutX()+prefSize/2;
    y1 = (int)currentNode.getLayoutY()+prefSize/2;
  }
  
  /**
   * Handles the dragging across nodes.
   * @param event
   */
  private void draggedOver(MouseEvent event)
  {
    Node currentNode = (Node) event.getSource();
    word += currentNode.getId();
    disable(currentNode);
  
    x2 = (int) currentNode.getLayoutX() + prefSize / 2;
    y2 = (int) currentNode.getLayoutY() + prefSize / 2;
    
    gtx.setFill(Color.SLATEBLUE);
    gtx.fillOval(x1-7, y1-7, 14, 14);
    gtx.fillOval(x2 -7, y2 -7, 14, 14);
  
    gtx.setLineWidth(5);
    gtx.setStroke(Color.SLATEBLUE);
    gtx.strokeLine(x1, y1, x2, y2);
    
    x1 = x2;
    y1 = y2;
  }
  
  /**
   * Handles the end of a drag.
   * @param event
   */
  private void dragEnded(MouseEvent event)
  {
    dragging = false;
    viewController.handleWord(word);
    enable();
  
    gtx.clearRect(0, 0, 500, 500);
  }
  
  /**
   * Updates file name as needed (used in styling of buttons).
   * @param option
   */
  private void format(int option)
  {
    if(option == 0)
    {
      file = file.substring(0, 3);
      file += "s.png";
    }
    if(option == 1)
    {
      file = file.substring(0, 3);
      file += "i.png";
    }
    else if(option == 4)
    {
      prefSize = 115;
      file = "115i.png";
    }
    else if(option == 5)
    {
      prefSize = 90;
      file = "090i.png";
    }
    cssInitial = "-fx-background-color: #E8E8E8;" +
            "-fx-background-image: url('/Resources/"+file+"');" +
            "-fx-border-color: #E8E8E8;" +
            "-fx-border-width: 5;" +
            "-fx-border-radius: 15;" +
            "-fx-font-size: "+prefSize/dimensions+"px;" +
            "-fx-text-fill: #F8F8F8;";
    cssSelected = "-fx-background-color: #E8E8E8;" +
            "-fx-border-color: #E8E8E8;" +
            "-fx-border-width: 10;" +
            "-fx-border-radius: 15;" +
            "-fx-background-image: url('/Resources/"+file+"');" +
            "-fx-font-size: "+prefSize/(dimensions-1)+"px;" +
            "-fx-text-fill: #F8F8F8;";
  }
  
  /**
   * Updates the node to be selected & disabled.
   * @param currentNode
   */
  private void disable(Node currentNode)
  {
    format(0);
    currentNode.setStyle(cssSelected);
    currentNode.setDisable(true);
  }
  
  /**
   * Enables every node on the tray.
   */
  private void enable()
  {
    format(1);
    for(int r = 0; r < dimensions; r++)
    {
      for(int c = 0; c < dimensions; c++)
      {
        letters[r][c].setDisable(false);
        letters[r][c].setStyle(cssInitial);
      }
    }
  }
}
