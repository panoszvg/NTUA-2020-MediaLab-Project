

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
        for(Integer i=0; i<10; i++)
            for(Integer j=0; j<10; j++)
                board[i][j] = 0;
    }

    /*Prints the board with no filter - for player*/
    public void printUnfiltered(){
        for(Integer i=0; i<10; i++){
            if(i!=0) System.out.println();
            for(Integer j=0; j<10; j++)
                System.out.print(board[i][j] + " ");
        }
    }

    /*Prints the board with filtered - for enemy*/
    public void printFiltered(){
        for(Integer i=0; i<10; i++){
            if(i!=0) System.out.println();
            for(Integer j=0; j<10; j++)
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
    }

}