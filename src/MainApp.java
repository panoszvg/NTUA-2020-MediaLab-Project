import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
 
public class MainApp extends Application {

    private Cell[][] playerBoard;
    private Cell[][] enemyBoard;

    public static void main(String[] args) {
        launch(args);
    }

    public class Cell extends Rectangle {
        
        public Cell(int x, int y, boolean playerBoard){
            super(30, 30);
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
    public void start(Stage primaryStage) {
        
        /* Start a game */
        Gameplay Game = new Gameplay();
        Game.gameplay();

        /* Create main Pane -> BorderPane and scene */
        BorderPane root = new BorderPane();  
        Scene scene = new Scene(root,1100,700);
        /* Create Menu */  
        MenuBar menubar = new MenuBar();  
        Menu ApplicationMenu = new Menu("Application");  
        MenuItem AppItem1=new MenuItem("Start");
        AppItem1.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent t){
                Gameplay Game = new Gameplay();
                Game.gameplay();
            }
        });
        MenuItem AppItem2=new MenuItem("Load");  
        MenuItem AppItem3=new MenuItem("Exit");  
        Menu DetailsMenu=new Menu("Details");  
        MenuItem DetailItem1=new MenuItem("Enemy Ships");  
        MenuItem DetailItem2=new MenuItem("Player Ships");  
        MenuItem DetailItem3=new MenuItem("Enemy Shots");  
        /* Place menu on top of BorderPane */
        root.setTop(menubar);
        /* Create HBox to place the two grids */
        HBox playBox = new HBox();
        /* Create player/enemy grids -> GridPane */
        GridPane playerGrid = new GridPane();
        GridPane enemyGrid = new GridPane();
        /* Place grids in HBox and format it */
        playBox.getChildren().addAll(playerGrid, enemyGrid);
        playBox.setPadding(new Insets(150, 20, 20, 20));
        playBox.setSpacing(100);
        playBox.setAlignment(Pos.CENTER);
        playerBoard = new Cell[10][10];
        enemyBoard = new Cell[10][10];

        /* Create all cells for both grids */
        for(int row=0; row<10; row++)
            for(int col=0; col<10; col++){
                playerBoard[row][col] = new Cell(row, col, true);
                if(Game.getPlayerGrid().isShip(new IntPair(row, col)))
                    playerBoard[row][col].isShip();
                enemyBoard[row][col] = new Cell(row, col, false);
                //////////////////////////////////////////////// Fix implementation
                playerGrid.add(playerBoard[row][col], col, row);
                enemyGrid.add(enemyBoard[row][col], col, row);
            }
        

        /* place HBox in the middle */    
        root.setCenter(playBox);

        Label i_coord = new Label();
        Label j_coord = new Label();
        TextField iTextField = new TextField();
        TextField jTextField = new TextField();
        Button hitButton = new Button("Hit!");

        VBox texts = new VBox();
        Label outputTextArea = new Label();
        outputTextArea.setPrefWidth(300);
        outputTextArea.setAlignment(Pos.CENTER);
        // outputTextArea.setEditable(false);

        hitButton.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent t){
                    int iCo = 0;
                    int jCo = 0; 
                    try{
                        iCo = Integer.parseInt(iTextField.getText());
                        jCo = Integer.parseInt(jTextField.getText());
                        if(iCo < 1 || iCo > 10 || jCo < 1 || jCo > 10){
                            outputTextArea.setText("Please insert a valid number (1 <= i,j <= 10)");
                            return;
                        }
                    } catch(NumberFormatException nfe) {
                        outputTextArea.setText("Please insert valid input (numbers)");
                        return;
                    }
                    try{ 
                        IntPair[] updatePositions = new IntPair[2];
                        updatePositions = Game.oneTurn(iCo, jCo);
                        // update board
                        enemyBoard[updatePositions[1].i_pos][updatePositions[1].j_pos].updatePosition(Game.getEnemyGrid().getPosition(updatePositions[1].i_pos, updatePositions[1].j_pos));
                        //playerBoard[updatePositions[0].i_pos][updatePositions[0].j_pos].updatePosition(Game.getPlayerGrid().getPosition(updatePositions[0].i_pos, updatePositions[0].j_pos));
                         //enemyBoard[updatePositions[1].i_pos][updatePositions[1].j_pos].updatePosition(Game.getEnemyGrid().getPosition(updatePositions[1].i_pos, updatePositions[1].j_pos));
                    } catch(AlreadyHitException ahe){
                        outputTextArea.setText("That position was already hit, please choose a different one");
                    }
            }
        });
        
        
        GridPane insertCoordinates = new GridPane();
        insertCoordinates.addColumn(0, iTextField, i_coord);
        insertCoordinates.addColumn(1, jTextField, j_coord);
        insertCoordinates.addColumn(2, hitButton);
        insertCoordinates.setHgap(20);
        insertCoordinates.setPadding(new Insets(10, 10, 40, 10));
        insertCoordinates.setAlignment(Pos.CENTER);
        texts.getChildren().addAll(outputTextArea, insertCoordinates);
        texts.setAlignment(Pos.CENTER);
        root.setBottom(texts);

        /* Format App Window */
        ApplicationMenu.getItems().addAll(AppItem1, AppItem2, AppItem3);  
        menubar.getMenus().addAll(ApplicationMenu, DetailsMenu);
        DetailsMenu.getItems().addAll(DetailItem1, DetailItem2, DetailItem3);  
        primaryStage.setScene(scene);  
        primaryStage.setTitle("MediaLab Battleship");
        primaryStage.show();  


        
    }
}