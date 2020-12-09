import java.io.*;
import java.util.*;

public class Main {

    private static Integer typeOfShip, i_position, j_position, orientation;

    /* Function to assist file input
    Source: https://knpcode.com/java-programs/how-to-read-delimited-file-in-java/ 
    */
    public static void parseData(String str) {
        Scanner lineScanner = new Scanner(str);
        lineScanner.useDelimiter(",");
        while (lineScanner.hasNext()) {
            typeOfShip = Integer.parseInt(lineScanner.next());
            i_position = Integer.parseInt(lineScanner.next());
            j_position = Integer.parseInt(lineScanner.next());
            orientation = Integer.parseInt(lineScanner.next());
            /*System.out.println("Input: " 
            + typeOfShip + i_position 
            + j_position + orientation);*/
        }
        lineScanner.close();
    }

    public static void main(String[] args) {
        Grid PlayerGrid = new Grid();
        Grid EnemyGrid = new Grid();

        Player Player = new Player("Player");
        EnemyPlayer EnemyPlayer = new EnemyPlayer("Enemy");
        ArrayList<IntPair> possiblePositions = new ArrayList<IntPair>();

        /*********
         * Read from file - add exceptions, for now assume it is correct
         *********/
        System.out.println("Ship Array size = " + Player.shipArray.length);
        Scanner sc = null;
        for(int i=0; i<2; i++)
        try {
            if(i==0)
                sc = new Scanner(new File("/home/panos/Desktop/MediaLab/player_SCENARIO-ID.txt"));
            else 
                sc = new Scanner(new File("/home/panos/Desktop/MediaLab/enemy_SCENARIO-ID.txt"));
            
            while (sc.hasNextLine()) {
                String str = sc.nextLine();
                parseData(str);
                IntPair temp = new IntPair(i_position, j_position); /* is local to while, no need to free memory */
                if(i==0){
                  /* For Player */
                    /* Set ship position */
                    Player.shipArray[typeOfShip-1].setShipPosition(typeOfShip, temp, orientation);
                    /* Update board in corresponding Grid */
                    for(int j=0; j<Player.shipArray[typeOfShip-1].getOccupyingSpaces(); j++)
                        PlayerGrid.Set(Player.shipArray[typeOfShip-1].getShipPosition().get(j).i_pos, 
                        Player.shipArray[typeOfShip-1].getShipPosition().get(j).j_pos, 1);
                }
                else{
                  /* For Enemy */
                    EnemyPlayer.shipArray[typeOfShip-1].setShipPosition(typeOfShip, temp, orientation);
                    for(int j=0; j<EnemyPlayer.shipArray[typeOfShip-1].getOccupyingSpaces(); j++)
                        EnemyGrid.Set(EnemyPlayer.shipArray[typeOfShip-1].getShipPosition().get(j).i_pos, 
                        EnemyPlayer.shipArray[typeOfShip-1].getShipPosition().get(j).j_pos, 1);
                }
            }
        } catch (IOException exp){
            exp.printStackTrace();
        } finally{
            if(sc != null)
                sc.close();
        }

        PlayerGrid.printUnfiltered();
        EnemyGrid.printUnfiltered();


    while(true){
    // Start Game, Create Everything

    /*
    
    Player turn
    
    */
    // Is Game Over? (1) -> Ships
    
    IntPair positionToHit = new IntPair(0,0);
    /* Hit is done after the skeleton and if it doesn't succeed
    it returns false, thus re-entering the do-while */
    do {
        /* Get possible positions from AIChoose function
        and put them in positionToHit variable (necessary for predictions afterwards) */
        /* Note: In AIChoose, possiblePositions is only used if AIOption is enabled, 
        therefore there is no need to examine edge cases (e.g. the first round where it's NULL) */
        positionToHit = EnemyPlayer.AIChoose(possiblePositions.get(0));
        if(!possiblePositions.isEmpty()){
            /* if there are possible positions, remove one after using it -
            (it is used when it is chosen from AIChoose) */
            possiblePositions.remove(0);
        }
        else{
            /* if there aren't any possible positions, disable AI Option */
            EnemyPlayer.disableAIOption();
        }
    }
    while (!PlayerGrid.Hit(positionToHit.i_pos, positionToHit.j_pos));
    
    /* If a ship is hit enable AI Option */
    for(int i=0; i<5; i++)
        if(Player.shipArray[i].Condition() == "Hit") EnemyPlayer.enableAIOption();
        else EnemyPlayer.disableAIOption();

    if(EnemyPlayer.getAIOption()){
       
        IntPair temp = new IntPair(0,0);

    /*  
    We need to check these positions:
         []
      [] X []
        []
    of the previous Hit move (cause we assume it succeeded).
    We add new possible positions in the possiblePositions ArrayList
    */

        temp.i_pos = positionToHit.i_pos;
        temp.j_pos = positionToHit.j_pos;
     
    /* 1st position -> (i-1, j) */
        temp.i_pos -= 1;
        if(PlayerGrid.isUnknown(temp.i_pos, temp.j_pos))
            possiblePositions.add(possiblePositions.size(), temp);

    /* 2nd position -> (i+1, j) */        
        temp.i_pos += 2;
        if(PlayerGrid.isUnknown(temp.i_pos, temp.j_pos))
            possiblePositions.add(possiblePositions.size(), temp);
        temp.i_pos -= 1;
      
    /* 3rd position -> (i, j-1) */    
        temp.j_pos -= 1;
        if(PlayerGrid.isUnknown(temp.i_pos, temp.j_pos))
            possiblePositions.add(possiblePositions.size(), temp);
       
    /* 4th position -> (i, j+1) */    
            temp.j_pos += 2;
        if(PlayerGrid.isUnknown(temp.i_pos, temp.j_pos))    
            possiblePositions.add(possiblePositions.size(), temp);
        
    /* Note: the validity of the positions (e.g. if i<0)
    is checked in the isUnknown function */
    }

    PlayerGrid.printFiltered();

    /*
    
    Enemy Turn
    
    */
    // Is Game Over? (2) -> Ships -> Moves
    }
    }
}
