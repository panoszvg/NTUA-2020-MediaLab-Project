/**
 * Implements the board on which ships are placed - both
 * Player's ships and Enemy'e ships
 */
class Grid{
    
    private int board[][];
    /*
    
    */

    /**
     * Initializes the playing board.
     * What each position represents and what
     * can be seen by who:
     *
     *            Player  Enemy
     * 0:Sea         X       X
     * 1:Ship        X
     * 2:Hit Ship    X       X
     * 3:Hit Sea     X       X
     */
    Grid(){
        this.board = new int[10][10];
        for(int i=0; i<10; i++)
            for(int j=0; j<10; j++)
                this.board[i][j] = 0;
    }

    /**
     * @deprecated
     * Prints the board with no filter - for player.
     * Was used during debugging, when there was
     * no front-end and game was played in terminal
     */
    public void printUnfiltered(){
        for(int i=0; i<10; i++){
            if(i!=0) System.out.println();
            for(int j=0; j<10; j++)
                System.out.print(board[i][j] + " ");
        }
        System.out.println();
        System.out.println();
    }

    /**
     * @deprecated
     * Prints the board with filtered - for enemy.
     * Was used during debugging, when there was
     * no front-end and game was played in terminal
     */
    public void printFiltered(){
        for(int i=0; i<10; i++){
            if(i!=0) System.out.println();
            for(int j=0; j<10; j++)
                switch(board[i][j]){
                    case 0:
                        System.out.print("- ");
                        break;
                    case 1:
                        System.out.print("- ");
                        break;
                    case 2:
                    System.out.print("X ");
                        break;
                    case 3:
                        System.out.print("O ");
                        break;    
                }
        }
        System.out.println();
        System.out.println();
    }

    /**
     * @deprecated
     * Prints given row raw (for player's ships), using integer values.
     * Was used during debugging, when there was
     * no front-end and game was played in terminal
     * @param i which row to print raw (values 0-9)
     */
    public void printRowUnfiltered(int i){
        for(int j=0; j<10; j++)
            System.out.print(board[i][j] + " ");
        System.out.print("   |   ");
    }

    /**
     * @deprecated
     * Prints given row (for enemy's ships), using:
     * - : Unknown
     * X : Hit Ship
     * O : Hit Sea
     * Was used during debugging, when there was
     * no front-end and game was played in terminal
     */
    public void printRowfiltered(int i){
        for(int j=0; j<10; j++)
        switch(board[i][j]){
        case 0:
            System.out.print("- ");
            break;
        case 1:
            System.out.print("- ");
            break;
        case 2:
            System.out.print("X ");
            break;
        case 3:
            System.out.print("O ");
            break;    
        }
            System.out.println();
    }

    /**
     * Hits the position (i,j) it is given
     * @param i x-coordinate
     * @param j y-coordinate
     * @return true if it succeeds, false if position was already hit
     */
    public boolean Hit(int i, int j){
        try{
            switch(this.board[i][j]){
            case 0: /* Sea */
                this.board[i][j] = 3; /* becomes Hit Sea */
                break;
            case 1: /* Ship */
                this.board[i][j] = 2; /* becomes Hit Ship */
                break;
            case 2: /* Hit Ship - is already hit */
                return false;
            case 3: /* Hit Sea - is already hit */
                return false;
            }
        }
        catch(Exception arrayIndexOutOfBoundsException){
            return false;
        }
        return true;
    }

    /**
     * Set a specific position (i,j) as a specific input (0-3)
     * @param i x-coordinate
     * @param j y-coordinate
     * @param setNumber integer the position (i, j) is set to
     * @throws OversizeException if it's outside the board
     * @throws OverlapTilesException if a different ship is already placed there
     */
    public void Set(int i, int j, int setNumber) throws OversizeException, OverlapTilesException {
        if(i<0 || i>9 || j<0 || j>9) throw new OversizeException("OversizeException");
        else {
            if(this.board[i][j] == 1) throw new OverlapTilesException("OverlapTilesException");
            this.board[i][j] = setNumber;
        }
    }

    /**
     * Checks whether the given position (i, j) is unknown (Sea:0 or Ship:1
     * instead of Hit Ship:2 or Hit Sea:3)
     * @param i x-coordinate
     * @param j y-coordinate
     * @return true if position hasn't been hit yet, false if it has
     */
    public boolean isUnknown(int i, int j){
        try{
            if(this.board[i][j] == 0 
            || this.board[i][j] == 1) return true;
        }
        catch(Exception arrayIndexOutOfBoundsException){
            return false;
        }
        return false;
    }

    /**
     * Check whether given position (i, j) is Hit Sea
     * @param i x-coordinate
     * @param j y-coordinate
     * @return true if integer in position (i, j) is 3, otherwise false
     */
    public boolean isHitSea(int i, int j){
        try{
            if(this.board[i][j] == 3) return true;
        }
        catch(Exception arrayIndexOutOfBoundsException){
            return false;
        }
        return false;
    }

    /**
     * Check whether given position (i, j) was a ship that was hit
     * @param position x-y coordinates
     * @return true if integer in position (i, j) is 2, otherwise false
     */
    public boolean wasHit(IntPair position){
        try{
            if(this.board[position.i_pos][position.j_pos] == 2) return true;
        }
        catch(Exception arrayIndexOutOfBoundsException){
            return false;
        }
        return false;
    }

    /**
     * Check whether given position (i, j) is a ship that isn't hit
     * @param position x-y coordinates
     * @return true if integer in position (i, j) is 1, otherwise false
     */
    public boolean isShip(IntPair position){
        try{
            if(this.board[position.i_pos][position.j_pos] == 1) return true;
        }
        catch(Exception arrayIndexOutOfBoundsException){
            return false;
        }
        return false;
    }

    /**
     * Check whether given position (i, j) is sea that isn't hit
     * @param position x-y coordinates
     * @return true if integer in position (i, j) is 1, otherwise false
     */
    public boolean isSea(IntPair position){
        try{
            if(this.board[position.i_pos][position.j_pos] == 1) return true;
        }
        catch(Exception arrayIndexOutOfBoundsException){
            return false;
        }
        return false;
    }

    /**
     * Getter method for information in given position (i, j)
     * @param i x-coordinate
     * @param j y-coordinate
     * @return integer in given position
     */
    public int getPosition(int i, int j){
        return this.board[i][j];
    }

}