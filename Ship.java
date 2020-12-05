public class Ship {
    private String type;
    private int occupyingSpaces;
    private int shotPoints;
    private int sinkBonus;
    private int timesHit;

    /* Create Ship - is called 5 times for each player */
    Ship(String type){
        this.type = type;
        switch(type){
        case "Carrier":
            this.occupyingSpaces = 5;
            this.shotPoints = 350;
            this.sinkBonus = 1000;
            break;
        case "Battleship":
            this.occupyingSpaces = 4;
            this.shotPoints = 250;
            this.sinkBonus = 500;
            break;
        case "Cruiser":
            this.occupyingSpaces = 3;
            this.shotPoints = 100;
            this.sinkBonus = 250;
            break;
        case "Submarine":
            this.occupyingSpaces = 3;
            this.shotPoints = 100;
            this.sinkBonus = 0;
            break;
        case "Destroyer":
            this.occupyingSpaces = 2;
            this.shotPoints = 50;
            this.sinkBonus = 0;
            break;
        }
        this.timesHit = 0;
    }

    /* Returns string with condition of the ship -
    could be a String member of the class, not a function */
    public String Condition(){
        /* switch requires constant expressions */
        if(this.timesHit == 0)
            return "Intact";
        else if (this.timesHit == this.occupyingSpaces)
            return "Sunk";
        else return "Hit";
    }

    public String getType() {
        return this.type;
    }

    public int getShotPoints() {
        return this.shotPoints;
    }

    public int getSinkBonus() {
        return this.sinkBonus;
    }
}
