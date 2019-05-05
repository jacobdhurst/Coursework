/*
 * CS351L Project #2: SmartRail
 * Jacob Hurst & Jaehee Shin
 * 10/25/17
 *
 * SwitchView.java - View component for switches.
 */

package View;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SwitchView
{
  private String switchName = "";
  private final VBox switchBox = new VBox();
  private final Label switchInfo = new Label();
  private final ImageView switchImage;
  
  private final Color color[] = { Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
          Color.CORNFLOWERBLUE, Color.MEDIUMPURPLE, Color.PLUM, Color.LIGHTPINK, Color.WHITESMOKE};
  
  /**
   * Default constructor.
   */
  public SwitchView(int railIndex)
  {
    this.switchInfo.setText(switchName);
    this.switchInfo.setTextFill(color[railIndex]);
    this.switchInfo.setAlignment(Pos.TOP_CENTER);
  
    switchImage = new ImageView();
    switchImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/switch.png")));
  
    switchBox.getChildren().addAll(switchInfo, switchImage);
    switchBox.setAlignment(Pos.CENTER);
  }
  
  /**
   * @return view component for track.
   */
  public VBox getComponent()
  {
    return switchBox;
  }
  
  /**
   * Sets the current lock name on switch.
   *
   * @param lock
   * @param index
   */
  public void setLock(String lock, int index)
  {
    if(!switchName.equals("")) return;
    switchInfo.setTextFill(color[index]);
    switchInfo.setText(lock);
  }
}
