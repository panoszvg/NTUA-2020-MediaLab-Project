import java.util.Random;

public class Player {
    private String name;
    private int points;
    private int moves;
    private boolean AIOption;

    /* Initalizes Player class with Name, Points
    (initially 0) and Moves (initially 40) */
    Player(String name){
        this.name = name;
        this.points = 0;
        this.moves = 40;
        AIOption = false;
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
    public int[] AIChoose(int[][] possiblePositions){
        Random rand = new Random();
        int[] ans = new int[2];
        if(this.AIOption == false){
            ans[0] = rand.nextInt(10);
            ans[1] = rand.nextInt(10);
        }
        else{
            ans[0] = possiblePositions[0][0];
            ans[1] = possiblePositions[0][1];
        }
        return ans;
    }

}
