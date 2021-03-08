import java.util.*;

/**
 * Ship class contains the information necessary to
 * describe a ship and extend it into different types.
 */
public class Ship {
    private String type;
    private int occupyingSpaces;
    private int shotPoints;
    private int sinkBonus;
    private int timesHit;
    private ArrayList<IntPair> shipPosition;

    /**
     * Constructs a Ship object with given parameter values and 
     * the number of times it is hit (initially 0).
     * @param type the type of the ship
     * @param occupyingSpaces the number of spaces in the board it occupies
     * @param shotPoints the points the opponent gets if one of its positions is hit
     * @param sinkBonus the bonus the opponent gets if they entirely sink this ship
     */
    Ship(String type, int occupyingSpaces, int shotPoints, int sinkBonus){
        this.type = type;
        this.occupyingSpaces = occupyingSpaces;
        this.shotPoints = shotPoints;
        this.sinkBonus = sinkBonus;
        this.timesHit = 0;
    }

    /**
     * Returns string with condition of the ship
     * @return String based on the value of the timesHit variable
     */
    public String Condition(){
        /* switch requires constant expressions */
        if(this.timesHit == 0)
            return "Intact";
        else if (this.timesHit == this.occupyingSpaces)
            return "Sunk";
        else return "Hit";
    }

    /**
     * If this ship has only been hit once return true -
     * needed in order to know where to aim (for Enemy Player)
     * @return true|false
     */
    public boolean firstTimeHit(){
        if(this.timesHit == 1) return true;
        return false;
    }

    /**
     * If a Ship is hit increase "timesHit" variable
     */
    public void isHit(){
        this.timesHit++;
    }

    /**
     * Getter method for the type of ship
     * @return "type" variable
     */
    public String getType() {
        return this.type;
    }

    /**
     * Getter method for points it gives 
     * if one of its positions is shot
     * @return "shotPoints" variable
     */
    public int getShotPoints() {
        return this.shotPoints;
    }

    /**
     * Getter method for points it gives 
     * if one of all its positions are shot
     * @return "sinkBonus" variable
     */
    public int getSinkBonus() {
        return this.sinkBonus;
    }

    /**
     * Getter method for the number of
     * spaces a ship occupies in a board
     * @return "occupyingSpaces" variable
     */
    public int getOccupyingSpaces() {
        return occupyingSpaces;
    }

    /**
     * Places the ship on the board, saving the positions it occupies.
     * It creates the shipPosition ArrayList, that contains
     * all IntPairs of the positions it occupies. More specifically,
     * it takes the info provided by the .txt file and 
     * uses it to create the "shipPosition" variable
     * @param typeOfShip needed to determine number of occupying spaces
     * @param position from which (i, j) position to start placing the ship
     * @param orientation whether to place the ship horizontally or vertically
     */
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

    /**
     * Getter method for the positions a ship occupies
     * @return "shipPosition" ArrayList variable
     */
    public ArrayList<IntPair> getShipPosition() {
        return shipPosition;
    }

}
