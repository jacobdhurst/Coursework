/*
 * CS351L Project #2: SmartRail
 * Jacob Hurst & Jaehee Shin
 * 11/01/17
 *
 * TrainView.java - View component for trains.
 */

package View;

import Model.Track;
import javafx.geometry.Point3D;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TrainView
{
  private Stage prompt;
  private String arrival;
  private Text dialog;
  private TextField destination;
  
  private final AudioClip trainHorn = new AudioClip(getClass().getResource("/audio/trainHorn.mp3").toExternalForm());
  private ImageView trainImage;
  private int trainX, trainY;
  private final int destinationX;

  private Track track;
  private final int index;
  
  /**
   * Default constructor.
   *
   * @param destinationX
   */
  public TrainView(int destinationX, Track track, int index)
  {
    this.destinationX = destinationX;
    this.trainX = 0;
    this.trainY = 0;
    this.track = track;
    this.index = index;
    
    setComponent();
  }
  
  /**
   * @return view component for the train.
   */
  public ImageView getComponent()
  {
    return trainImage;
  }
  
  /**
   * @return name of destination entered in prompt.
   */
  public String getArrival()
  {
    return arrival;
  }
  
  /**
   * Sets the current track for this train view
   */
  public void setCurrentTrack(Track track)
  {
    this.track = track;
  }
  
  /**
   * @return current track of train.
   */
  public Track getCurrentTrack()
  {
    return track;
  }
  
  /**
   * @return whether this train is at and endpoint.
   */
  public boolean isEndPoint()
  {
    return trainX == destinationX || trainX == 100;
  }
  
  /**
   * Sets the trains initial position and calls prompt.
   *
   * @param trainX
   */
  public void setTrainX(int trainX)
  {
    this.trainX = trainX;
    trainImage.setTranslateX(trainX);
    if(trainX != 0) trainImage.setRotate(180);
    prompt();
  }
  
  /**
   * Sets the trains visibility.
   * @param flag
   */
  public void setVisible(boolean flag)
  {
    trainImage.setVisible(flag);
    
    if(flag) trainHorn.play();
  }
  
  /**
   * Prompts user for a destination.
   */
  private void prompt()
  {
    dialog = new Text("Hello, where am I heading?");
    dialog.setTextAlignment(TextAlignment.CENTER);
    dialog.setFill(Color.WHITESMOKE);
  
    destination = new TextField();
    destination.setOnKeyPressed(e ->
    {
      if(e.getCode() == KeyCode.ENTER)
      {
        arrival = destination.getText();
        prompt.close();
      }
    });
    
    VBox container = new VBox(dialog, destination);
    container.setStyle("-fx-background: #3B3B3B;");
    
    prompt = new Stage();
    
    prompt.setTitle("Message from train...");
    prompt.setScene(new Scene(container));
    prompt.setWidth(300);
    prompt.initModality(Modality.APPLICATION_MODAL);
    prompt.setOnCloseRequest(e ->
            {
              arrival = "CANCEL";
              trainImage.setVisible(false);
            });
    prompt.showAndWait();
  }
  
  /**
   * Informs user that route could not be found.
   */
  public void error()
  {
    dialog.setText("I couldn't find a route to \"" + arrival + "\".\nThe route may be busy, try again?");
    prompt.setTitle("ERROR");
    prompt.showAndWait();
  }
  
  /**
   * Changes orientation of train.
   *
   * @param direction
   */
  public void changeDirection(String direction)
  {
    if(direction.equals("L")) trainImage.setRotationAxis(new Point3D(0, 0.5, 0));
    else if(direction.equals("R")) trainImage.setRotationAxis(new Point3D(0, 0.0, 0));
    trainImage.setRotate(180);
  }
  
  /**
   * Handles movement of train.
   *
   * @param direction
   */
  public void move(String direction)
  {
    switch(direction)
    {
      case "": return;
      case "U":
        trainY = trainY - 70;
        break;
      case "R":
        trainX = trainX + 100;
        break;
      case "D":
        trainY = trainY + 70;
        break;
      case "L":
        trainX = trainX - 100;
        break;
    }
    trainImage.setTranslateX(trainX);
    trainImage.setTranslateY(trainY);
  }
  
  /**
   * Sets the imageView for the train.
   */
  private void setComponent()
  {
    trainImage = new ImageView();
    switch (this.index)
    {
      case 0:
        trainImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/train_red.png")));
        break;
      case 1:
        trainImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/train_orange.png")));
        break;
      case 2:
        trainImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/train_gold.png")));
        break;
      case 3:
        trainImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/train_green.png")));
        break;
      case 4:
        trainImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/train_blue.png")));
        break;
      case 5:
        trainImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/train_purple.png")));
        break;
      case 6:
        trainImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/train_lightpurple.png")));
        break;
      case 7:
        trainImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/train_pink.png")));
        break;
      default:
        break;
    }
    trainImage.setVisible(false);
  }
}
