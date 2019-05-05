/* Jacob Hurst.
 * Implementation of Conway's Game Of Life to familiarize
 * myself with JavaFX.
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

public class GameOfLife extends Application
{
  private static final int DRAW_WIDTH  = 800;
  private static final int DRAW_HEIGHT = 600;
  
  private Animation myAnimation;
  private Canvas canvas;
  private GraphicsContext gtx;
  private Group root;
  private Stage stage;
  
  private Random random;
  
  private static final int CELL_SIZE = 10;
  private static final int GRID_WIDTH = DRAW_WIDTH/CELL_SIZE;
  private static final int GRID_HEIGHT = DRAW_HEIGHT/CELL_SIZE;
  private int framesPerSecond;
  private int count, n;
  private int x1, x2;
  private int y1, y2;
  
  private final int[][] grid = new int[GRID_WIDTH][GRID_HEIGHT];
  private final int[][] tempGrid = new int[GRID_WIDTH][GRID_HEIGHT];
  
  private static final double NANO = 0.000000001;
  private double currentTimeInSec;
  private double lastUpdateSec = 0;
  private double deltaTime;
  
  @Override
  public void start(Stage stage) throws Exception
  {
    this.stage = stage;
    
    root = new Group();
    
    canvas = new Canvas(DRAW_WIDTH, DRAW_HEIGHT);
    
    gtx = canvas.getGraphicsContext2D();
    gtx.setFill(Color.WHITE);
    gtx.fillRect(0, 0, DRAW_WIDTH, DRAW_HEIGHT);
    
    root.getChildren().addAll(canvas);
    
    Scene scene = new Scene(root, DRAW_WIDTH, DRAW_HEIGHT);
    
    stage.setScene(scene);
    stage.show();
    
    random = new Random();
    
    setup();

    myAnimation = new Animation();
    myAnimation.start();
  }
  
  private void setup()
  {
    for (int y=0; y<GRID_HEIGHT; y++)
    {
      for (int x=0; x<GRID_WIDTH; x++)
      {
        if (random.nextBoolean()) grid[x][y] = 1;
        else grid[x][y] = 0;
      }
    }
  }
  
  class Animation extends AnimationTimer
  {
    @Override
    public void handle(long now)
    {
      currentTimeInSec = now*NANO;
      deltaTime = currentTimeInSec - lastUpdateSec;
      lastUpdateSec = currentTimeInSec;
      framesPerSecond = (int)(1.0/deltaTime);
      stage.setTitle("FPS: "+ framesPerSecond);
      
      update();
      commit();
    }
  }
  
  private void update()
  {
    for (int x=0; x<GRID_WIDTH; x++)
    {
      for (int y=0; y<GRID_HEIGHT; y++)
      {
        n = countNeighbors(x,y);
        if (grid[x][y] == 1 && n < 2) tempGrid[x][y] = 0;
        else if ((grid[x][y] == 1 && n == 2) || (grid[x][y] == 1 && n == 3)) tempGrid[x][y] = 1;
        else if (grid[x][y] == 1 && n > 3) tempGrid[x][y] = 0;
        else if (grid[x][y] == 0 && n == 3) tempGrid[x][y] = 1;
        else tempGrid[x][y] = 0;
      }
    }
  }
  
  private void commit()
  {
    for (int x=0; x<GRID_WIDTH; x++)
    {
      for (int y=0; y<GRID_HEIGHT; y++)
      {
        grid[x][y] = tempGrid[x][y];
      
        if (grid[x][y] == 1) gtx.setFill(Color.MISTYROSE);
        else gtx.setFill(Color.BLACK);
      
        gtx.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
      }
    }
  }
  
  private int countNeighbors(int x, int y)
  {
    count = 0;
    x1 = x-1;
    if (x1 < 0) x1 = GRID_WIDTH-1;
    
    x2 = (x+1) % (GRID_WIDTH-1);
    
    y1 = y-1;
    if (y1 < 0) y1 = GRID_HEIGHT-1;
  
    y2 = (y + 1) % (GRID_HEIGHT - 1);
    
    if (grid[x1][y1] == 1) count++;
    if (grid[x1][y] == 1) count++;
    if (grid[x1][y2] == 1) count++;
    
    if (grid[x2][y1] == 1) count++;
    if (grid[x2][y] == 1) count++;
    if (grid[x2][y2] == 1) count++;
    
    if (grid[x][y1] == 1) count++;
    if (grid[x][y2] == 1) count++;
    
    return count;
  }

  public static void main(String[] args)
  {
    launch(args);
  }
}
