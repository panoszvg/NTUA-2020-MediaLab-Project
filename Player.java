import java.util.Random;
import java.util.Vector;

public class Player {
    private String name;
    private int points;
    private int moves;
    private boolean AIOption;
    Ship[] shipArray;
    private Vector<IntPair> enemyChoices;

    /* Initalizes Player class with Name, Points
    (initially 0) and Moves (initially 40) */
    Player(String name){
        this.name = name;
        this.points = 0;
        this.moves = 40;
        AIOption = false;
        this.shipArray = new Ship[5];
        this.shipArray[0] = new Ship("Carrier");
        this.shipArray[1] = new Ship("Battleship");
        this.shipArray[2] = new Ship("Cruiser");
        this.shipArray[3] = new Ship("Submarine");
        this.shipArray[4] = new Ship("Destroyer");

        this.enemyChoices = new Vector<IntPair>();
        IntPair temp = new IntPair();
        for(int i=0; i<10; i++)
            for(int j=0; j<10; j++){
                temp.i_pos = i;
                temp.j_pos = j;
                enemyChoices.add(temp);
            }
    }

    /* Prints Player Statistics - for debugging */
    public void PlayerStats(){
        System.out.println("Player " + this.name + " Statistics");
        System.out.println("Points " + this.points);
        System.out.println("Remaining Moves " + this.moves);
    }

    /* Player made a move, decrease number of remaining moves */
    public void MadeAMove(){
        this.moves--;
    }

    /* Increase Player points */
    public void IncreasePoints(int points){
        this.points += points;
    }

    public boolean getAIOption(){
        return this.AIOption;
    }

    /* Is used by the enemy/computer to choose which position to hit -
    possiblePositions is given by main and is updated when this function resolves */
    public IntPair AIChoose(IntPair possiblePositions){
        
        Random rand = new Random();
        IntPair ans = new IntPair();
        
        /* Do you want a smart choice? Or random? */
        if(this.AIOption == false){
            /* Try two random numbers and if they are possible moves
            that can be made, remove them from the choices and return them */
            do{
                ans.i_pos = rand.nextInt(10);
                ans.j_pos = rand.nextInt(10);
            }
            while(!enemyChoices.contains(ans));
            /* Find index of IntPair that contains (i, j) and remove it */
            enemyChoices.remove(enemyChoices.indexOf(ans));
        }
        else{
            ans.i_pos = possiblePositions.i_pos;
            ans.j_pos = possiblePositions.j_pos;
        }
        return ans;
    }

    public void enableAIOption(){
        this.AIOption = true;
    }

    public void disableAIOption(){
        this.AIOption = false;
    }

}
