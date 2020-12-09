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
