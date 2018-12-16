/*
 * CS351L Project #2: SmartRail
 * Jacob Hurst & Jaehee Shin
 * 10/24/17
 *
 * ViewController.java - Controls the various aspects and components of the view.
 */

package Controller;

import Model.*;
import View.LightView;
import View.SwitchView;
import View.TrackView;
import View.TrainView;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class ViewController extends Application
{
  private final Scene scene = new Scene(new Group());

  private Stage setup;
  private Stage builder;

  private ScrollPane simulation;

  private Configuration configuration;
  private ModelController modelController;
  private ArrayList<Train> trains;
  private ArrayList<Track> stations;
  private Rail[] rails;

  private VBox configurationBox;

  private boolean clicked;
  private boolean valid;

  private TextField customRails[][];
  private final String[][] grid = new String[8][3];

  private final Label elapsed = new Label("");
  private final Label activeTrains = new Label("");
  private final Label currentMessages = new Label("Click on a station to add a train!");
  private Button start, custom, submit, pause, resume;
  private final Button[] load = {
          new Button("Tracks Only (S)"),
          new Button("Tracks Only (L)"),
          new Button("Full (S)"),
          new Button("Full (L)")
  };

  private final int WINDOW_HEIGHT = 500;
  private final int WINDOW_WIDTH = 1000;
  private final int BUTTON_W = 125;
  private final int BUTTON_H = 25;
  private int height = 0;

  private AnimationTimer animationTimer;
  private PauseTransition timer;

  private final ClassLoader classLoader = getClass().getClassLoader();
  private final InputStream[] file = {
          (classLoader.getResourceAsStream("configurations/tracks_S.txt")),
          (classLoader.getResourceAsStream("configurations/tracks_L.txt")),
          (classLoader.getResourceAsStream("configurations/full_S.txt")),
          (classLoader.getResourceAsStream("configurations/full_L.txt"))
  };

  /**
   * Application start method. Sets up the simulation display.
   *
   * @param stage
   */
  @Override
  public void start(Stage stage)
  {
    stage.setTitle("SmartRail");
    scene.setFill(Color.valueOf("#2F2F2F"));

    configurationBox = new VBox();
    configurationBox.setPrefSize(WINDOW_WIDTH - 100, WINDOW_HEIGHT - 40);
    configurationBox.setAlignment(Pos.CENTER);
    configurationBox.setStyle("-fx-background-color: #2F2F2F;");

    simulation = new ScrollPane();
    simulation.setContent(configurationBox);
    simulation.getStylesheets().add("css/scroll-bar.css");

    elapsed.setWrapText(true);
    elapsed.setPrefWidth(150);
    elapsed.setPrefHeight(75);
    elapsed.setTextFill(Color.RED);

    activeTrains.setWrapText(true);
    activeTrains.setPrefWidth(100);
    activeTrains.setPrefHeight(75);
    activeTrains.setTextFill(Color.WHITESMOKE);

    currentMessages.setWrapText(true);
    currentMessages.setPrefWidth(100);
    currentMessages.setPrefHeight(75);
    currentMessages.setTextFill(Color.WHITESMOKE);
    currentMessages.setVisible(false);

    pause = new Button("Pause");
    pause.setPrefSize(BUTTON_W, BUTTON_H);
    pause.setOnMouseClicked(this::setOnMouseClicked);

    resume = new Button("Resume");
    resume.setPrefSize(BUTTON_W, BUTTON_H);
    resume.setOnMouseClicked(this::setOnMouseClicked);
    resume.setDisable(true);

    VBox informationBox = new VBox();
    informationBox.setSpacing(10);
    informationBox.setPadding(new Insets(2.5, 5, 2.5, 5));
    informationBox.setPrefSize(100, WINDOW_HEIGHT);
    informationBox.setAlignment(Pos.CENTER_LEFT);
    informationBox.getChildren().addAll(elapsed, activeTrains, currentMessages);

    HBox controlBox = new HBox();
    controlBox.setSpacing(10);
    controlBox.setPadding(new Insets(2.5, 5, 2.5, 5));
    controlBox.setPrefSize(WINDOW_WIDTH - 100, 40);
    controlBox.setAlignment(Pos.CENTER);
    controlBox.getChildren().addAll(pause, resume);

    BorderPane container = new BorderPane();
    container.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    container.setCenter(simulation);
    container.setBottom(controlBox);
    container.setRight(informationBox);

    ((Group) scene.getRoot()).getChildren().addAll(container);
    stage.setScene(scene);
    stage.show();
    stage.setOnCloseRequest(e ->
    {
      Platform.exit();
      System.exit(0);
    });

    setup();
  }

  /**
   * Displays a setup dialog box.
   */
  private void setup()
  {
    Text dialog = new Text("Welcome to SmartRail!\nPlease select your configuration!");
    dialog.setTextAlignment(TextAlignment.CENTER);
    dialog.setFill(Color.WHITESMOKE);

    VBox optionBox = new VBox(10);
    optionBox.getChildren().add(dialog);
    optionBox.setAlignment(Pos.CENTER);

    for (int i = 0; i < 4; i++)
    {
      load[i].setPrefSize(BUTTON_W, BUTTON_H);
      load[i].setOnMouseClicked(this::setOnMouseClicked);
      optionBox.getChildren().add(load[i]);
    }

    clicked = false;
    custom = new Button("Custom");
    custom.setPrefSize(BUTTON_W, BUTTON_H);
    custom.setOnMouseClicked(this::setOnMouseClicked);

    start = new Button("Start");
    start.setPrefSize(BUTTON_W, BUTTON_H);
    start.setOnMouseClicked(this::setOnMouseClicked);

    optionBox.getChildren().addAll(custom, start);

    BorderPane loader = new BorderPane();
    loader.setStyle("-fx-background: #3B3B3B;");
    loader.setCenter(optionBox);

    Scene load = new Scene(loader);

    setup = new Stage();
    setup.setWidth(300);
    setup.setHeight(300);
    setup.setTitle("SmartRail: Loader");
    setup.setScene(load);
    setup.setOnCloseRequest(e ->
    {
      Platform.exit();
      System.exit(0);
    });
    setup.show();
  }

  /**
   * Displays a custom configuration builder.
   * This builder is not complete yet but it offers some fun testing.
   */
  private void builder()
  {
    /* note: the lengths could be longer than 8 tracks but for simplicity, we set a limit to 8. */
    Text instructions = new Text("Each row represents a rail, with station names on the endpoints.\n" +
            "To configure a rail type a sequence of 'T', 'L', & 'S' characters.\n" +
            "'T' signifies a track, 'L' signifies a light, & 'S' signifies a switch.\n" +
            "To configure a rail without a station at one endpoint, just empty station name field!\n\n" +
            "Minimum length = 2, Maximum length = 8.\n" +
            "Switches must have connection set EITHER above or below.\n" +
            "Invalid customizations result in default configuration loading.\n" +
            "Invalid characters entered will be treated as tracks.");
    instructions.setFill(Color.WHITESMOKE);

    customRails = new TextField[8][3];

    HBox lines[] = new HBox[8];

    VBox customFrame = new VBox();
    customFrame.getChildren().add(instructions);
    customFrame.setStyle("-fx-background: #2F2F2F;");

    int index = 0;
    for (TextField t[] : customRails)
    {
      t[0] = new TextField("Station" + (index));
      t[1] = new TextField("TT");
      t[2] = new TextField("Station" + (index + 1));
      
      lines[index / 2] = new HBox();
      lines[index / 2].getChildren().addAll(t[0], t[1], t[2]);
      customFrame.getChildren().add(lines[index / 2]);

      index += 2;
    }

    valid = true;
    submit = new Button("Start");
    submit.setPrefSize(BUTTON_W, BUTTON_H);
    submit.setOnMouseClicked(this::setOnMouseClicked);

    customFrame.getChildren().add(submit);
    customFrame.setAlignment(Pos.CENTER);

    builder = new Stage();
    builder.setTitle("SmartRail: Custom");
    builder.setScene(new Scene(customFrame));
    builder.setOnCloseRequest(event ->
    {
      Platform.exit();
      System.exit(0);
    });
    builder.show();
  }

  /**
   * Builds the custom configuration.
   * If errors are detected in the sizing of configuration e.g. too big (width > 8) or too small (width < 2),
   * the default configuration gets loaded instead.
   */
  private void buildCustom()
  {
    int index = 0;
    for (TextField t[] : customRails)
    {
      if (t[1].getText().length() > 8 || t[1].getText().length() < 2)
      {
        valid = false;
        return;
      }
      
      if(t[0].getText().equals("")) t[0].setText("NO STATION");
      if(t[2].getText().equals("")) t[2].setText("NO STATION");
      
      grid[index][0] = t[0].getText();
      grid[index][1] = t[1].getText().toUpperCase();
      grid[index][2] = t[2].getText();
      
      index++;
    }
  }

  /**
   * Handler for button clicks.
   *
   * @param event
   */
  private void setOnMouseClicked(Event event)
  {
    Object source = event.getSource();
    for(int i = 0; i < 4; i++)
    {
      if(source == load[i])
      {
        configurationBox.getChildren().clear();
        simulation.setContent(configurationBox);

        configuration = new Configuration(file[i], i);
        height = configuration.getHeight();

        loadSimulation(configuration);
      }
      else if(source == custom && !clicked)
      {
        /* was getting strange issue of custom being too sensitive and opening 4 builder windows */
        clicked = true;

        configurationBox.getChildren().clear();
        simulation.setContent(configurationBox);

        setup.close();
        builder();
      }
      else if(source == submit)
      {
        configurationBox.getChildren().clear();
        simulation.setContent(configurationBox);

        buildCustom();

        if(valid) configuration = new Configuration(grid);
        else configuration = new Configuration(file[0], 0);
        height = configuration.getHeight();

        loadSimulation(configuration);
  
        for(Track track : stations) track.getTrackView().stationDisabled(false);
        
        animationTimer.start();
        startTimer();

        currentMessages.setVisible(true);
        pause.setDisable(true);
        /* was getting strange issue of pause not working within custom configurations from builder.
         * for some reason, animationTimer.stop() would not pause the simulation even though the timer would stop,
         * (still works w/ any config loaded via file).*/

        builder.close();
      }
      else if(source == start && configuration != null)
      {
        animationTimer.start();
        startTimer();

        currentMessages.setVisible(true);
  
        for(Track track : stations) track.getTrackView().stationDisabled(false);
        
        setup.close();
      }
      else if(source == pause)
      {
        animationTimer.stop();
        timer.pause();

        pause.setDisable(true);
        resume.setDisable(false);
      }
      else if(source == resume)
      {
        animationTimer.start();
        timer.play();

        pause.setDisable(false);
        resume.setDisable(true);
      }
    }
  }

  /**
   * Starts a timer for elapsed time.
   */
  private void startTimer()
  {
    timer = new PauseTransition(Duration.INDEFINITE);
    timer.playFrom(Duration.ZERO);
  }

  /**
   * loads a simulation given some configuration.
   *
   * @param configuration
   */
  private void loadSimulation(Configuration configuration)
  {
    modelController = new ModelController(configuration);
    trains = new ArrayList<>();
    stations = new ArrayList<>();
    trains = modelController.getTrains();
    rails = modelController.getRails();

    setupViewComponents();

    animationTimer = new AnimationTimer()
    {
      @Override
      public void handle(long now)
      {
        elapsed.setText((String.format("%.2f", timer.getCurrentTime().toSeconds()) + "s elapsed"));
        activeTrains.setText(modelController.getActiveCount() + " trains active.");
        runSimulation();
      }
    };
  }

  /**
   * Sets up view component for each thread.
   */
  private void setupViewComponents()
  {
    HBox[] railBox = new HBox[height];

    for(int i = 0; i < height; i++)
    {
      HBox lane = new HBox();

      Train train = trains.get(i);
      Rail rail = rails[i];
      Track[] track = rail.getTracks();

      for(int j = 0; j < rails[i].getTracks().length; j++)
      {
        if(track[j] instanceof Light)
        {
          LightView lightView = new LightView(i);
          lane.getChildren().add(lightView.getComponent());

          ((Light)track[j]).setLightView(lightView);
        }
        else if(track[j] instanceof Switch)
        {
          SwitchView switchView = new SwitchView(i);
          lane.getChildren().add(switchView.getComponent());

          ((Switch)track[j]).setSwitchView(switchView);
        }
        else
        {
          TrackView trackView = new TrackView(modelController, i);

          if(track[j].isEndPoint("L"))
          {
            if(!track[j].getName().equals("NO STATION")) trackView.setStation(track[j].getName());
            trackView.stationDisabled(true);
            trackView.setEndpoint("L");
            stations.add(track[j]);
          }
          else if(track[j].isEndPoint("R"))
          {
            if(!track[j].getName().equals("NO STATION")) trackView.setStation(track[j].getName());
            trackView.stationDisabled(true);
            trackView.setEndpoint("R");
            stations.add(track[j]);
          }

          lane.getChildren().add(trackView.getComponent());

          track[j].setTrackView(trackView);
        }
      }

      TrainView trainView = new TrainView(track.length * 100, track[0], i);

      train.setTrainView(trainView);

      lane.getChildren().add(0, trainView.getComponent());
      HBox.setMargin(lane.getChildren().get(0), new Insets(7, 30, 0, 0));

      configurationBox.getChildren().add(lane);
      VBox.setMargin(lane, new Insets(10, 0, 10, 0));

      railBox[i] = lane;
    }
  }

  /**
   * Get's called by animationTimer and polls for updates from threads, animating changes when needed.
   */
  private void runSimulation()
  {
    for(Train train : modelController.getTrains())
    {
      if(train.receivedMessage())
      {
        synchronized(train)
        {
          currentMessages.setText(train.getMessage());
          updateTrainView(train);
          train.notify();
  
          try
          {
            sleep(125);
          }
          catch (InterruptedException ignored) {}
        }
      }
    }

    for(Rail rail : rails)
    {
      for(Track track : rail.getTracks())
      {
        updateTrackView(track);
        if(track.receivedMessage())
        {
          currentMessages.setText(track.getMessage());
          synchronized(track)
          {
            track.notify();
          }
        }
      }
    }
  }
  
  /**
   * Helper function, updates the train view component periodically.
   *
   * @param train
   */
  private void updateTrainView(Train train)
  {
    if(train.getTranslate() != null && train.isMoved())
    {
      if(train.getTrainView().getCurrentTrack().getNeighbor(train.getTranslate()) == null)
      {
        train.getTrainView().setCurrentTrack(train.getTrainView().getCurrentTrack());
      }
      else
      {
        train.getTrainView().setCurrentTrack(train.getTrainView().getCurrentTrack().getNeighbor(train.getTranslate()));
        train.getTrainView().move(train.getTranslate());
      }
    }
  }
  
  /**
   * Helper function, updates the track view component periodically.
   *
   * @param track
   */
  private void updateTrackView(Track track)
  {
    if(track instanceof Light)
    {
      if(track.getTrain() == null) ((Light)track).getLightView().setLock("", 8);
      else ((Light)track).getLightView().setLock(track.getLock(), track.getTrain().getIndex());
    }
    else if(track instanceof Switch)
    {
      if(track.getTrain() == null) ((Switch)track).getSwitchView().setLock("", 8);
      else ((Switch)track).getSwitchView().setLock(track.getLock(), track.getTrain().getIndex());
    }
    else
    {
      if(track.getTrain() == null) track.getTrackView().setLock("", 8);
      else track.getTrackView().setLock(track.getLock(), track.getTrain().getIndex());
    }
  }
}
