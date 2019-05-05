/*
 * CS351L Project #2: Boggle
 * Jacob Hurst
 * 09/16/17
 *
 * Timer.java - Handles the timer label.
 */

package Model;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Timer
{
  private final Timeline timer;
  private String timeString;
  private int time;
  private int minutes;
  private int seconds;
  
  /**
   * Default constructor.
   */
  public Timer(int time)
  {
    timer = new Timeline();
    timeString = "";
    this.time = time;
    minutes = 0;
    seconds = 0;
  }
  
  /**
   * Starts the timer.
   * @param label
   */
  public void play(Label label)
  {
    converter(time);
    
    timer.setCycleCount(Animation.INDEFINITE);
    timer.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e ->
    {
      seconds--;
      time--;
      if(seconds < 0)
      {
        seconds = 59;
        minutes--;
      }
      if(minutes < 0) minutes = 0;
      
      format(label);
      
      if(time <= 0) timer.stop();
    }));
    timer.playFromStart();
  }
  
  /**
   * Converts the current time (total seconds) into
   * minutes & seconds.
   * @param time
   */
  private void converter(int time)
  {
    this.time = time;
    int temp = time;
    minutes = temp / 60;
    seconds = temp - (minutes * 60);
  }
  
  /**
   * Formats the time display for a timer label.
   * @param label
   */
  private void format(Label label)
  {
    String minutes, seconds;
    if(this.minutes < 10) minutes = "0" + this.minutes;
    else minutes = String.valueOf(this.minutes);
    if(this.seconds < 10) seconds = "0" + this.seconds;
    else seconds = String.valueOf(this.seconds);
    
    timeString = minutes + ":" + seconds;
    label.setText(timeString);
    label.setScaleX(2);
    label.setScaleY(2);
    if(time <= 30) label.setTextFill(javafx.scene.paint.Color.RED);
    else label.setTextFill(javafx.scene.paint.Color.GREEN);
  }
}
