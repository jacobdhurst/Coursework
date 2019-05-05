package ElevatorSystem.unused;

import ElevatorSystem.unused.PositionBarcode;

import static ElevatorSystem.SystemValues.*;

public class ElevatorShaft
{
  private final PositionBarcode[][] barcodes;

  public ElevatorShaft()
  {
    PositionBarcode[][] barcodes = new PositionBarcode[2][numOfFloors];
    for(int i = 0; i < numOfFloors; i++)
    {
      barcodes[0][i] = new PositionBarcode(floorIDs[i],floorPos[i]);
    }
    for(int i = 0; i < numOfFloors; i++)
    {
      barcodes[1][i] = new PositionBarcode(floorIDs[i],floorPos[i]+CabinHeight);
    }
    this.barcodes = barcodes;
  }

  int[] checkPosition(int position,int location)
  {
    if(location == top && topBarCode-variance <= position && position <= topBarCode+variance)
    {
      return(new int[]{numOfFloors+1,position - topBarCode});
    }
    else if(location == bottom && bottomBarCode-variance <= position && position <= bottomBarCode+variance)
    {
      return(new int[]{0,position - bottomBarCode});
    }
    for(int i = 0; i < numOfFloors; i++)
    {
      PositionBarcode barcode = barcodes[location][i];
      if(barcode.position-variance <= position && position <= barcode.position+variance)
      {
        return(new int[]{barcode.floorid,position - barcode.position});
      }
    }
    return new int[]{-1};
  }
}
