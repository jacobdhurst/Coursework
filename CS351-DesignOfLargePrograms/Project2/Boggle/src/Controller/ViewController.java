/*
 * CS351L Project #2: Boggle
 * Jacob Hurst
 * 09/16/17
 *
 * ViewController.java - Controls which aspects of display are shown and handles input.
 */

package Controller;

import Model.Timer;
import View.TrayDisplay;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;

public class ViewController implements EventHandler<ActionEvent>
{
  private ModelController modelController;
  private TrayDisplay trayDisplay;
  
  private Stage end;
  private BorderPane root;
  
  private VBox optionBox;
  private HBox timeBox;
  
  private TextField timeField;
  private Button newGame4x4;
  private Button newGame5x5;
  
  private Label gameLabel;
  private Label timerLabel;
  private Label scoreLabel;
  private Label correctLabel;
  private Label incorrectLabel;
  
  private TextField input;
  private String word = "";
  private String correctList;
  private String incorrectList;
  private ArrayList<String> incorrect;
  
  private final AudioClip correctSound = new AudioClip(this.getClass().getResource(
                                                 "/Resources/correctSound.mp3").toExternalForm());
  private final AudioClip incorrectSound = new AudioClip(this.getClass().getResource(
                                                   "/Resources/incorrectSound.mp3").toExternalForm());
  private final AudioClip endSound = new AudioClip(this.getClass().getResource(
                                             "/Resources/endSound.mp3").toExternalForm());
  
  /**
   * Initializes the display to the start screen.
   * @return
   */
  public Parent initialize()
  {
    root = new BorderPane();
  
    newGame4x4 = new Button("New 4x4");
    newGame4x4.setOnAction(this);
    newGame4x4.setPrefSize(100, 50);
  
    newGame5x5 = new Button("New 5x5");
    newGame5x5.setOnAction(this);
    newGame5x5.setPrefSize(100, 50);
  
    gameLabel = new Label("Welcome to Boggle!");
    gameLabel.setAlignment(Pos.CENTER);
    Label timeLabel = new Label("Set the duration (in seconds):");
    timeLabel.setAlignment(Pos.CENTER);
    Label infoLabel = new Label("Minimum = 5 seconds\nDefault = 180 seconds\nMaximum = 3600 seconds");
    infoLabel.setAlignment(Pos.CENTER);
    timeField = new TextField("180");
    timeField.setMaxWidth(200);
    timeField.setAlignment(Pos.CENTER);
    
    HBox hBox = new HBox(15);
    hBox.getChildren().addAll(newGame4x4, newGame5x5);
    hBox.setAlignment(Pos.CENTER);
  
    optionBox = new VBox(15);
    optionBox.setPadding(new Insets(0, 10, 5, 10));
    optionBox.getChildren().addAll(gameLabel, hBox, timeLabel, timeField, infoLabel);
    optionBox.setAlignment(Pos.CENTER);
    
    root.setCenter(optionBox);
    root.setStyle("-fx-background-color: #E8E8E8;");
    
    return root;
  }
  
  /**
   * Handles the options presented on start screen.
   * @param event
   */
  @Override
  public void handle(ActionEvent event)
  {
    Object source = event.getSource();
    if(end != null)
    {
      /* called in between games */
      root.setDisable(false);
      end.close();
      end = null;
      System.gc();
    }
    if(source == newGame4x4)
    {
      startGame(4);
    }
    else if(source == newGame5x5)
    {
      startGame(5);
    }
  }
  
  /**
   * Handles the display when a word is submitted,
   * sends word to modelController for checking.
   * @param word
   */
  public void handleWord(String word)
  {
    if(modelController.handleWord(word))
    {
      correctSound.play();
      correctList += ("\u2022 " + word + "\n");
      update();
    }
    else if(!modelController.getCorrect().contains(word) && !incorrect.contains(word))
    {
      incorrectSound.play();
      incorrect.add(word);
      incorrectList += ("\u2022 " + word + "\n");
      update();
    }
  }
  
  /**
   * Starts a game given the option selected,
   * sets up all view aspects.
   * @param dimensions
   */
  private void startGame(int dimensions)
  {
    modelController = new ModelController(dimensions);
    
    correctList = "CORRECT:\n";
    incorrectList = "INCORRECT:\n";
    
    timerLabel = new Label();
    correctLabel = new Label(correctList);
    correctLabel.setTextFill(Color.GREEN);
    incorrectLabel = new Label(incorrectList);
    incorrectLabel.setTextFill(Color.RED);
    scoreLabel = new Label("SCORE: ");
    Label inputLabel = new Label("Click & drag on letters to build a word," +
                                 " or just type the word you see here: ");
    
    input = new TextField();
    input.setPrefWidth(250);
    
    incorrect = new ArrayList<>();
    VBox incorrectBox = new VBox();
    incorrectBox.getChildren().add(incorrectLabel);
    incorrectBox.setSpacing(10);
    incorrectBox.setPadding(new Insets(10, 10, 10, 10));
    incorrectBox.setAlignment(Pos.TOP_CENTER);
    incorrectBox.setPrefSize(100, 500);
  
    VBox correctBox = new VBox();
    correctBox.getChildren().add(correctLabel);
    correctBox.setSpacing(10);
    correctBox.setPadding(new Insets(10, 10, 10, 10));
    correctBox.setAlignment(Pos.TOP_CENTER);
    correctBox.setPrefSize(100, 500);
  
    HBox inputBox = new HBox();
    inputBox.getChildren().addAll(inputLabel, input);
    inputBox.setSpacing(10);
    inputBox.setPadding(new Insets(10, 10, 10, 10));
    inputBox.setAlignment(Pos.BOTTOM_LEFT);
    inputBox.setPrefSize(700, 50);
    
    trayDisplay = new TrayDisplay(this, dimensions, modelController.getTray());
    StackPane trayView = trayDisplay.getTray();
    
    startTimer();
    update();
    
    root.setTop(timeBox);
    root.setCenter(trayView);
    root.setLeft(incorrectBox);
    root.setRight(correctBox);
    root.setBottom(inputBox);
    
    play();
  }
  
  /**
   * Sets up and starts the timer and it's label.
   */
  private void startTimer()
  {
    int time;
    try
    {
      time = Integer.parseInt(timeField.getText());
    }
    catch(NumberFormatException e)
    {
      time = 180;
    }
    if(time > 3600) time = 3600;
    if(time < 5) time = 5;
    
    timeBox = new HBox();
    timeBox.setSpacing(300);
    timeBox.setPadding(new Insets(10, 10, 10, 10));
    timeBox.setAlignment(Pos.CENTER);
  
    Timer timer = new Timer(time);
    timer.play(timerLabel);
    
    timeBox.getChildren().addAll(timerLabel, scoreLabel);
    timeBox.setPrefSize(700, 50);
    PauseTransition delay = new PauseTransition(Duration.seconds(time));
    delay.setOnFinished(e -> endGame());
    delay.play();
  }
  
  /**
   * Handles user input and game mechanics,
   * bulk of gameplay occurs here.
   */
  private void play()
  {
    input.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER)
        {
          word = input.getText();
          word = word.toLowerCase();
          handleWord(word);
          input.clear();
          word = "";
        }
      }
    });
  }
  
  /**
   * Updates the display.
   */
  private void update()
  {
    scoreLabel.setText("SCORE: " + modelController.getScore() + " out of " +
                       modelController.getPossibleScore());
    correctLabel.setText(correctList.toUpperCase());
    incorrectLabel.setText(incorrectList.toUpperCase());
  }
  
  /**
   * End game dialog screen.
   * Disables the game, provides dialog w/ final score and list of words
   * that were on the tray, offers play again button which loads game with same settings.
   */
  private void endGame()
  {
    endSound.play();
    
    /* disabling the display */
    root.setDisable(true);
    
    /* handling the last word if timer ends mid-drag */
    String currentWord = trayDisplay.currentWord();
    if(!currentWord.equals("")) handleWord(currentWord);
    
    /* gathering list of all possible words */
    StringBuilder list = new StringBuilder();
    for(String word : modelController.getPossible())
    {
      list.append(" \u2022 ").append(word.toUpperCase()).append("\n");
    }
    Text dialog = new Text("Time has expired!\nYour final score was: " + modelController.getScore() +
            " out of " + modelController.getPossibleScore() + "\nHere is a list of words that were on the tray:\n" + list);
    
    gameLabel.setText("Play Again?");
    
    VBox dialogBox = new VBox(10);
    dialogBox.getChildren().addAll(dialog, optionBox);
    
    ScrollPane scroll = new ScrollPane();
    scroll.setContent(dialogBox);
    scroll.setPadding(new Insets(5, 10, 5, 10));
    scroll.setMaxHeight(600);
    scroll.setPrefWidth(265);
    
    Scene dialogScene = new Scene(scroll);
  
    end = new Stage();
    end.setTitle("Boggle");
    end.setScene(dialogScene);
    end.setOnCloseRequest(e -> Platform.exit());
    end.show();
  }
}