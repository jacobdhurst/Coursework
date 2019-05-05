package ElevatorSystem;

public final class SystemValues
{
  //position values from 1-1001
  //floors spaced 100 apart from eachother
  public static final int bottomBarCode = 1;
  public static final int numOfFloors = 10;
  public static final int numOfElevators = 4;
  public static final int[] floorPos = new int[]{ 51,151,251,351,451,551,651,751,851,951};
  public static final int[] floorIDs = new int[]{1, 2,  3,  4,  5,  6,  7,  8,  9, 10};
  public static final int topBarCode = 1001;

  public static final int floorRequestChance = 90;
  public static final int failureChanceBound = 1000;
  public static final int obstructionChanceBound = 100;
  public static final int weightOverloadChanceBound = 100;

  public static final int failureChance = 1;
  public static final int obstructionChance = 5;
  public static final int weightOverloadChance = 5;

  public static final int alignmentVariance = 5;

  public static final int DoorClosed = 50;
  public static final int CabinStartingPos = floorPos[0];
  public static final int CabinHeight = 75;

  public static final int top = 1;
  public static final int bottom = 0;

  public static final int variance = 25;


}
