class Grid{
    
    private int board[][];
    /*
    What each position represents and what
    can be seen by who:

                Player  Enemy
    0:Sea         X       X
    1:Ship        X
    2:Hit Ship    X       X
    3:Hit Sea     X       X
    */

    /*Initializes the playing board*/
    Grid(){
        this.board = new int[10][10];
        for(int i=0; i<10; i++)
            for(int j=0; j<10; j++)
                this.board[i][j] = 0;
    }

    /*Prints the board with no filter - for player*/
    public void printUnfiltered(){
        for(int i=0; i<10; i++){
            if(i!=0) System.out.println();
            for(int j=0; j<10; j++)
                System.out.print(board[i][j] + " ");
        }
        System.out.println();
        System.out.println();
    }

    /*Prints the board with filtered - for enemy*/
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

    /* Hits the position (i,j) it is given - returns true if it succeeds */
    public boolean Hit(int i, int j){
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
        return true;
    }

    /* Set a specific position (i,j) as a specific input (setNumber) */
    public void Set(int i, int j, int setNumber) throws OversizeException, OverlapTilesException {
        if(i<0 || i>10 || j<0 || j>10) throw new OversizeException("OversizeException");
        else {
            if(this.board[i][j] == 1) throw new OverlapTilesException("OverlapTilesException");
            this.board[i][j] = setNumber;
        }
    }

    /* Returns true if position hasn't been hit yet */
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

    /* Returns true if position is Hit Sea (3) */
    public boolean isHitSea(int i, int j){
        try{
            if(this.board[i][j] == 3) return true;
        }
        catch(Exception arrayIndexOutOfBoundsException){
            return false;
        }
        return false;
    }

    /* Returns true if position is Hit Ship (2) */
    public boolean wasHit(IntPair position){
        try{
            if(this.board[position.i_pos][position.j_pos] == 2) return true;
        }
        catch(Exception arrayIndexOutOfBoundsException){
            return false;
        }
        return false;
    }

}