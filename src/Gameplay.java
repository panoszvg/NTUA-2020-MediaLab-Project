import java.util.*;
import java.io.*;

public class Gameplay {
    
    public static boolean gameIsOver;

    private boolean PlayerPlaysFirst;
    public boolean getPlayerPlaysFirst() {
        return PlayerPlaysFirst;
    }
    private boolean playersShipsAllSunk;
    private static Grid PlayerGrid;
    public Grid getPlayerGrid() {
        return PlayerGrid;
    }
    private static Grid EnemyGrid;
    public Grid getEnemyGrid() {
        return EnemyGrid;
    }

    private static Player Player;
    public Player getPlayer() {
        return Player;
    }

    private static EnemyPlayer EnemyPlayer;
    public EnemyPlayer getEnemyPlayer() {
        return EnemyPlayer;
    }

    // private static void printTables(Player Player, EnemyPlayer EnemyPlayer ,Grid PlayerGrid, Grid EnemyGrid){
    //     System.out.print("Player's Points: " + String.format("%-" + 6 + "s", Player.getPoints()) + "|");
    //     System.out.println("   Enemy's Points: " + EnemyPlayer.getPoints());
    //     System.out.println();
    //     for(int i=0; i<10; i++){
    //         PlayerGrid.printRowUnfiltered(i);
    //         EnemyGrid.printRowfiltered(i);
    //     }
    //     System.out.println();
    // }

  
    public static Integer typeOfShip, i_position, j_position, orientation;
    public static int inputCounter = 0;
    public static boolean fileNotFound = false; // some reason can't create Exception

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

    public static void read(Player Player, Grid PlayerGrid, EnemyPlayer EnemyPlayer, Grid EnemyGrid, String SCENARIO_ID) 
    throws OversizeException, OverlapTilesException, AdjacentTilesException, InvalidCountException {
        Scanner sc = null;
        for(int i=0; i<2; i++)
        try {
            if(i==0)
                {
                    File file = new File("./player_" + SCENARIO_ID + ".txt");
                    if (file.exists()) sc = new Scanner(file);
                    else {
                        fileNotFound = true;
                        return;
                    }
                }
            else 
                {
                    File file = new File("./enemy_" + SCENARIO_ID + ".txt");
                    if (file.exists()) sc = new Scanner(file);
                    else {
                        fileNotFound = true;
                        return;
                    }
                }
            
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
                        }
                        catch (OversizeException oversizeException){throw oversizeException;}
                        catch (OverlapTilesException overlapTilesException){throw overlapTilesException;}
                    
                        for(int j=0; j<Player.shipArray[typeOfShip-1].getOccupyingSpaces(); j++) {
                        
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
                }
                else{
                  /* For Enemy */
                    createShip((Player)EnemyPlayer);
                    EnemyPlayer.shipArray[typeOfShip-1].setShipPosition(typeOfShip, temp, orientation);
                    for(int j=0; j<EnemyPlayer.shipArray[typeOfShip-1].getOccupyingSpaces(); j++)
                        try{
                            EnemyGrid.Set(EnemyPlayer.shipArray[typeOfShip-1].getShipPosition().get(j).i_pos, 
                            EnemyPlayer.shipArray[typeOfShip-1].getShipPosition().get(j).j_pos, 1);
                        }
                        catch(OversizeException oversizeException){throw oversizeException;}
                        catch (OverlapTilesException overlapTilesException){throw overlapTilesException;}
                
                        for(int j=0; j<EnemyPlayer.shipArray[typeOfShip-1].getOccupyingSpaces(); j++){
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
                                || EnemyGrid.isShip(new IntPair(EnemyPlayer.shipArray[typeOfShip-1].getShipPosition().get(j).i_pos,
                                    EnemyPlayer.shipArray[typeOfShip-1].getShipPosition().get(j).j_pos-1))))
                                throw new AdjacentTilesException("AdjacentTilesException");
                        }

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


    public void gameplay(MainApp a, String SCENARIO_ID) {
        
        Random rand = new Random();
        PlayerPlaysFirst = (rand.nextInt(2) == 0);
        playersShipsAllSunk = false;
        PlayerGrid = new Grid();
        EnemyGrid = new Grid();

        Player = new Player("Player");
        EnemyPlayer = new EnemyPlayer("Enemy");
        EnemyPlayer.initializePossiblePositions();
        gameIsOver = false;

        /*********
         * Read from file
         *********/
        
        try{
            read(Player, PlayerGrid, EnemyPlayer, EnemyGrid, SCENARIO_ID);
        }
        catch(OversizeException oversizeException){
            a.setNoExceptions(false);
            a.createAlert(null, "Oversize Exception", "Please make sure that ships don't go out of the grid;s bounds."); 
            System.out.println(oversizeException.getMessage()); 
            return;
        }
        catch (OverlapTilesException overlapTilesException){
            a.setNoExceptions(false);
            a.createAlert(null, "OverlapTiles Exception", "Please make sure that ships don't occupy the same space."); 
            System.out.println(overlapTilesException.getMessage()); 
            return;
        }
        catch (AdjacentTilesException adjacentTilesException){
            a.setNoExceptions(false);
            a.createAlert(null, "AdjacentTiles Exception", "Ships must have at least one empty space between them."); 
            System.out.println(adjacentTilesException.getMessage()); 
            return;
        }
        catch (InvalidCountException invalidCountException){
            a.setNoExceptions(false);
            a.createAlert(null, "InvalidCount Exception", "Make sure you have provided the correct number and type of ships."); 
            System.out.println(invalidCountException.getMessage()); 
            return;
        }
        if(fileNotFound){
            a.setNoExceptions(false);
            a.createAlert(null, "File Not Found", "Please insert a valid SCENARIO-ID.");
        }

    }

    
    public IntPair oneTurn(MainApp a, int i_coord, int j_coord) throws AlreadyHitException {
    
        
    /*Player turn*/
    if(PlayerPlaysFirst){
    a.setInputTextArea("Enter the coordinates (i, j) for your move: ");

    if(!EnemyGrid.Hit(i_coord, j_coord)) throw new AlreadyHitException("Position is already hit");

    IntPair userAttackPosition = new IntPair(i_coord, j_coord);
    if(EnemyGrid.wasHit(userAttackPosition)){
        Player.madeASuccessfulShot();
        /* Call isHit() to update timesHit variable */
        EnemyPlayer.shipArray[EnemyPlayer.findShip(userAttackPosition)].isHit();
        /* Increase Player points according to ship found in position */
        Player.IncreasePoints(EnemyPlayer.shipArray[EnemyPlayer.findShip(userAttackPosition)].getShotPoints());
        /* If ship is sunk increase Player points with Sink Bonus */
        if(EnemyPlayer.shipArray[EnemyPlayer.findShip(userAttackPosition)].Condition() == "Sunk"){
            Player.IncreasePoints(EnemyPlayer.shipArray[EnemyPlayer.findShip(userAttackPosition)].getSinkBonus());
            a.setOutputTextArea("You sunk a ship!");
        }
        else a.setOutputTextArea("You hit a ship!");

        /* All ships sunk == every ship is sunk 
        Therefore, if one isn't, make playersShipsAllSunk not true */    
        playersShipsAllSunk = true;
        for(int i=0; i<5; i++)
            if(EnemyPlayer.shipArray[i].Condition() != "Sunk")
                playersShipsAllSunk = false;
    }
    Player.MadeAMove();
    // Is Game Over?
    if((Player.getMoves() == EnemyPlayer.getMoves() && EnemyPlayer.getMoves() == 0) || playersShipsAllSunk) 
    {
        gameIsOver = true;
        a.setInputTextArea("");
        if (Player.getPoints() > EnemyPlayer.getPoints())
            a.setOutputTextArea("You won!");
        else a.setOutputTextArea("You lost.");
    }
    }
    else PlayerPlaysFirst = true;

    /*Enemy Turn*/
    IntPair ReturnPair = EnemyPlayer.enemyTurn(Player, PlayerGrid, EnemyPlayer, playersShipsAllSunk);
    
    //printTables(Player, EnemyPlayer, PlayerGrid, EnemyGrid);
    // Is Game Over?
    if((Player.getMoves() == EnemyPlayer.getMoves() && EnemyPlayer.getMoves() == 0) || playersShipsAllSunk) 
    {
        gameIsOver = true;
        a.setInputTextArea("");
        if (Player.getPoints() > EnemyPlayer.getPoints())
            a.setOutputTextArea("You won!");
        else a.setOutputTextArea("You lost.");
    }

    return ReturnPair;

    }
}
