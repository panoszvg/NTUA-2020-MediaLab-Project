import java.util.ArrayList;

/**
 * Player class represents both the human player and
 * the computer enemy (Enemy class that extends Player)
 * and provides information relevant to the player and methods
 * to access that information that are needed for the game.
 */
public class Player {
    private String name;
    private int points;
    private int moves;
    protected Ship[] shipArray;
    private int successfulShots;

    /**
     * Initalizes Player class with given name, points
     * (initially 0), successfulShots (initially 0) 
     * and moves (initially 40). Also initialises
     * a Ship array of 5 positions to place Ship objects (upcasted
     * into one of five different ship types)
     * @param name given by user in case it is needed (for
     * future extensions - eg. human player gives his name)
     */
    Player(String name){
        this.name = name;
        this.points = 0;
        this.moves = 40;
        this.shipArray = new Ship[5];
        this.successfulShots = 0;
    }

    /**
     * @deprecated
     * Prints Player Statistics
     * Was used during debugging, when there was
     * no front-end and game was played in terminal
     */
    public void PlayerStats(){
        System.out.println("Player " + this.name + " Statistics");
        System.out.println("Points " + this.points);
        System.out.println("Remaining Moves " + this.moves);
    }

    /**
     * Finds which ship is in position (i, j):
     * An IntPair is given that contains the position (i, j)
     * to be searched and returns the integer that corresponds
     * to the Ship type that is in that position, otherwise
     * returns -1
     * @param position given position in Grid of type IntPair
     * @return Ship type or -1
     */
    public int findShip(IntPair position){
        for(int i=0; i<5; i++){
            ArrayList<IntPair> temp = new ArrayList<IntPair>();
            temp = this.shipArray[i].getShipPosition();
            for(int j=0; j<this.shipArray[i].getOccupyingSpaces(); j++){
                //System.out.println(temp.get(j).i_pos + "," + temp.get(j).j_pos);
                if(temp.get(j).equals(position)) return i;
            }
        }
        return -1;
    }

    /**
     * When a player makes a move, the number of his remaining
     * moves is decreased by one, which is implemented
     * by decrementing the variable "moves"
     */
    public void MadeAMove(){
        this.moves--;
    }

    /**
     * When a player makes a successful shot, the number of
     * the "successfulShots" variable is incremented by one
     */
    public void madeASuccessfulShot(){
        this.successfulShots++;
    }

    /**
     * Increase a player's total points by the given value
     * @param points points to be added "points" variable
     */
    public void IncreasePoints(int points){
        this.points += points;
    }

    /**
     * Getter method for a player's remaining moves
     * @return "moves" variable
     */
    public int getMoves() {
        return moves;
    }
    
    /**
     *  Getter method for a player's total points
     * @return "points" variable
     */
    public int getPoints() {
        return points;
    }

    /**
     *  Getter method for a player's successful shots
     * @return "successfulShots" variable
     */
    public int getSuccessfulShots() {
        return successfulShots;
    }

}
