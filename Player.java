import java.util.ArrayList;

public class Player {
    private String name;
    private int points;
    private int moves;
    Ship[] shipArray;

    /* Initalizes Player class with Name, Points
    (initially 0) and Moves (initially 40) */
    Player(String name){
        this.name = name;
        this.points = 0;
        this.moves = 40;
        this.shipArray = new Ship[5];
        this.shipArray[0] = new Ship("Carrier");
        this.shipArray[1] = new Ship("Battleship");
        this.shipArray[2] = new Ship("Cruiser");
        this.shipArray[3] = new Ship("Submarine");
        this.shipArray[4] = new Ship("Destroyer");
    }

    /* Prints Player Statistics - for debugging */
    public void PlayerStats(){
        System.out.println("Player " + this.name + " Statistics");
        System.out.println("Points " + this.points);
        System.out.println("Remaining Moves " + this.moves);
    }

    /* Finds which ship is in position (i, j) */
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

    /* Player made a move, decrease number of remaining moves */
    public void MadeAMove(){
        this.moves--;
    }

    /* Increase Player points */
    public void IncreasePoints(int points){
        this.points += points;
    }

    public int getMoves() {
        return moves;
    }
    
    public int getPoints() {
        return points;
    }

}
