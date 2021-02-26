import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class EnemyPlayer extends Player {
    
    private boolean AIOption;
    private Vector<IntPair> enemyChoices;
    private ArrayList<IntPair> possiblePositions;
    private IntPair possiblePositionsErrorIntPair;
    private static IntPair positionInQuestion;

/* Initalizes EnemyPlayer class, first as a Player class
and then initializes EnemyPlayer-specific parameters 
(Option for AI, aka "smart targeting" and positions not already hit)*/
    EnemyPlayer(String name){
        super(name);

        AIOption = false;
        this.enemyChoices = new Vector<IntPair>();
        for(int i=0; i<10; i++)
            for(int j=0; j<10; j++){
                enemyChoices.add(new IntPair(i, j));
            }
        //System.out.println(enemyChoices.size());
    }

    /* Is used by the enemy/computer to choose which position to hit -
    possiblePositions is given by main and is updated when this function resolves */
    public IntPair AIChoose(IntPair possiblePositions){
        
        Random rand = new Random();
        IntPair ans = new IntPair(0, 0);
        
        /* Do you want a smart choice? Or random? */
        if(this.AIOption == false || possiblePositions.i_pos == -1){
            /* Try two random numbers and if they are possible moves
            that can be made, remove them from the choices and return them */
            do{
                ans.i_pos = rand.nextInt(10);
                ans.j_pos = rand.nextInt(10);
            }
            while(!enemyChoices.contains(ans));
        }
        else{
            ans.i_pos = possiblePositions.i_pos;
            ans.j_pos = possiblePositions.j_pos;
        }
        /* Find index of IntPair that contains (i, j) and remove it */
        enemyChoices.remove(enemyChoices.indexOf(ans));
        return ans;
    }

    
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

    public IntPair enemyTurn(Player Player, Grid PlayerGrid, EnemyPlayer EnemyPlayer, boolean playersShipsAllSunk){
            
            IntPair positionsHitThisTurn;
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

        return positionsHitThisTurn;
    }


    public void enableAIOption(){
        this.AIOption = true;
    }

    public void disableAIOption(){
        this.AIOption = false;
    }

    public boolean getAIOption(){
        return this.AIOption;
    }

    public void initializePossiblePositions(){
        possiblePositions = new ArrayList<IntPair>();
    }

}
