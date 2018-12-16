package Simulator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Main extends Application {

    //IF USING JAVA 11:
    //Step 1 - File -> Project Structure -> make sure lib is in there.
    //Step 2 - Build project, then go to Run -> Edit Configurations -> under VM Options paste next line
    //         --module-path="/usr/local/javafx-sdk-11.0.1/lib" --add-modules=javafx.controls

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Just setup stuff, nothing important to development.
        primaryStage.setTitle("Manager Terminal");
        ScrollPane mainScroll = new ScrollPane();

        Controller controller = new Controller(mainScroll);
        controller.start();

        primaryStage.setScene(new Scene(mainScroll, 820, 960, Color.BLACK));
        primaryStage.show();

        // side panel for testing the GUI
        //new TestSim(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }


}
