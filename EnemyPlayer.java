import java.util.Random;
import java.util.Vector;

public class EnemyPlayer extends Player {
    
    private boolean AIOption;
    private Vector<IntPair> enemyChoices;

/* Initalizes EnemyPlayer class, first as a Player class
and then initializes EnemyPlayer-specific parameters 
(Option for AI, aka "smart targeting" and positions not already hit)*/
    EnemyPlayer(String name){
        super(name);

        AIOption = false;
        this.enemyChoices = new Vector<IntPair>();
        IntPair temp = new IntPair(0, 0);
        for(int i=0; i<10; i++)
            for(int j=0; j<10; j++){
                temp.i_pos = i;
                temp.j_pos = j;
                enemyChoices.add(temp);
            }
    }

    /* Is used by the enemy/computer to choose which position to hit -
    possiblePositions is given by main and is updated when this function resolves */
    public IntPair AIChoose(IntPair possiblePositions){
        
        Random rand = new Random();
        IntPair ans = new IntPair(0, 0);
        
        /* Do you want a smart choice? Or random? */
        if(this.AIOption == false){
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

    public void enableAIOption(){
        this.AIOption = true;
    }

    public void disableAIOption(){
        this.AIOption = false;
    }

    public boolean getAIOption(){
        return this.AIOption;
    }

}
