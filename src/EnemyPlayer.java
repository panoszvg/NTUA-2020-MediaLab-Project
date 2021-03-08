import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

/**
 * EnemyPlayer class represents both the computer enemy
 * and provides information relevant to the enemy player and methods
 * to access that information that are needed for the game.
 */
public class EnemyPlayer extends Player {
    
    private boolean AIOption; /* whether to choose random or smart */
    private Vector<IntPair> enemyChoices; /* positions enemy hasn't already hit */
    private static ArrayList<IntPair> possiblePositions; /* is used if it hits Player's ship */
    private IntPair possiblePositionsErrorIntPair; /* assisting variable */
    private static IntPair positionInQuestion; /* assisting variable */

    /**
     * Initalizes EnemyPlayer class, first as a Player class
     * and then initializes EnemyPlayer-specific parameters 
     * (Option for AI, aka "smart targeting" and positions not already hit)
     * @param name same with Player class
     */
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

    /**
     * Is used by the enemy/computer to choose which position to hit:
     * If AIOption is disabled it chooses random, otherwise it uses given variable
     * @param possiblePositions is given by enemyTurn(...) and is updated when this function resolves:
     * if it contains the position (-1, -1) or the "AIOption" variable is false
     * a random position to hit is returned, otherwise it is returned as it is
     * @return positions that are going to be hit
     */
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

    /**
     * If Player's ship is hit, adds positions (i,j) that surround the hit position
     * in the "possiblePositions" variable 
     * @param PlayerGrid needed in order to check surrounding positions
     * @param positionToHit position that was hit and will check surrounding positions
     */
    private static void cross(Grid PlayerGrid, IntPair positionToHit){
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

    /**
     * After Player's ship is hit, decides whether it's placed horizontally
     * or vertically: it checks surrounding positions to see if any one of them
     * is hit. If none other is hit, it means no inference about the Player's ship's 
     * orientation can be made. If there is, decide its orientation and sweep; that
     * is, delete all possible positions that don't agree with the orientation by
     * removing them from the "possiblePositions" variable
     * @param PlayerGrid needed in order to check surrounding positions
     */
    private static void setAIOrientationAndSweep(Grid PlayerGrid){
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
    //else System.out.println("---->Didn't do anything in setAIOrientationAndSweep");
    }

    /**
     * Plays enemy's turn:
     * - Hit position in Player's grid
     * - If shot was successful update Player's ships and Enemy's variables:
     *      --> Points
     *      --> AIOption
     *      --> possiblePositions
     *      --> positionsHitThisTurn
     * - All the time sweep in order to ensure that no unnecessary possble positions remain in the list
     * @param Player needed to get access to Player's ships
     * @param PlayerGrid needed in order to check surrounding positions
     * @param EnemyPlayer needed in order to change enemy's variables
     * @param playersShipsAllSunk update it to know whether game is over
     * @return "positionsHitThisTurn" variable to update front-end
     */
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
                positionToHit = AIChoose(possiblePositions.get(0));
            }
            catch (Exception indexOutOfBoundsException){
                possiblePositionsErrorIntPair = new IntPair(-1, -1);
                positionToHit = AIChoose(possiblePositionsErrorIntPair);
            }
            finally{
                if(!possiblePositions.isEmpty()){
                    /* if there are possible positions, remove one after using it -
                    (it is used when it is chosen from AIChoose) */
                    possiblePositions.remove(0);
                }
                else{
                    /* if there aren't any possible positions, disable AI Option */
                    disableAIOption();
                }
            }
        }
        while (!PlayerGrid.Hit(positionToHit.i_pos, positionToHit.j_pos));

        positionsHitThisTurn = new IntPair(positionToHit.i_pos, positionToHit.j_pos);

        setAIOrientationAndSweep(PlayerGrid);

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
                /* If the ship was sunk get sink bonus and remove remaining possiblePositions */
                if(Player.shipArray[Player.findShip(positionToHit)].Condition() == "Sunk"){
                    EnemyPlayer.IncreasePoints(Player.shipArray[Player.findShip(positionToHit)].getSinkBonus());
                    possiblePositions.clear();
                    positionInQuestion = null;
                }
            }
        }

        setAIOrientationAndSweep(PlayerGrid);

        /* If a ship is hit enable AI Option */
        for(int i=0; i<5; i++){
            if(Player.shipArray[i].Condition() == "Hit") {
                enableAIOption();
                break;
            }
            disableAIOption(); 
        }

        /* If AI Option is enabled add new possible moves */    
        if(getAIOption() && PlayerGrid.wasHit(positionToHit))
            cross(PlayerGrid, positionToHit);

        setAIOrientationAndSweep(PlayerGrid);
        playersShipsAllSunk = true;
        for(int i=0; i<5; i++){
            if(EnemyPlayer.shipArray[i].Condition() != "Sunk") 
                playersShipsAllSunk = false;
        }
        EnemyPlayer.MadeAMove();

        return positionsHitThisTurn;
    }

    /**
     * Enable smart choices (from "possiblePositions" ArrayList)
     */
    public void enableAIOption(){
        this.AIOption = true;
    }

    /**
     * Enable random hits
     */
    public void disableAIOption(){
        this.AIOption = false;
    }

    /**
     * Check whether enemy is playing smart or random
     * @return "AIOption" variable
     */
    public boolean getAIOption(){
        return this.AIOption;
    }

    /**
     * Is called when a game is initialised, to also
     * initialise the "possiblePositions" variable
     */
    public void initializePossiblePositions(){
        possiblePositions = new ArrayList<IntPair>();
    }

}
