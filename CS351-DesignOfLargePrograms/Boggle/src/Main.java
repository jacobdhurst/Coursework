/*
 * CS351L Project #2: Boggle
 * Jacob Hurst
 * 09/16/17
 *
 * Main.java - Main entry point for the program.
 */

import Controller.ViewController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{
  @Override
  public void start(Stage stage) throws Exception
  {
    ViewController viewController = new ViewController();
    Parent root = viewController.initialize();
  
    Scene scene = new Scene(root, 700, 600);
    
    stage.setTitle("Boggle");
    stage.setScene(scene);
    stage.setOnCloseRequest(e -> Platform.exit());
    stage.show();
  }
  
  /**
   * @param args
   */
  public static void main(String[] args)
  {
    launch(args);
  }
}