public class Player {
    private String name;
    private int points;
    private int moves;

    /* Initalizes Player class with Name, Points
    (initially 0) and Moves (initially 40) */
    Player(String name){
        this.name = name;
        this.points = 0;
        this.moves = 40;
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

    /* Increase PLayer points */
    public void IncreasePoints(int points){
        this.points += points;
    }

}
