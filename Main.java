import java.util.*;

public class Main extends ReadFromFile {
    
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
    else System.out.println("---->Didn't do anything in setAIOrientation");
    }

    public static void main(String[] args) {
        
      

        Grid PlayerGrid = new Grid();
        Grid EnemyGrid = new Grid();

        Player Player = new Player("Player");
        EnemyPlayer EnemyPlayer = new EnemyPlayer("Enemy");
        ArrayList<IntPair> possiblePositions = new ArrayList<IntPair>();

        /*********
         * Read from file - add exceptions, for now assume it is correct
         *********/
        
        try{
            read(Player, PlayerGrid, EnemyPlayer, EnemyGrid);
        }
        catch(OversizeException oversizeException){System.out.println(oversizeException.getMessage()); return;}
        catch (OverlapTilesException overlapTilesException){System.out.println(overlapTilesException.getMessage()); return;}
        PlayerGrid.printUnfiltered();
        EnemyGrid.printUnfiltered();
    

    while(true){
    // Start Game

    /*
    
    Player turn
    
    */
    // Is Game Over? (1) -> Ships
    //System.out.println("In loop with possiblePositions = " + possiblePositions.size());
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

    setAIOrientation(PlayerGrid, possiblePositions);

    /* If the positionToHit (that was hit) was where 
    a ship was in the Grid, also update "Hit" status to Ship */
    if(PlayerGrid.wasHit(positionToHit)){
        Player.shipArray[Player.findShip(positionToHit)].isHit();
        //System.out.println("A SHIP WAS HIT");

        /* If it was the first time this ship was hit update positionInQuestion */
        if(Player.shipArray[Player.findShip(positionToHit)].Condition() == "Hit" && 
        Player.shipArray[Player.findShip(positionToHit)].firstTimeHit()){
            positionInQuestion = new IntPair(positionToHit.i_pos, positionToHit.j_pos);
            //System.out.println("positionInQuestion is set.");
            // System.out.println("About to enter setAIOrientation!!!!!");
            // setAIOrientation(PlayerGrid, possiblePositions);
        }
        else{
            if(Player.shipArray[Player.findShip(positionToHit)].Condition() == "Sunk"){
                //System.out.println("Ship was sunk ---> removing all possiblePositions");
                possiblePositions.clear();
                //System.out.println("Confirm ---------> Size of possiblePositions = " + possiblePositions.size());
                positionInQuestion = null;
            }
        }
    }


    setAIOrientation(PlayerGrid, possiblePositions);


    /* If a ship is hit enable AI Option */
    for(int i=0; i<5; i++){
        if(Player.shipArray[i].Condition() == "Hit") {
            //System.out.println("Ship #" + i + " is hit  ->  AI Option is enabled");    
            EnemyPlayer.enableAIOption();
            break;
        }
        EnemyPlayer.disableAIOption();
    }


    /* If AI Option is enabled add new possible moves */    
    if(EnemyPlayer.getAIOption()){
    //System.out.println("positionToHit:  i = " + positionToHit.i_pos + "  j = " + positionToHit.j_pos);
    
    if(PlayerGrid.wasHit(positionToHit))
        cross(PlayerGrid, positionToHit, possiblePositions);
    }

    setAIOrientation(PlayerGrid, possiblePositions);
    EnemyPlayer.MadeAMove();
    // System.out.print("possiblePositions after turn = " + possiblePositions.size() + " -> ");
    // for(int j=0; j<possiblePositions.size(); j++)
    //     System.out.print("(" + possiblePositions.get(j).i_pos + "," + possiblePositions.get(j).j_pos + ")   ");
    // System.out.println();
    PlayerGrid.printFiltered();

    /*
    
    Enemy Turn
    
    */
    // Is Game Over? (2) -> Ships -> Moves
    if(EnemyPlayer.getMoves() == 0) break;
    }
    }
}
