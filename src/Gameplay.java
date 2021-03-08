import java.util.*;
import java.io.*;

/**
 * This class implements the gameplay, that is reading from the
 * file, initialising the game with both players (being static), grids 
 * and ships, also provides access to all of them via getter functions 
 * (used in MainApp class)
 */
public class Gameplay {
    
    public static boolean gameIsOver;

    private boolean PlayerPlaysFirst;
    /**
     * Getter method to know whether player plays first
     * @return PlayerPlaysFirst boolean
     */
    public boolean getPlayerPlaysFirst() { 
        return PlayerPlaysFirst;
    }

    private boolean playersShipsAllSunk;
    private static Grid PlayerGrid;
    /**
     * Getter method to access player's board
     * @return PlayerGrid object
     */
    public Grid getPlayerGrid() {
        return PlayerGrid;
    }

    private static Grid EnemyGrid;
    /**
     * Getter method to access enemy's board
     * @return EnemyGrid object
     */
    public Grid getEnemyGrid() {
        return EnemyGrid;
    }

    private static Player Player;
    /**
     * Getter method to access player's information
     * @return Player object
     */
    public Player getPlayer() {
        return Player;
    }

    private static EnemyPlayer EnemyPlayer;
    /**
     * Getter method to access enemy's information
     * @return EnemyPlayer object
     */
    public EnemyPlayer getEnemyPlayer() {
        return EnemyPlayer;
    }

    /**
     * @deprecated
     * Prints the information of both players.
     * Was used during debugging, when there was
     * no front-end and game was played in terminal
     * @param Player necessary to access player's points
     * @param EnemyPlayer necessary to access enemy's points
     * @param PlayerGrid necessary to print player's board
     * @param EnemyGrid necessary to print enemy's board
     */
    private static void printTables(Player Player, EnemyPlayer EnemyPlayer ,Grid PlayerGrid, Grid EnemyGrid){
        System.out.print("Player's Points: " + String.format("%-" + 6 + "s", Player.getPoints()) + "|");
        System.out.println("   Enemy's Points: " + EnemyPlayer.getPoints());
        System.out.println();
        for(int i=0; i<10; i++){
            PlayerGrid.printRowUnfiltered(i);
            EnemyGrid.printRowfiltered(i);
        }
        System.out.println();
    }

  
    public static Integer typeOfShip, i_position, j_position, orientation;
    public static int inputCounter = 0;
    public static boolean fileNotFound = false; /* for some reason can't create Exception */

    /**
     * Function that parses data read from input file
     * Source: https://knpcode.com/java-programs/how-to-read-delimited-file-in-java/ 
     * @param str String of information to be parsed
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

    /**
     * Creates a Ship in shipArray of given Player (EnemyPlayer extends Player).
     * Position of shipArray is based on the typeOfShip, therefore if a player
     * doesn't place a type of ship, that position remains null -> used 
     * for InvalidCountException at the end of read()
     * @param player needed to know whether it's for player or enemy
     */
    public static void createShip(Player player){
        switch(typeOfShip){
            case 1:
                player.shipArray[0] = new Carrier();
                break;
            case 2:
                player.shipArray[1] = new Battleship();
                break;
            case 3:
                player.shipArray[2] = new Cruiser();
                break;
            case 4:
                player.shipArray[3] = new Submarine();
                break;
            case 5:
                player.shipArray[4] = new Destroyer();
                break;
        }
    }

    /**
     * Is given a SCENARIO_ID and it reads files that have that very variable.
     * Places ships and updates information in Players and Grid objects appropriately
     * @param Player needed to access player's shipArray
     * @param PlayerGrid needed to update it
     * @param EnemyPlayer needed to access enemy's shipArray
     * @param EnemyGrid needed to update it
     * @param SCENARIO_ID identify which files to read from
     * @throws OversizeException if ships are to be placed outside board
     * @throws OverlapTilesException if a ship is to be placed on top of another
     * @throws AdjacentTilesException if a sips is to be placed next to another
     * @throws InvalidCountException if ships aren't exactly 5 or of different types
     */
    public static void read(Player Player, Grid PlayerGrid, EnemyPlayer EnemyPlayer, Grid EnemyGrid, String SCENARIO_ID) 
    throws OversizeException, OverlapTilesException, AdjacentTilesException, InvalidCountException {
        Scanner sc = null;
        for(int i=0; i<2; i++)
        try {
            if(i==0)
                {
                    File file = new File("../scenarios/player_" + SCENARIO_ID + ".txt");
                    if (file.exists()) {
                        fileNotFound = false;
                        sc = new Scanner(file);
                    }
                    else {
                        fileNotFound = true;
                        return;
                    }
                }
            else 
                {
                    File file = new File("../scenarios/enemy_" + SCENARIO_ID + ".txt");
                    if (file.exists()) {
                        fileNotFound = false;
                        sc = new Scanner(file);
                    }
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

    /**
     * Creates new Gameplay (clean slate) using given input file 
     * @param SCENARIO_ID what file to read
     */
    public void gameplay(String SCENARIO_ID) {
        
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
            MainApp.setNoExceptions(false);
            MainApp.createAlert(null, "Oversize Exception", "Please make sure that ships don't go out of the grid;s bounds."); 
            System.out.println(oversizeException.getMessage()); 
            return;
        }
        catch (OverlapTilesException overlapTilesException){
            MainApp.setNoExceptions(false);
            MainApp.createAlert(null, "OverlapTiles Exception", "Please make sure that ships don't occupy the same space."); 
            System.out.println(overlapTilesException.getMessage()); 
            return;
        }
        catch (AdjacentTilesException adjacentTilesException){
            MainApp.setNoExceptions(false);
            MainApp.createAlert(null, "AdjacentTiles Exception", "Ships must have at least one empty space between them."); 
            System.out.println(adjacentTilesException.getMessage()); 
            return;
        }
        catch (InvalidCountException invalidCountException){
            MainApp.setNoExceptions(false);
            MainApp.createAlert(null, "InvalidCount Exception", "Make sure you have provided the correct number and type of ships."); 
            System.out.println(invalidCountException.getMessage()); 
            return;
        }
        if(fileNotFound){
            MainApp.setNoExceptions(false);
            MainApp.createAlert(null, "File Not Found", "Please insert a valid SCENARIO-ID.");
        }

    }

    /**
     * Implements one turn of the game. Both players play
     * and information in front-end is updated
     * @param app needed to update information in front-end
     * @param i_coord x-coordinate player wants to hit
     * @param j_coord y-coordinate player wants to hit
     * @return position (i, j) enemy hit this turn - needed to update front-end
     * @throws AlreadyHitException if the coordinates given by players have already been hit
     */
    public IntPair oneTurn(MainApp app, int i_coord, int j_coord) throws AlreadyHitException {
    
    /*Player turn*/
    if(PlayerPlaysFirst){
        app.setInputTextArea("Enter the coordinates (i, j) for your move: ");

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
            app.setOutputTextArea("You sunk a ship!");
        }
        else app.setOutputTextArea("You hit a ship!");

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
        app.setInputTextArea("");
        if (Player.getPoints() > EnemyPlayer.getPoints())
        app.setOutputTextArea("You won!");
        else if (Player.getPoints() < EnemyPlayer.getPoints())
        app.setOutputTextArea("You lost.");
        else app.setOutputTextArea("It's a tie.");
    }
    }
    else PlayerPlaysFirst = true;

    /*Enemy Turn*/
    IntPair ReturnPair = EnemyPlayer.enemyTurn(Player, PlayerGrid, EnemyPlayer, playersShipsAllSunk);
    
    //printTables(Player, EnemyPlayer, PlayerGrid, EnemyGrid);
    /* Is Game Over? */
    if((Player.getMoves() == EnemyPlayer.getMoves() && EnemyPlayer.getMoves() == 0) || playersShipsAllSunk) 
    {
        gameIsOver = true;
        app.setInputTextArea("");
        if (Player.getPoints() > EnemyPlayer.getPoints())
        app.setOutputTextArea("You won!");
        else if (Player.getPoints() < EnemyPlayer.getPoints())
        app.setOutputTextArea("You lost.");
        else app.setOutputTextArea("It's a tie.");
    }

    return ReturnPair;
    }
}
