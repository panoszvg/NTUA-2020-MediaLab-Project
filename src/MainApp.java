import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
 
/**
 * MainApp is the class that contains main() and creates
 * a JavaFX application and connects it to front-end, which
 * exists in .fxml file
 */
public class MainApp extends Application implements Initializable {

    @FXML
    private GridPane playerGrid;
    @FXML
    private GridPane enemyGrid;
    @FXML
    private Label playerShipsAlive;
    /**
     * Update front-end: show how many of player's ships are still alive
     * @param a integer that shows player's ships still alive
     */
    public void setPlayerShipsAlive(int a) {
        this.playerShipsAlive.setText("Ships Alive: " + a);
    }
    @FXML
    private Label playerSuccessfulShots;
    /**
     * Update front-end: show percentage of player's successful shots
     * @param s string that contains float (percentage) of player's successful shots
     */
    public void setPlayerSuccessfulShots(String s) {
        this.playerSuccessfulShots.setText("Successful Shots Average: " + s + "%");
    }
    @FXML
    private Label playerPoints;
    /**
     * Update front-end: show player's points
     * @param a integer containing player's points
     */
    public void setPlayerPoints(int a) {
        this.playerPoints.setText("Player Points: " + a);;
    }
    @FXML
    private Label enemyShipsAlive;
    /**
     * Update front-end: show how many of enemy's ships are still alive
     * @param a integer that shows enemy's ships still alive
     */
    public void setEnemyShipsAlive(int a) {
        this.enemyShipsAlive.setText("Ships Alive: " + a);
    }
    @FXML
    private Label enemySuccessfulShots;
    /**
     * Update front-end: show percentage of enemy's successful shots
     * @param s string that contains float (percentage) of enemy's successful shots
     */
    public void setEnemySuccessfulShots(String s) {
        this.enemySuccessfulShots.setText("Successful Shots Average: " + s + "%");
    }
    @FXML
    private Label enemyPoints;
    /**
     * Update front-end: show enemy's points
     * @param a integer containing enemy's points
     */
    public void setEnemyPoints(int a) {
        this.enemyPoints.setText("Enemy Points: " + a);;
    }
    @FXML
    private Label outputTextArea;
    /**
     * Update front-end: show message in given, used for messages
     * concerning game progress, eg. ship sunk, game over status
     * @param s String given to be displayed
     */
    public void setOutputTextArea(String s) {
        outputTextArea.setText(s);
    }
    @FXML
    private Label inputTextArea;
    /**
     * Update front-end: show message in given, used for messages
     * concerning instructions to user, eg. enter coordinates
     * @param s String given to be displayed
     */
    public void setInputTextArea(String s) {
        inputTextArea.setText(s);
    }
    @FXML
    private TextField iTextField;
    @FXML
    private TextField jTextField;
    @FXML
    private Button hitButton;
    private static Gameplay Game;

    private static Cell[][] playerBoard;
    private static Cell[][] enemyBoard;

    LinkedList<IntPair> playerShotsList;
    LinkedList<IntPair> enemyShotsList;

    private static boolean noExceptions;
    /**
     * Getter method to know whether there have been any
     * exceptions in read from file
     * @return "noExceptions" variable
     */
    public static boolean getNoExceptions(){
        return noExceptions;
    }
    /**
     * Setter method to mark whether there have been any
     * exceptions in read from file
     */
    public static void setNoExceptions(boolean noExceptions) {
        MainApp.noExceptions = noExceptions;
    }
    private static String SCENARIO_ID;
    private boolean wasClicked = false;
    private IntPair clickedCoordinates;

    /**
     * Cells that make up the boards of the app
     */
    public class Cell extends Rectangle {
        private int x_c, y_c;

        /**
         * Creates Cell by creating a 30x30 rectangle
         * @param x x-coordinate of cell (is +1 of back-end coordinates)
         * @param y y-coordinate of cell (is +1 of back-end coordinates)
         * @param playerBoard true if board is player's, false if it
         * is enemy's - needed to make clickable
         */
        public Cell(int x, int y, boolean playerBoard){
            super(30,30);
            x_c = x;
            y_c = y;
            setFill(Color.LIGHTGREY);
            setStroke(Color.BLACK);

            /* if it's the enemy's board, make it clickable */
            if(!playerBoard)
                this.setOnMouseClicked(new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent t) {
                        wasClicked = true;
                        clickedCoordinates = new IntPair(x_c-1, y_c-1);
                        hitAction(new ActionEvent());
                    }
                });
        }

        /**
         * Given a number, update front-end: make it a different color.
         * Specifically, if sea or ship was hit, show it
         * @param action integer in board[][] in Grid class
         */
        public void updatePosition(int action){
            switch(action){
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    setFill(Color.RED);
                    break;
                case 3:
                    setFill(Color.BLACK);
                    break;
            }
        }

        /**
         * Update front-end: make cell lightyellow 
         * since it's a ship (for player)
         */
        public void isShip() {
            setFill(Color.LIGHTYELLOW);
        }

        /**
         * Update front-end: make cell gray
         * since it was a ship (for enemy post-game)
         */
        public void wasShip(){
            setFill(Color.GRAY);
        }

        /**
         * Update front-end: make cell red
         * since it's a ship that was shot
         */
        public void makeShotShip() {
            setFill(Color.RED);
        }

        /**
         * Update front-end: make cell black
         * since it's sea that was shot
         */
        public void makeShotSea() {
            setFill(Color.BLACK);
        }
    }

    @Override
    /** 
     * Needed to load game from .fxml
     */
    public void initialize(URL location, ResourceBundle resources) {
        initialize();
    }

    /**
     * Opted to create new function, in order to be able to call it 
     * if game is restarted and everything needs to be reinitialised
     */
    public void initialize() {
    
        setInputTextArea("Enter the coordinates (i, j) for your move: ");
        setPlayerShipsAlive(5);
        setEnemyShipsAlive(5);
        setPlayerSuccessfulShots("0.00");
        setEnemySuccessfulShots("0.00");
        setPlayerPoints(0);
        setEnemyPoints(0);
        playerShotsList = new LinkedList<IntPair>();
        enemyShotsList = new LinkedList<IntPair>();
        /* Start a game */
        noExceptions = true;
        createGame();

    }

    /**
     * What happens when a player hits a position in grid - game commences:
     * a turn is played and front-end is updated accordingly
     * @param t ActionEvent that led to this function being called
     */
    public void hitAction(ActionEvent t){
        setOutputTextArea("");

        /* if game is over do nothing */
        if(Gameplay.gameIsOver) {
            iTextField.setText("");
            jTextField.setText("");
            if(Game.getPlayer().getPoints() > Game.getEnemyPlayer().getPoints())
                setOutputTextArea("You won!");
            else if(Game.getPlayer().getPoints() < Game.getEnemyPlayer().getPoints())
                setOutputTextArea("You lost.");
            else setOutputTextArea("It's a tie.");
            return;
        }
        
        /* it requires correct SCENARIO_ID to function */
        if(!noExceptions){
            createAlert(null, "Can't play this scenario", "Please provide a valid scenario to start the game.");
            iTextField.setText("");
            jTextField.setText("");
            return;
        }

        int iCo = 0;
        int jCo = 0; 
        /* if it wasn't clicked take it from the textfields */
        if (!wasClicked)
            try{
                iCo = Integer.parseInt(iTextField.getText());
                jCo = Integer.parseInt(jTextField.getText());
                iTextField.setText("");
                jTextField.setText("");
                if(iCo < 0 || iCo > 9 || jCo < 0 || jCo > 9){
                    setInputTextArea("Please insert a valid number (0 <= i,j <= 9)");
                    return;
                }
            } catch(NumberFormatException nfe) {
                iTextField.setText("");
                jTextField.setText("");
                setInputTextArea("Please insert valid input (numbers)");
                return;
            }
        /* if it was clicked take it from the clickedCoordinates variable */
        else {iCo = clickedCoordinates.i_pos;
              jCo = clickedCoordinates.j_pos;
              clickedCoordinates = null;
              wasClicked = false;
              iTextField.setText("");
              jTextField.setText("");
            }
        try{ 
            IntPair updatePositions = new IntPair(-1, -1);
            updatePositions = Game.oneTurn(this, iCo, jCo);
            // update history & board
            playerShotsList.add(new IntPair(iCo, jCo));
            enemyBoard[iCo+1][jCo+1].updatePosition(Game.getEnemyGrid().getPosition((iCo), (jCo)));
            enemyShotsList.add(new IntPair(updatePositions.i_pos, updatePositions.j_pos));
            playerBoard[updatePositions.i_pos+1][updatePositions.j_pos+1].updatePosition(Game.getPlayerGrid().getPosition(updatePositions.i_pos, updatePositions.j_pos));
        } catch(AlreadyHitException ahe){
            iTextField.setText("");
            jTextField.setText("");
            setInputTextArea("That position was already hit, please choose a different one");
        }
        
        /* Update ShipsAlive */
        int pShipsAlive = 5;
        int eShipsAlive = 5;
        for(int i=0; i<5; i++){
            if(Game.getPlayer().shipArray[i].Condition() == "Sunk") pShipsAlive--;
            if(Game.getEnemyPlayer().shipArray[i].Condition() == "Sunk") eShipsAlive--;
        }
        setPlayerShipsAlive(pShipsAlive);
        setEnemyShipsAlive(eShipsAlive);
        /* Update Successful Shots Average */
        setPlayerSuccessfulShots(String.format("%.2f", ((double)Game.getPlayer().getSuccessfulShots()/(40 - Game.getPlayer().getMoves()))*100));
        setEnemySuccessfulShots(String.format("%.2f", ((double)Game.getEnemyPlayer().getSuccessfulShots()/(40 - Game.getEnemyPlayer().getMoves()))*100));
        /* Update Points */
        setPlayerPoints(Game.getPlayer().getPoints());
        setEnemyPoints(Game.getEnemyPlayer().getPoints());

        /* After game is over reveal enemy ships player didn't sink */
        if(Gameplay.gameIsOver)
            for(int i=0; i<10; i++)
                for(int j=0; j<10; j++)
                    if(Game.getEnemyGrid().getPosition(i, j) == 1) 
                        enemyBoard[i][j].wasShip();
                
    }

    /**
     * Start Game loaded (clean slate)
     * @param t ActionEvent that led to this function being called
     */
    public void startAction(ActionEvent t){
        initialize();
    }

    /**
     * Opens to dialog so that user gives input (which
     * file to read from)
     * @param t ActionEvent that led to this function being called
     */
    public void loadAction(ActionEvent t){
        /* create dialog to insert SCENARIO_ID value */
        TextInputDialog lDialog = new TextInputDialog("SCENARIO-ID");
        lDialog.setHeaderText("Select Game Scenario-ID:");
        lDialog.showAndWait();
        SCENARIO_ID = lDialog.getEditor().getText();
        Game = new Gameplay();
        Game.gameplay(SCENARIO_ID);
            
    }

    /**
     * Function that exits this application
     * @param t
     */
    public void exitAction(ActionEvent t){
        Platform.exit();
    }

    /**
     * Gets enemy ships condition and displays it
     * @param t ActionEvent that led to this function being called
     */
    public void enemyShipsAction(ActionEvent t){
        String str = "";
        for(int i=0; i<5; i++){
            str += (Game.getEnemyPlayer().shipArray[i].getType() + ": " + Game.getEnemyPlayer().shipArray[i].Condition() + '\n');
        }
        /* create alert to display information */
        createAlert("Enemy Ships Condition", null, str);
    }


    /**
     * Gets 5 last player shots information and displays it
     * @param t ActionEvent that led to this function being called
     */
    public void playerShotsAction(ActionEvent t){
        String str = "";
        for(int i=playerShotsList.size()-1; i>=((playerShotsList.size() < 5) ? 0 : playerShotsList.size()-5); i--){
            int iList = playerShotsList.get(i).i_pos;
            int jList = playerShotsList.get(i).j_pos;
            int shotResult = Game.getEnemyGrid().getPosition(iList, jList);
            String shipType = "";
            if(Game.getEnemyGrid().wasHit(new IntPair(iList, jList)))
                shipType = Game.getEnemyPlayer().shipArray[Game.getEnemyPlayer().findShip(new IntPair(iList, jList))].getType();
            
                str += "Position:{" + String.valueOf(iList) + "," + String.valueOf(jList) + "}    "
                 + ((shotResult==2) ? "Hit -> " + shipType : "Missed") + '\n';
        }
        /* create alert to display information */
        createAlert("Player Shots Information", null, str);
    }

    /**
     * Gets 5 last enemy shots information and displays it
     * @param t ActionEvent that led to this function being called
     */
    public void enemyShotsAction(ActionEvent t){
        String str = "";
        for(int i=enemyShotsList.size()-1; i>=((enemyShotsList.size() < 5) ? 0 : enemyShotsList.size()-5); i--){
            int iList = enemyShotsList.get(i).i_pos;
            int jList = enemyShotsList.get(i).j_pos;
            int shotResult = Game.getPlayerGrid().getPosition(iList, jList);
            String shipType = "";
            if(Game.getPlayerGrid().wasHit(new IntPair(iList, jList)))
                shipType = Game.getPlayer().shipArray[Game.getPlayer().findShip(new IntPair(iList, jList))].getType();
            
                str += "Position:{" + String.valueOf(iList) + "," + String.valueOf(jList) + "}    "
                 + ((shotResult==2) ? "Hit -> " + shipType : "Missed") + '\n';
        }
        /* create alert to display information */
        createAlert("Enemy Shots Information", null, str);
    }

    /**
     * Gets history/information of all player shots and displays it
     * @param t ActionEvent that led to this function being called
     */
    public void playerHistoryAction(ActionEvent t){
        String str = "";
        for(int i=playerShotsList.size()-1; i>=0; i--){
            int iList = playerShotsList.get(i).i_pos;
            int jList = playerShotsList.get(i).j_pos;
            int shotResult = Game.getEnemyGrid().getPosition(iList, jList);
            String shipType = "";
            if(Game.getEnemyGrid().wasHit(new IntPair(iList, jList)))
                shipType = Game.getEnemyPlayer().shipArray[Game.getEnemyPlayer().findShip(new IntPair(iList, jList))].getType();
            
                str += "Position:{" + String.valueOf(iList) + "," + String.valueOf(jList) + "}    "
                 + ((shotResult==2) ? "Hit -> " + shipType : "Missed") + '\n';
        }
        /* create alert to display information */
        createAlert("Player Shots History", null, str);
    }

    /**
     * Gets history/information of all enemy shots and displays it
     * @param t ActionEvent that led to this function being called
     */
    public void enemyHistoryAction(ActionEvent t){
        String str = "";
        for(int i=enemyShotsList.size()-1; i>=0; i--){
            int iList = enemyShotsList.get(i).i_pos;
            int jList = enemyShotsList.get(i).j_pos;
            int shotResult = Game.getPlayerGrid().getPosition(iList, jList);
            String shipType = "";
            if(Game.getPlayerGrid().wasHit(new IntPair(iList, jList)))
                shipType = Game.getPlayer().shipArray[Game.getPlayer().findShip(new IntPair(iList, jList))].getType();
            
                str += "Position:{" + String.valueOf(iList) + "," + String.valueOf(jList) + "}    "
                 + ((shotResult==2) ? "Hit -> " + shipType : "Missed") + '\n';
        }
        /* create alert to display information */
        createAlert("Enemy Shots History", null, str);
    }

    /**
     * When user presses Left Arrow Key or "Enter" (and there is no
     * input in previous TextField) moves to it (jTextField -> iTextField)
     * @param event ActionEvent that led to this function being called
     */
    public void handleEnterPressed(KeyEvent event){
        /* move left */
        if (event.getCode() == KeyCode.LEFT) {
            iTextField.requestFocus();    
        }
        if (event.getCode() == KeyCode.ENTER)
            /* if no input in iTextField go there */
            if(iTextField.getText().trim().isEmpty())
                iTextField.requestFocus();
            /* otherwise go there and Hit */
            else {
                iTextField.requestFocus();
                hitAction(new ActionEvent());
            }
        

    }

    /**
     * When user presses Right Arrow Key or "Enter" (and there is no
     * input in next TextField) moves to it (iTextField -> jTextField)
     * @param event ActionEvent that led to this function being called
     */
    public void moveToNextTextField(KeyEvent event){
        /* move right */
        if (event.getCode() == KeyCode.RIGHT) {
            jTextField.requestFocus();
        }
        /* if no input in jTextField go there */
        if (event.getCode() == KeyCode.ENTER)
            if (jTextField.getText().trim().isEmpty())
                jTextField.requestFocus();
            /* otherwise remain here and Hit */
            else hitAction(new ActionEvent());
    }

    /**
     * Creates and displays alert with given information
     * @param WindowTitle String that is displayed as window title
     * @param Title String that is the title inside the window
     * @param s String to be written as a more detailed explaination
     */
    public static void createAlert(String WindowTitle, String Title, String s){
        Alert alert = new Alert(AlertType.INFORMATION);
        if(WindowTitle == null) alert.setTitle("ALERT!");
        else alert.setTitle(WindowTitle);
        alert.setHeaderText(Title);
        alert.setGraphic(null);
        alert.setContentText(s);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Creates game, also initialising front-end grids and plays a turn
     * if enemy is supposed to play first in this game
     */
    public void createGame(){
        Game = new Gameplay();
        setOutputTextArea("");

        if(SCENARIO_ID == null) 
            Game.gameplay("default");
        else Game.gameplay(SCENARIO_ID);


        playerBoard = new Cell[11][11];
        enemyBoard = new Cell[11][11];

        /* Create all cells for both grids */
        for(int row=0; row<=10; row++){      
            for(int col=0; col<=10; col++){

                if(row==0 && col==0) continue;
                if(row==0){
                    /* add label to playerGrid */
                    Label label = new Label();
                    label.setMinHeight(30.0);
                    label.setMinWidth(30.0);
                    label.setAlignment(Pos.CENTER);
                    label.setText(Integer.toString(col-1));
                    label.setFont(new Font("Arial", 20));
                    playerGrid.add(label, col, row);
                    /* add label to enemyGrid */
                    label = new Label();
                    label.setMinHeight(30.0);
                    label.setMinWidth(30.0);
                    label.setAlignment(Pos.CENTER);
                    label.setText(Integer.toString(col-1));
                    label.setFont(new Font("Arial", 20));
                    enemyGrid.add(label, col, row);
                    continue;
                }

                if(col==0){
                    /* add label to playerGrid */
                    Label label = new Label();
                    label.setMinWidth(30.0);
                    label.setAlignment(Pos.CENTER);
                    label.setText(Integer.toString(row-1));
                    label.setFont(new Font("Arial", 20));
                    playerGrid.add(label, col, row);
                    /* add label to enemyGrid */
                    label = new Label();
                    label.setMinWidth(30.0);
                    label.setAlignment(Pos.CENTER);
                    label.setText(Integer.toString(row-1));
                    label.setFont(new Font("Arial", 20));
                    enemyGrid.add(label, col, row);
                    continue;
                }

                /* create Cells in Cell[][] arrays */
                playerBoard[row][col] = new Cell(row, col, true);
                if(noExceptions && Game.getPlayerGrid().isShip(new IntPair(row-1, col-1))){
                    /* if it's a ship change its color */
                    playerBoard[row][col].isShip();
                }
                enemyBoard[row][col] = new Cell(row, col, false);
                /* add Cells to their respective Gridpanes */
                playerGrid.add(playerBoard[row][col], col, row);
                enemyGrid.add(enemyBoard[row][col], col, row);
            }
        }

        /* if enemy plays first, play enemy's turn */
        if(!Game.getPlayerPlaysFirst()){
            try{
            setOutputTextArea("Enemy plays first.");
            IntPair temp = Game.oneTurn(this, 0, 0);
            enemyShotsList.add(new IntPair(temp.i_pos, temp.j_pos));
            playerBoard[temp.i_pos+1][temp.j_pos+1].updatePosition(Game.getPlayerGrid().getPosition(temp.i_pos, temp.j_pos));
            } catch(AlreadyHitException e) {}
        }
    }

    @Override
    /**
     * start function needed to initialise stage/window to play in
     */
    public void start(Stage primaryStage) {
        
        Parent root = null;
        
        try{
        root = FXMLLoader.load(getClass().getClassLoader().getResource("javafx-project.fxml"));
        } catch(Exception e){e.printStackTrace();}
        primaryStage.setScene(new Scene(root,1100,700));  
        primaryStage.setTitle("MediaLab Battleship");
        primaryStage.show();

    }
}