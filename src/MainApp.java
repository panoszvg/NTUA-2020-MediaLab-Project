import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
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
 
public class MainApp extends Application implements Initializable {

    @FXML
    private GridPane playerGrid;
    @FXML
    private GridPane enemyGrid;
    @FXML
    private Label playerShipsAlive;
    public void setPlayerShipsAlive(int a) {
        this.playerShipsAlive.setText("Ships Alive: " + a);
    }
    @FXML
    private Label playerSuccessfulShots;
    public void setPlayerSuccessfulShots(String s) {
        this.playerSuccessfulShots.setText("Successful Shots Average: " + s + "%");
    }
    @FXML
    private Label playerPoints;
    public void setPlayerPoints(int a) {
        this.playerPoints.setText("Player Points: " + a);;
    }
    @FXML
    private Label enemyShipsAlive;
    public void setEnemyShipsAlive(int a) {
        this.enemyShipsAlive.setText("Ships Alive: " + a);
    }
    @FXML
    private Label enemySuccessfulShots;
    public void setEnemySuccessfulShots(String s) {
        this.enemySuccessfulShots.setText("Successful Shots Average: " + s + "%");
    }
    @FXML
    private Label enemyPoints;
    public void setEnemyPoints(int a) {
        this.enemyPoints.setText("Enemy Points: " + a);;
    }
    @FXML
    private Label outputTextArea;
    public void setOutputTextArea(String s) {
        this.outputTextArea.setText(s);
    }
    @FXML
    private Label inputTextArea;
    public void setInputTextArea(String s) {
        this.inputTextArea.setText(s);
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

    private boolean noExceptions;
    public boolean getNoExceptions(){
        return noExceptions;
    }
    public void setNoExceptions(boolean noExceptions) {
        this.noExceptions = noExceptions;
    }
    private static String SCENARIO_ID;
    private boolean wasClicked = false;
    private IntPair clickedCoordinates;

    public class Cell extends Rectangle {
        private int x_c, y_c;

        public Cell(int x, int y, boolean playerBoard){
            super();
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

        public void isShip() {
            setFill(Color.LIGHTYELLOW);
        }

        public void makeShotShip() {
            setFill(Color.RED);
        }

        public void makeShotSea() {
            setFill(Color.BLACK);
        }

        public void isLabel(){
            setFill(Color.WHITE);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialize();
    }

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

    public void hitAction(ActionEvent t){
        setOutputTextArea("");

        /* if game is over do nothing */
        if(Gameplay.gameIsOver) {
            iTextField.setText("");
            jTextField.setText("");
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
    }

    public void startAction(ActionEvent t){
        initialize();
    }

    public void loadAction(ActionEvent t){
        /* create dialog to insert SCENARIO_ID value */
        TextInputDialog lDialog = new TextInputDialog("SCENARIO-ID");
        lDialog.setHeaderText("Select Game Scenario-ID:");
        lDialog.showAndWait();
        SCENARIO_ID = lDialog.getEditor().getText();
        Game = new Gameplay();
        Game.gameplay(this, SCENARIO_ID);
            
    }

    public void exitAction(ActionEvent t){
        Platform.exit();
    }

    /* gets enemy ships condition */
    public void enemyShipsAction(ActionEvent t){
        String str = "";
        for(int i=0; i<5; i++){
            str += (Game.getEnemyPlayer().shipArray[i].getType() + ": " + Game.getEnemyPlayer().shipArray[i].Condition() + '\n');
        }
        /* create alert to display information */
        createAlert("Enemy Ships Condition", null, str);
    }

    /* gets 5 last player shots information */
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

    /* gets 5 last enemy shots information */
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

    /* gets history/information of all player shots */
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

    /* gets history/information of all enemy shots */
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

    /* jTextField action function */
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

    /* iTextField action function */
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

    public void createAlert(String WindowTitle, String Title, String s){
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

    public void createGame(){
        Game = new Gameplay();

        if(SCENARIO_ID == null) 
            Game.gameplay(this, "default");
        else Game.gameplay(this, SCENARIO_ID);


        NumberBinding rectsAreaSize = Bindings.min(playerGrid.heightProperty(), playerGrid.widthProperty());
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
                
                /* configure size */
                playerBoard[row][col].xProperty().bind(rectsAreaSize.multiply(row).divide(10));
                playerBoard[row][col].yProperty().bind(rectsAreaSize.multiply(col).divide(10));
                playerBoard[row][col].heightProperty().bind(rectsAreaSize.divide(10));
                playerBoard[row][col].widthProperty().bind(playerBoard[row][col].heightProperty());
                enemyBoard[row][col].heightProperty().bind(rectsAreaSize.divide(10));
                enemyBoard[row][col].widthProperty().bind(enemyBoard[row][col].heightProperty());
                enemyBoard[row][col].xProperty().bind(rectsAreaSize.multiply(row).divide(10));
                enemyBoard[row][col].yProperty().bind(rectsAreaSize.multiply(col).divide(10));

            }
        }

        /* if enemy plays first, play enemy's turn */
        if(!Game.getPlayerPlaysFirst()){
            try{
            IntPair temp = Game.oneTurn(this, 0, 0);
            enemyShotsList.add(new IntPair(temp.i_pos, temp.j_pos));
            playerBoard[temp.i_pos+1][temp.j_pos+1].updatePosition(Game.getPlayerGrid().getPosition(temp.i_pos, temp.j_pos));
            } catch(AlreadyHitException e) {}
        }
    }

    @Override
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