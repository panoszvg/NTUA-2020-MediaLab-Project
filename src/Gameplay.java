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

    private Player Player;
    private EnemyPlayer EnemyPlayer;
    private ArrayList<IntPair> possiblePositions;


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

    // private static IntPair getUserInput(Grid EnemyGrid, Scanner userInput){
    //     boolean alreadyHit = false;
    //     int uAP_i;
    //     int uAP_j;
    //     do{
    //         if(alreadyHit)
    //             System.out.print("That position is already hit, try another: ");
    //         else alreadyHit = true;
            
    //         uAP_i = userInput.nextInt();
    //         uAP_j = userInput.nextInt();
    //         if(uAP_i<0 || uAP_i>9 || uAP_j<0 || uAP_j>9) {
    //             System.out.print("Please give valid position (0 <= i,j <= 9): ");
    //             alreadyHit = false; /* so that the alreadyHit message is not displayed this time */
    //             continue;
    //         }
    //     }
    //     while(!EnemyGrid.Hit(uAP_i, uAP_j));
    //     return new IntPair(uAP_i, uAP_j);
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
            //System.out.println("Checking-->  (" + (positionInQuestion.i_pos-1) + "," + (positionInQuestion.j_pos) + ")");
            if(PlayerGrid.wasHit(new IntPair(positionInQuestion.i_pos-1, positionInQuestion.j_pos)))
                vertical = true;
            else vertical = false;
        
            //System.out.println("Checking-->  (" + (positionInQuestion.i_pos+1) + "," + (positionInQuestion.j_pos) + ")");
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

    public void gameplay() {
        
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
            read(Player, PlayerGrid, EnemyPlayer, EnemyGrid);
        }
        catch(OversizeException oversizeException){System.out.println(oversizeException.getMessage()); return;}
        catch (OverlapTilesException overlapTilesException){System.out.println(overlapTilesException.getMessage()); return;}
        catch (AdjacentTilesException adjacentTilesException){System.out.println(adjacentTilesException.getMessage()); return;}
        catch (InvalidCountException invalidCountException){System.out.println(invalidCountException.getMessage()); return;}
        PlayerGrid.printUnfiltered();
        EnemyGrid.printUnfiltered();
    }
   
    
    public IntPair oneTurn(int i_coord, int j_coord) throws AlreadyHitException {
    
    IntPair positionsHitThisTurn;
        
    /*Player turn*/
    if(PlayerPlaysFirst){
    System.out.print("Enter the coordinates (i, j) for your move: ");
    // Scanner userInput = new Scanner(new FilterInputStream(System.in) {
    //     @Override
    //     public void close() throws IOException {
    //         // do nothing here ! 
    //     }
    // });
    
    // IntPair userAttackPosition = getUserInput(EnemyGrid, userInput);
    // userInput.close();

    if(!EnemyGrid.Hit(i_coord, j_coord)) throw new AlreadyHitException("Position is already hit");

    System.out.println(); System.out.println(); System.out.println(); System.out.println(); System.out.println();

    IntPair userAttackPosition = new IntPair(i_coord, j_coord);
    if(EnemyGrid.wasHit(userAttackPosition)){
        /* Call isHit() to update timesHit variable */
        EnemyPlayer.shipArray[EnemyPlayer.findShip(userAttackPosition)].isHit();
        /* Increase Player points according to ship found in position */
        Player.IncreasePoints(EnemyPlayer.shipArray[EnemyPlayer.findShip(userAttackPosition)].getShotPoints());
        /* If ship is sunk increase Player points with Sink Bonus */
        if(EnemyPlayer.shipArray[EnemyPlayer.findShip(userAttackPosition)].Condition() == "Sunk"){
            Player.IncreasePoints(EnemyPlayer.shipArray[EnemyPlayer.findShip(userAttackPosition)].getSinkBonus());
            System.out.println("You sunk a ship!");
        }
        else System.out.println("You hit a ship!");
        System.out.println();
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
        printTables(Player, EnemyPlayer, PlayerGrid, EnemyGrid);
        System.out.println();
        if (Player.getPoints() > EnemyPlayer.getPoints())
            System.out.println("You won!");
        else System.out.println("You lost.");
        System.out.println();
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
    
    printTables(Player, EnemyPlayer, PlayerGrid, EnemyGrid);
    System.out.println();System.out.println();
    // Is Game Over?
    if((Player.getMoves() == EnemyPlayer.getMoves() && EnemyPlayer.getMoves() == 0) || playersShipsAllSunk) 
    {
        System.out.println();
        if (Player.getPoints() > EnemyPlayer.getPoints())
            System.out.println("You won!");
        else System.out.println("You lost.");
        System.out.println();
    }

    return positionsHitThisTurn;

    }
}
