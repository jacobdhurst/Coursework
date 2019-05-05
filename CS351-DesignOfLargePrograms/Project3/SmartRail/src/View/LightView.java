/*
 * CS351L Project #2: SmartRail
 * Jacob Hurst & Jaehee Shin
 * 10/25/17
 *
 * LightView.java - View component for lights.
 */

package View;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class LightView
{
  private String lightName = "";
  private final VBox lightBox = new VBox();
  private final Label lightInfo = new Label();
  private ImageView lightImage;
  
  private final Color color[] = { Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
          Color.CORNFLOWERBLUE, Color.MEDIUMPURPLE, Color.PLUM, Color.LIGHTPINK, Color.WHITESMOKE};
  
  /**
   * Default constructor.
   */
  public LightView(int railIndex)
  {
    this.lightInfo.setText(lightName);
    this.lightInfo.setTextFill(color[railIndex]);
    this.lightInfo.setAlignment(Pos.TOP_CENTER);
  
    lightImage = new ImageView();
    lightImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/light_off.png")));
  
    lightBox.getChildren().addAll(lightInfo, lightImage);
    lightBox.setAlignment(Pos.CENTER);
  }
  
  /**
   * @return this light.
   */
  public VBox getComponent()
  {
    return lightBox;
  }
  
  /**
   * Changes light color.
   * @param color
   */
  public void changeLight(String color)
  {
    if(color.equals("red")) lightImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/light_red.png")));
    else lightImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/light_green.png")));
  }
  
  /**
   * Sets the current lock name on light.
   *
   * @param lock
   * @param index
   */
  public void setLock(String lock, int index)
  {
    if(!lightName.equals("")) return;
    lightInfo.setTextFill(color[index]);
    lightInfo.setText(lock);
  }
}
