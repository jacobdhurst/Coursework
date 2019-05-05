/*
 * CS351L Project #2: SmartRail
 * Jacob Hurst & Jaehee Shin
 * 10/25/17
 *
 * TrackView.java - View component for tracks.
 */

package View;

import Controller.ModelController;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class TrackView
{
  private final ModelController modelController;
  private final int railIndex;
  private String endpoint;
  
  private String trackName = "";
  private final VBox trackBox = new VBox();
  private final Label trackInfo = new Label(trackName);
  private final ImageView trackImage;
  
  private final Color color[] = { Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
          Color.CORNFLOWERBLUE, Color.MEDIUMPURPLE, Color.PLUM, Color.LIGHTPINK, Color.WHITESMOKE};
  /**
   * Default constructor.
   */
  public TrackView(ModelController modelController, int railIndex)
  {
    this.modelController = modelController;
    this.railIndex = railIndex;
    
    this.trackInfo.setText(trackName);
    this.trackInfo.setTextFill(color[railIndex]);
    this.trackInfo.setAlignment(Pos.TOP_CENTER);
  
    trackImage = new ImageView();
    trackImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/track.png")));
  
    trackBox.getChildren().addAll(trackInfo, trackImage);
    trackBox.setAlignment(Pos.CENTER);
  }
  
  /**
   * @return view component for track.
   */
  public VBox getComponent() { return trackBox; }
  
  /**
   * Sets the endpoint this station is on.
   * @param endpoint
   */
  public void setEndpoint(String endpoint)
  {
    this.endpoint = endpoint;
  }
  
  /**
   * Sets the track station and adds a train if clicked.
   * @param name
   */
  public void setStation(String name)
  {
    trackName = name;
    trackInfo.setText(trackName);
    trackInfo.setTextFill(color[8]);
    this.trackInfo.setOnMouseClicked(event -> modelController.startTrain(railIndex, endpoint));
  }
  
  /**
   * Disables or enables station click listener depending on flag.
   * @param flag
   */
  public void stationDisabled(boolean flag)
  {
    this.trackInfo.setDisable(flag);
  }
  
  /**
   * Sets the current lock name on track.
   *
   * @param lock
   * @param index
   */
  public void setLock(String lock, int index)
  {
    if(!trackName.equals("")) return;
    trackInfo.setTextFill(color[index]);
    trackInfo.setText(lock);
  }
}
