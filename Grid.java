

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
    }

    /* Hits the position (i,j) it is given */
    public void Hit(int i, int j){
        switch(this.board[i][j]){
            case 0: /* Sea */
                this.board[i][j] = 3; /* becomes Hit Sea */
                break;
            case 1: /* Ship */
                this.board[i][j] = 2; /* becomes Hit Ship */
                break;
            case 2: /* Hit Ship - is already hit */
                // Throw AlreadyHit Exception
                break;
            case 3: /* Hit Sea - is already hit */
                // Throw AlreadyHit Exception
                break;    
        }
    }

    /* Set a specific position (i,j) as a specific input (setNumber) */
    public void Set(int i, int j, int setNumber){
        if(i<0 || i>3 || j<0 || j>3) ;//Throw InvalidNumberToSet Exception
        else {
            this.board[i][j] = setNumber;
        }
    }

}