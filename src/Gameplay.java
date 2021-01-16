import java.util.*;

public class Gameplay extends ReadFromFile {
    
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

    private ArrayList<IntPair> possiblePositions;


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


    private static void cross(Grid PlayerGrid, IntPair positionToHit, ArrayList<IntPair> possiblePositions){
        /*  
        We need to check these positions :
             []
          [] X []
            []
        of the previous Hit move (cause we assume it succeeded).
        We add new possible positions in the possiblePositions ArrayList
        *
        *
        *
        Note: above is true for the first time a ship is hit; if it's not,
        then the next attack should have one orientation (vertical or horizontal),
        hence the firstTimeHit condition
        */

        /* 1st position -> (i-1, j) */
        if(PlayerGrid.isUnknown(positionToHit.i_pos-1, positionToHit.j_pos))
            possiblePositions.add(new IntPair(positionToHit.i_pos-1, positionToHit.j_pos));
        
        /* 2nd position -> (i+1, j) */        
        if(PlayerGrid.isUnknown(positionToHit.i_pos+1, positionToHit.j_pos))
            possiblePositions.add(new IntPair(positionToHit.i_pos+1, positionToHit.j_pos));

        /* 3rd position -> (i, j-1) */    
        if(PlayerGrid.isUnknown(positionToHit.i_pos, positionToHit.j_pos-1))
            possiblePositions.add(new IntPair(positionToHit.i_pos, positionToHit.j_pos-1));
    
        /* 4th position -> (i, j+1) */    
        if(PlayerGrid.isUnknown(positionToHit.i_pos, positionToHit.j_pos+1))
            possiblePositions.add(new IntPair(positionToHit.i_pos, positionToHit.j_pos+1));
        
        /* Note: the validity of the positions (e.g. if i<0)
        is checked in the isUnknown function */
    }

    private static void setAIOrientation(Grid PlayerGrid, ArrayList<IntPair> possiblePositions){
        if(positionInQuestion == null) return;

        boolean vertical;
        if((!PlayerGrid.isUnknown(positionInQuestion.i_pos-1, positionInQuestion.j_pos)
        && !PlayerGrid.isUnknown(positionInQuestion.i_pos+1, positionInQuestion.j_pos))
        ||
        (!PlayerGrid.isUnknown(positionInQuestion.i_pos, positionInQuestion.j_pos-1)
        && !PlayerGrid.isUnknown(positionInQuestion.i_pos, positionInQuestion.j_pos+1))
        ||
        (PlayerGrid.wasHit(new IntPair(positionInQuestion.i_pos, positionInQuestion.j_pos-1))) 
        ||
        (PlayerGrid.wasHit(new IntPair(positionInQuestion.i_pos, positionInQuestion.j_pos+1)))
        ||
        (PlayerGrid.wasHit(new IntPair(positionInQuestion.i_pos-1, positionInQuestion.j_pos))) 
        ||
        (PlayerGrid.wasHit(new IntPair(positionInQuestion.i_pos+1, positionInQuestion.j_pos))) 
        ){
            if(PlayerGrid.wasHit(new IntPair(positionInQuestion.i_pos-1, positionInQuestion.j_pos)))
                vertical = true;
            else vertical = false;
        
            if(PlayerGrid.wasHit(new IntPair(positionInQuestion.i_pos+1, positionInQuestion.j_pos)))
                vertical = true;

        /* If orientation is vertical, remove all horizontal possiblePositions (2 columns) 
        and vice versa. This works since we are examining one ship at a time */
        if(vertical){
            for(int i=0; i<10; i++){
                try{
                    possiblePositions.remove(new IntPair(i, positionInQuestion.j_pos-1));
                } catch(Exception arrayIndexOutOfBoundsException){System.out.println("For some reason exception for i=" + i);}
                try{
                    possiblePositions.remove(new IntPair(i, positionInQuestion.j_pos+1));
                } catch(Exception arrayIndexOutOfBoundsException){System.out.println("For some reason exception for i=" + i);}
            }
        }
        else{
            for(int i=0; i<10; i++){
                try{
                    possiblePositions.remove(new IntPair((positionInQuestion.i_pos-1), i));
                } catch(Exception arrayIndexOutOfBoundsException){System.out.println("For some reason exception for i=" + i);}
                try{
                    possiblePositions.remove(new IntPair((positionInQuestion.i_pos+1), i));
                } catch(Exception arrayIndexOutOfBoundsException){System.out.println("For some reason exception for i=" + i);}
            }    
        }
    }
    //else System.out.println("---->Didn't do anything in setAIOrientation");
    }

    public void gameplay(MainApp a, String SCENARIO_ID) {
        
        Random rand = new Random();
        PlayerPlaysFirst = (rand.nextInt(2) == 0);
        playersShipsAllSunk = false;
        PlayerGrid = new Grid();
        EnemyGrid = new Grid();

        Player = new Player("Player");
        EnemyPlayer = new EnemyPlayer("Enemy");
        possiblePositions = new ArrayList<IntPair>();

        /*********
         * Read from file
         *********/
        
        try{
            read(Player, PlayerGrid, EnemyPlayer, EnemyGrid, SCENARIO_ID);
        }
        catch(OversizeException oversizeException){
            a.setNoExceptions(false);
            a.createAlert("Oversize Exception", "Please make sure that ships don't go out of the grid;s bounds."); 
            System.out.println(oversizeException.getMessage()); 
            return;
        }
        catch (OverlapTilesException overlapTilesException){
            a.setNoExceptions(false);
            a.createAlert("OverlapTiles Exception", "Please make sure that ships don't occupy the same space."); 
            System.out.println(overlapTilesException.getMessage()); 
            return;
        }
        catch (AdjacentTilesException adjacentTilesException){
            a.setNoExceptions(false);
            a.createAlert("AdjacentTiles Exception", "Ships must have at least one empty space between them."); 
            System.out.println(adjacentTilesException.getMessage()); 
            return;
        }
        catch (InvalidCountException invalidCountException){
            a.setNoExceptions(false);
            a.createAlert("InvalidCount Exception", "Make sure you have provided the correct number and type of ships."); 
            System.out.println(invalidCountException.getMessage()); 
            return;
        }
        if(fileNotFound){
            a.setNoExceptions(false);
            a.createAlert("File Not Found", "Please insert a valid SCENARIO-ID.");
        }

    }

    
    public IntPair oneTurn(MainApp a, int i_coord, int j_coord) throws AlreadyHitException {
    
    IntPair positionsHitThisTurn;
        
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
        a.setInputTextArea("");
        if (Player.getPoints() > EnemyPlayer.getPoints())
            a.setOutputTextArea("You won!");
        else a.setOutputTextArea("You lost.");
    }
    }
    else PlayerPlaysFirst = true;


    /*Enemy Turn*/

    IntPair positionToHit = new IntPair(0,0);
    /* Hit is done after the skeleton and if it doesn't succeed
    it returns false, thus re-entering the do-while */
    do {
        /* Get possible positions from AIChoose function
        and put them in positionToHit variable (necessary for predictions afterwards) */
        /* Note: In AIChoose, possiblePositions is only used if AIOption is enabled, 
        therefore there is no need to examine edge cases (e.g. the first round where it's NULL) */
        try{
            positionToHit = EnemyPlayer.AIChoose(possiblePositions.get(0));
        }
        catch (Exception indexOutOfBoundsException){
            possiblePositionsErrorIntPair = new IntPair(-1, -1);
            positionToHit = EnemyPlayer.AIChoose(possiblePositionsErrorIntPair);
        }
        finally{
            if(!possiblePositions.isEmpty()){
                /* if there are possible positions, remove one after using it -
                (it is used when it is chosen from AIChoose) */
                possiblePositions.remove(0);
            }
            else{
                /* if there aren't any possible positions, disable AI Option */
                EnemyPlayer.disableAIOption();
            }
        }
    }
    while (!PlayerGrid.Hit(positionToHit.i_pos, positionToHit.j_pos));

    positionsHitThisTurn = new IntPair(positionToHit.i_pos, positionToHit.j_pos);

    setAIOrientation(PlayerGrid, possiblePositions);

    if(PlayerGrid.wasHit(positionToHit)){
        EnemyPlayer.madeASuccessfulShot();
        /* If the positionToHit (that was hit) was where 
        a ship was in the Grid, also update "Hit" status to Ship 
        and add points for hitting it to EnemyPlayer */
        Player.shipArray[Player.findShip(positionToHit)].isHit();
        EnemyPlayer.IncreasePoints(Player.shipArray[Player.findShip(positionToHit)].getShotPoints());

        /* If it was the first time this ship was hit update positionInQuestion */
        if(Player.shipArray[Player.findShip(positionToHit)].Condition() == "Hit" && 
        Player.shipArray[Player.findShip(positionToHit)].firstTimeHit()){
            positionInQuestion = new IntPair(positionToHit.i_pos, positionToHit.j_pos);
        }
        else{
            if(Player.shipArray[Player.findShip(positionToHit)].Condition() == "Sunk"){
                EnemyPlayer.IncreasePoints(Player.shipArray[Player.findShip(positionToHit)].getSinkBonus());
                possiblePositions.clear();
                positionInQuestion = null;
            }
        }
    }

    setAIOrientation(PlayerGrid, possiblePositions);

    /* If a ship is hit enable AI Option */
    for(int i=0; i<5; i++){
        if(Player.shipArray[i].Condition() == "Hit") {
            EnemyPlayer.enableAIOption();
            break;
        }
        EnemyPlayer.disableAIOption(); 
    }

    /* If AI Option is enabled add new possible moves */    
    if(EnemyPlayer.getAIOption() && PlayerGrid.wasHit(positionToHit))
        cross(PlayerGrid, positionToHit, possiblePositions);

    setAIOrientation(PlayerGrid, possiblePositions);
    playersShipsAllSunk = true;
    for(int i=0; i<5; i++){
        if(EnemyPlayer.shipArray[i].Condition() != "Sunk") 
            playersShipsAllSunk = false;
    }
    EnemyPlayer.MadeAMove();
    
    //printTables(Player, EnemyPlayer, PlayerGrid, EnemyGrid);
    // Is Game Over?
    if((Player.getMoves() == EnemyPlayer.getMoves() && EnemyPlayer.getMoves() == 0) || playersShipsAllSunk) 
    {
        a.setInputTextArea("");
        if (Player.getPoints() > EnemyPlayer.getPoints())
            a.setOutputTextArea("You won!");
        else a.setOutputTextArea("You lost.");
    }

    return positionsHitThisTurn;

    }
}
