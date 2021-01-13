import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
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

    public class Cell extends Rectangle {
        
        public Cell(int x, int y, boolean playerBoard){
            super();
            //super(40, 40);
            setFill(Color.LIGHTGREY);
            setStroke(Color.BLACK);
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

    }

    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialize();
    }

    public void initialize() {
    
        setInputTextArea("Enter the coordinates (i, j) for your move: ");
        setPlayerShipsAlive(5);
        setEnemyShipsAlive(5);
        playerShotsList = new LinkedList<IntPair>();
        enemyShotsList = new LinkedList<IntPair>();
        /* Start a game */
        Game = new Gameplay();
        Game.gameplay();
        
        NumberBinding rectsAreaSize = Bindings.min(playerGrid.heightProperty(), playerGrid.widthProperty());
        playerBoard = new Cell[10][10];
        enemyBoard = new Cell[10][10];

        /* Create all cells for both grids */
        for(int row=0; row<10; row++)
            for(int col=0; col<10; col++){
                playerBoard[row][col] = new Cell(row, col, true);
                if(Game.getPlayerGrid().isShip(new IntPair(row, col))){
                    playerBoard[row][col].isShip();
                }
                enemyBoard[row][col] = new Cell(row, col, false);
                playerGrid.add(playerBoard[row][col], col, row);
                enemyGrid.add(enemyBoard[row][col], col, row);
                playerBoard[row][col].xProperty().bind(rectsAreaSize.multiply(row).divide(10));
                playerBoard[row][col].yProperty().bind(rectsAreaSize.multiply(col).divide(10));
                playerBoard[row][col].heightProperty().bind(rectsAreaSize.divide(10));
                playerBoard[row][col].widthProperty().bind(playerBoard[row][col].heightProperty());
                enemyBoard[row][col].heightProperty().bind(rectsAreaSize.divide(10));
                enemyBoard[row][col].widthProperty().bind(enemyBoard[row][col].heightProperty());
                enemyBoard[row][col].xProperty().bind(rectsAreaSize.multiply(row).divide(10));
                enemyBoard[row][col].yProperty().bind(rectsAreaSize.multiply(col).divide(10));
                continue;
            }

        if(!Game.getPlayerPlaysFirst()){
            try{
            IntPair temp = Game.oneTurn(this, 0, 0);
            enemyShotsList.add(new IntPair(temp.i_pos+1, temp.j_pos+1));
            playerBoard[temp.i_pos][temp.j_pos].updatePosition(Game.getPlayerGrid().getPosition(temp.i_pos, temp.j_pos));
            } catch(AlreadyHitException e) {}
        }

    }

    @FXML
    public void hitAction(ActionEvent t){
        setOutputTextArea("");
        int iCo = 0;
        int jCo = 0; 
        try{
            iCo = Integer.parseInt(iTextField.getText()); iCo--; // Immediate
            jCo = Integer.parseInt(jTextField.getText()); jCo--; // Conversion
            iTextField.setText("");
            jTextField.setText("");
            if(iCo < 0 || iCo > 9 || jCo < 0 || jCo > 9){
                setInputTextArea("Please insert a valid number (1 <= i,j <= 10)");
                return;
            }
        } catch(NumberFormatException nfe) {
            iTextField.setText("");
            jTextField.setText("");
            setInputTextArea("Please insert valid input (numbers)");
            return;
        }
        try{ 
            IntPair updatePositions = new IntPair(-1, -1);
            updatePositions = Game.oneTurn(this, iCo, jCo);
            // update history & board
            playerShotsList.add(new IntPair(iCo+1, jCo+1));
            enemyBoard[iCo][jCo].updatePosition(Game.getEnemyGrid().getPosition((iCo), (jCo)));
            enemyShotsList.add(new IntPair(updatePositions.i_pos+1, updatePositions.j_pos+1));
            playerBoard[updatePositions.i_pos][updatePositions.j_pos].updatePosition(Game.getPlayerGrid().getPosition(updatePositions.i_pos, updatePositions.j_pos));
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

    @FXML
    public void startAction(ActionEvent t){
        //System.out.println("Begun new game");
        initialize();
    }

    // Load
    @FXML
    public void loadAction(ActionEvent t){
        
        //
        //
        //
        //
        
        try{
            Gameplay.read(Game.getPlayer(), Game.getPlayerGrid(), Game.getEnemyPlayer(), Game.getEnemyGrid());
        }
        catch(OversizeException oversizeException){System.out.println(oversizeException.getMessage()); return;}
        catch (OverlapTilesException overlapTilesException){System.out.println(overlapTilesException.getMessage()); return;}
        catch (AdjacentTilesException adjacentTilesException){System.out.println(adjacentTilesException.getMessage()); return;}
        catch (InvalidCountException invalidCountException){System.out.println(invalidCountException.getMessage()); return;}
    }

    @FXML
    public void exitAction(ActionEvent t){
        Platform.exit();
    }

    @FXML
    public void enemyShipsAction(ActionEvent t){
        String str = "";
        for(int i=0; i<5; i++){
            str += (Game.getEnemyPlayer().shipArray[i].getType() + ": " + Game.getEnemyPlayer().shipArray[i].Condition() + '\n');
        }
        // enemyShipsPopup.show();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Enemy Ships Condition");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText(str);
        alert.showAndWait();
    }

    @FXML
    public void playerShotsAction(ActionEvent t){
        String str = "";
        for(int i=playerShotsList.size()-1; i>=((playerShotsList.size() < 5) ? 0 : playerShotsList.size()-5); i--){
            int iList = playerShotsList.get(i).i_pos;
            int jList = playerShotsList.get(i).j_pos;
            int shotResult = Game.getEnemyGrid().getPosition(iList-1, jList-1);
            String shipType = "";
            if(Game.getEnemyGrid().wasHit(new IntPair(iList-1, jList-1)))
                shipType = Game.getEnemyPlayer().shipArray[Game.getEnemyPlayer().findShip(new IntPair(iList-1, jList-1))].getType();
            
                str += "Position:{" + String.valueOf(iList) + "," + ((iList == 10 || jList == 10) ? "" : " ") + String.valueOf(jList) + "}    "
                 + ((iList == 10 && jList == 10) ? "" : "  ") + ((iList != 10 && jList != 10) ? " " : "") + ((shotResult==2) ? "Hit -> " + shipType : "Missed") + '\n';
        }
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Player Shots Information");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText(str);
        alert.showAndWait();
    }

    @FXML
    public void enemyShotsAction(ActionEvent t){
        String str = "";
        for(int i=enemyShotsList.size()-1; i>=((enemyShotsList.size() < 5) ? 0 : enemyShotsList.size()-5); i--){
            int iList = enemyShotsList.get(i).i_pos;
            int jList = enemyShotsList.get(i).j_pos;
            int shotResult = Game.getPlayerGrid().getPosition(iList-1, jList-1);
            String shipType = "";
            if(Game.getPlayerGrid().wasHit(new IntPair(iList-1, jList-1)))
                shipType = Game.getPlayer().shipArray[Game.getPlayer().findShip(new IntPair(iList-1, jList-1))].getType();
            
                str += "Position:{" + String.valueOf(iList) + "," + ((iList == 10 || jList == 10) ? "" : " ") + String.valueOf(jList) + "}    "
                 + ((iList == 10 && jList == 10) ? "" : "  ") + ((iList != 10 && jList != 10) ? " " : "") + ((shotResult==2) ? "Hit -> " + shipType : "Missed") + '\n';
        }
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Enemy Shots Information");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText(str);
        alert.showAndWait();
    }

    @FXML
    public void playerHistoryAction(ActionEvent t){
        String str = "";
        for(int i=playerShotsList.size()-1; i>=0; i--){
            int iList = playerShotsList.get(i).i_pos;
            int jList = playerShotsList.get(i).j_pos;
            int shotResult = Game.getEnemyGrid().getPosition(iList-1, jList-1);
            String shipType = "";
            if(Game.getEnemyGrid().wasHit(new IntPair(iList-1, jList-1)))
                shipType = Game.getEnemyPlayer().shipArray[Game.getEnemyPlayer().findShip(new IntPair(iList-1, jList-1))].getType();
            
                str += "Position:{" + String.valueOf(iList) + "," + ((iList == 10 || jList == 10) ? "" : " ") + String.valueOf(jList) + "}    "
                 + ((iList == 10 && jList == 10) ? "" : "  ") + ((iList != 10 && jList != 10) ? " " : "") + ((shotResult==2) ? "Hit -> " + shipType : "Missed") + '\n';
        }
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Player Shots History");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText(str);
        alert.showAndWait();
    }

    @FXML
    public void enemyHistoryAction(ActionEvent t){
        String str = "";
        for(int i=enemyShotsList.size()-1; i>=0; i--){
            int iList = enemyShotsList.get(i).i_pos;
            int jList = enemyShotsList.get(i).j_pos;
            int shotResult = Game.getPlayerGrid().getPosition(iList-1, jList-1);
            String shipType = "";
            if(Game.getPlayerGrid().wasHit(new IntPair(iList-1, jList-1)))
                shipType = Game.getPlayer().shipArray[Game.getPlayer().findShip(new IntPair(iList-1, jList-1))].getType();
            
                str += "Position:{" + String.valueOf(iList) + "," + ((iList == 10 || jList == 10) ? "" : " ") + String.valueOf(jList) + "}    "
                 + ((iList == 10 && jList == 10) ? "" : "  ") + ((iList != 10 && jList != 10) ? " " : "") + ((shotResult==2) ? "Hit -> " + shipType : "Missed") + '\n';
        }
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Enemy Shots History");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText(str);
        alert.showAndWait();
    }

    @FXML
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

    public static void main(String[] args) {
        launch(args);
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