import java.io.*;
import java.util.*;

public class ReadFromFile {
  
    public static Integer typeOfShip, i_position, j_position, orientation;
    public static IntPair possiblePositionsErrorIntPair, positionInQuestion;
    public static int inputCounter = 0;

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

    /* It creates a Ship in shipArray of player variable - position 
    of shipArray is based on the typeOfShip, therefore if a player
    doesn't place a type of ship, that position remains null -> used 
    for InvalidCountException at the end of read() */
    public static void createShip(Player player){
        switch(typeOfShip){
            case 1:
                player.shipArray[0] = new Ship("Carrier");
                break;
            case 2:
                player.shipArray[1] = new Ship("Battleship");
                break;
            case 3:
                player.shipArray[2] = new Ship("Cruiser");
                break;
            case 4:
                player.shipArray[3] = new Ship("Submarine");
                break;
            case 5:
                player.shipArray[4] = new Ship("Destroyer");
                break;
        }
    }

    public static void read(Player Player, Grid PlayerGrid, EnemyPlayer EnemyPlayer, Grid EnemyGrid) 
    throws OversizeException, OverlapTilesException, AdjacentTilesException, InvalidCountException {
        Scanner sc = null;
        for(int i=0; i<2; i++)
        try {
            if(i==0)
                sc = new Scanner(new File("./player_SCENARIO-ID.txt"));
            else 
                sc = new Scanner(new File("./enemy_SCENARIO-ID.txt"));
            
            inputCounter = 0;
            while (sc.hasNextLine()) {
                String str = sc.nextLine();
                inputCounter++;
                if(inputCounter > 5) throw new InvalidCountException("InvalidCountException");
                parseData(str);
                IntPair temp = new IntPair(i_position, j_position); /* is local to while, no need to free memory */
                if(i==0){
                  /* For Player */
                    createShip(Player);
                    /* Set ship position (it will be set regardless of whether it is correct -
                    if it throws OversizeException, it will be thrown on Set function) */
                    Player.shipArray[typeOfShip-1].setShipPosition(typeOfShip, temp, orientation);
                    /* Update board in corresponding Grid */
                    for(int j=0; j<Player.shipArray[typeOfShip-1].getOccupyingSpaces(); j++)
                        try{
                            PlayerGrid.Set(Player.shipArray[typeOfShip-1].getShipPosition().get(j).i_pos, 
                            Player.shipArray[typeOfShip-1].getShipPosition().get(j).j_pos, 1);
                            
                            /* if there is a ship adjacent throw Exception. We check:
                                [X][1][1][1][X] 
                            (and vertically using orientation variable) on the first if and:   
                                [X][X][X]
                                [1][1][1]
                                [X][X][X]
                            (and vertically using orientation variable) on the second if         
                            */                 
                            if(((j == 0) /* before first block */
                                && ((orientation == 1) 
                                    ? PlayerGrid.isShip(new IntPair(Player.shipArray[typeOfShip-1].getShipPosition().get(j).i_pos,
                                    Player.shipArray[typeOfShip-1].getShipPosition().get(j).j_pos-1)) 
                                    : PlayerGrid.isShip(new IntPair(Player.shipArray[typeOfShip-1].getShipPosition().get(j).i_pos-1,
                                    Player.shipArray[typeOfShip-1].getShipPosition().get(j).j_pos)))) 
                            || ((j == Player.shipArray[typeOfShip-1].getOccupyingSpaces()-1) /* after last block */
                                && ((orientation == 1) 
                                    ? PlayerGrid.isShip(new IntPair(Player.shipArray[typeOfShip-1].getShipPosition().get(j).i_pos,
                                    Player.shipArray[typeOfShip-1].getShipPosition().get(j).j_pos+1))
                                    : PlayerGrid.isShip(new IntPair(Player.shipArray[typeOfShip-1].getShipPosition().get(j).i_pos+1,
                                    Player.shipArray[typeOfShip-1].getShipPosition().get(j).j_pos))))) 
                                throw new AdjacentTilesException("AdjacentTilesException");
                            if((orientation == 1)
                                ? (PlayerGrid.isShip(new IntPair(Player.shipArray[typeOfShip-1].getShipPosition().get(j).i_pos+1,
                                    Player.shipArray[typeOfShip-1].getShipPosition().get(j).j_pos)) 
                                || PlayerGrid.isShip(new IntPair(Player.shipArray[typeOfShip-1].getShipPosition().get(j).i_pos-1,
                                    Player.shipArray[typeOfShip-1].getShipPosition().get(j).j_pos))
                                )
                                : (PlayerGrid.isShip(new IntPair(Player.shipArray[typeOfShip-1].getShipPosition().get(j).i_pos,
                                    Player.shipArray[typeOfShip-1].getShipPosition().get(j).j_pos+1)) 
                                || PlayerGrid.isShip(new IntPair(Player.shipArray[typeOfShip-1].getShipPosition().get(j).i_pos,
                                    Player.shipArray[typeOfShip-1].getShipPosition().get(j).j_pos-1))))
                                throw new AdjacentTilesException("AdjacentTilesException");

                        }
                        catch (OversizeException oversizeException){throw oversizeException;}
                        catch (OverlapTilesException overlapTilesException){throw overlapTilesException;}
                }
                else{
                  /* For Enemy */
                    createShip((Player)EnemyPlayer);
                    EnemyPlayer.shipArray[typeOfShip-1].setShipPosition(typeOfShip, temp, orientation);
                    for(int j=0; j<EnemyPlayer.shipArray[typeOfShip-1].getOccupyingSpaces(); j++)
                        try{
                            EnemyGrid.Set(EnemyPlayer.shipArray[typeOfShip-1].getShipPosition().get(j).i_pos, 
                            EnemyPlayer.shipArray[typeOfShip-1].getShipPosition().get(j).j_pos, 1);
                        
                            if(((j == 0) /* before first block */
                                && ((orientation == 1) 
                                    ? EnemyGrid.isShip(new IntPair(EnemyPlayer.shipArray[typeOfShip-1].getShipPosition().get(j).i_pos,
                                    EnemyPlayer.shipArray[typeOfShip-1].getShipPosition().get(j).j_pos-1)) 
                                    : EnemyGrid.isShip(new IntPair(EnemyPlayer.shipArray[typeOfShip-1].getShipPosition().get(j).i_pos-1,
                                    EnemyPlayer.shipArray[typeOfShip-1].getShipPosition().get(j).j_pos)))) 
                            || ((j == EnemyPlayer.shipArray[typeOfShip-1].getOccupyingSpaces()-1) /* after last block */
                                && ((orientation == 1) 
                                    ? EnemyGrid.isShip(new IntPair(EnemyPlayer.shipArray[typeOfShip-1].getShipPosition().get(j).i_pos,
                                    EnemyPlayer.shipArray[typeOfShip-1].getShipPosition().get(j).j_pos+1))
                                    : EnemyGrid.isShip(new IntPair(EnemyPlayer.shipArray[typeOfShip-1].getShipPosition().get(j).i_pos+1,
                                    EnemyPlayer.shipArray[typeOfShip-1].getShipPosition().get(j).j_pos))))) 
                                throw new AdjacentTilesException("AdjacentTilesException");
                            if((orientation == 1)
                                ? (EnemyGrid.isShip(new IntPair(EnemyPlayer.shipArray[typeOfShip-1].getShipPosition().get(j).i_pos+1,
                                    EnemyPlayer.shipArray[typeOfShip-1].getShipPosition().get(j).j_pos)) 
                                || EnemyGrid.isShip(new IntPair(EnemyPlayer.shipArray[typeOfShip-1].getShipPosition().get(j).i_pos-1,
                                    EnemyPlayer.shipArray[typeOfShip-1].getShipPosition().get(j).j_pos))
                                )
                                : (EnemyGrid.isShip(new IntPair(EnemyPlayer.shipArray[typeOfShip-1].getShipPosition().get(j).i_pos,
                                    EnemyPlayer.shipArray[typeOfShip-1].getShipPosition().get(j).j_pos+1)) 
                                || EnemyGrid.isShip(new IntPair(EnemyPlayer.shipArray[typeOfShip-1].getShipPosition().get(j).i_pos-1,
                                    EnemyPlayer.shipArray[typeOfShip-1].getShipPosition().get(j).j_pos-1))))
                                throw new AdjacentTilesException("AdjacentTilesException");
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

        /* if one position in shipArray is null it means that a type of ship was not used */
        for(int k=0; k<5; k++){
            if(Player.shipArray[k] == null) throw new InvalidCountException("Player InvalidCountException " + k);
            if(EnemyPlayer.shipArray[k] == null) throw new InvalidCountException("Enemy InvalidCountException " + k);
        }
    }

}
