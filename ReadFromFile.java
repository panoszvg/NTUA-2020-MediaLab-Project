import java.io.*;
import java.util.*;

public class ReadFromFile {
  
    public static Integer typeOfShip, i_position, j_position, orientation;
    public static IntPair possiblePositionsErrorIntPair, positionInQuestion;

        /* Function to assist file input
    Source: https://knpcode.com/java-programs/how-to-read-delimited-file-in-java/ 
    */
    public static void parseData(String str) {
        Scanner lineScanner = new Scanner(str);
        lineScanner.useDelimiter(",");
        while (lineScanner.hasNext()) {
            typeOfShip = Integer.parseInt(lineScanner.next());
            i_position = Integer.parseInt(lineScanner.next());
            j_position = Integer.parseInt(lineScanner.next());
            orientation = Integer.parseInt(lineScanner.next());
        }
        lineScanner.close();
    }

    public static void read(Player Player, Grid PlayerGrid, EnemyPlayer EnemyPlayer, Grid EnemyGrid) throws OversizeException, OverlapTilesException {
        Scanner sc = null;
        for(int i=0; i<2; i++)
        try {
            if(i==0)
                sc = new Scanner(new File("/home/panos/Desktop/MediaLab/player_SCENARIO-ID.txt"));
            else 
                sc = new Scanner(new File("/home/panos/Desktop/MediaLab/enemy_SCENARIO-ID.txt"));
            
            while (sc.hasNextLine()) {
                String str = sc.nextLine();
                parseData(str);
                IntPair temp = new IntPair(i_position, j_position); /* is local to while, no need to free memory */
                if(i==0){
                  /* For Player */
                    /* Set ship position (it will be set regardless of whether it is correct -
                    if it throws OversizeException, it will be thrown on Set function) */
                    Player.shipArray[typeOfShip-1].setShipPosition(typeOfShip, temp, orientation);
                    /* Update board in corresponding Grid */
                    for(int j=0; j<Player.shipArray[typeOfShip-1].getOccupyingSpaces(); j++)
                        try{
                            PlayerGrid.Set(Player.shipArray[typeOfShip-1].getShipPosition().get(j).i_pos, 
                            Player.shipArray[typeOfShip-1].getShipPosition().get(j).j_pos, 1);
                        }
                        catch (OversizeException oversizeException){throw oversizeException;}
                        catch (OverlapTilesException overlapTilesException){throw overlapTilesException;}
                }
                else{
                  /* For Enemy */
                    EnemyPlayer.shipArray[typeOfShip-1].setShipPosition(typeOfShip, temp, orientation);
                    for(int j=0; j<EnemyPlayer.shipArray[typeOfShip-1].getOccupyingSpaces(); j++)
                        try{
                            EnemyGrid.Set(EnemyPlayer.shipArray[typeOfShip-1].getShipPosition().get(j).i_pos, 
                            EnemyPlayer.shipArray[typeOfShip-1].getShipPosition().get(j).j_pos, 1);
                        }
                        catch(OversizeException oversizeException){throw oversizeException;}
                        catch (OverlapTilesException overlapTilesException){throw overlapTilesException;}
                }
            }
        } catch (IOException exp){
            exp.printStackTrace();
        } finally{
            if(sc != null)
                sc.close();
        }

    }

}
