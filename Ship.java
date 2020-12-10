import java.util.*;

public class Ship {
    private String type;
    private int occupyingSpaces;
    private int shotPoints;
    private int sinkBonus;
    private int timesHit;
    private ArrayList<IntPair> shipPosition;

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

    /* If it has only been hit once return true -
    needed to know where to aim (for Enemy Player) */
    public boolean firstTimeHit(){
        if(this.timesHit == 1) return true;
        return false;
    }

    /* If a Ship is hit increase timesHit counter */
    public void isHit(){
        this.timesHit++;
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

    public int getOccupyingSpaces() {
        return occupyingSpaces;
    }

    /* It takes the info provided by the .txt file and 
    assigns it to the shipPosition */
    public void setShipPosition(int typeOfShip, IntPair position, int orientation){
        
        IntPair temp = new IntPair(position.i_pos, position.j_pos);
        int loopVariable = 0;

        switch(typeOfShip){
        case 1:
            loopVariable = 5;
            break;
        case 2:
            loopVariable = 4;
            break;
        case 3:
            loopVariable = 3;
            break;
        case 4:
            loopVariable = 3;
            break;
        case 5:
            loopVariable = 2;
            break;
        }
        
        this.shipPosition = new ArrayList<IntPair>();

        switch(orientation){
        case 1:
            for(int i=0; i<loopVariable; i++){
                temp = new IntPair(position.i_pos, position.j_pos + i);
                this.shipPosition.add(temp);
            }
            break;
        case 2:
            for(int i=0; i<loopVariable; i++){
                temp = new IntPair(position.i_pos + i, position.j_pos);
                shipPosition.add(temp);
            }
            break;
        }
    }

    public ArrayList<IntPair> getShipPosition() {
        return shipPosition;
    }
}
