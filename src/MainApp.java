import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
// import javafx.event.ActionEvent;
// import javafx.event.EventHandler;
// import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
 
public class MainApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public class Cell extends Rectangle {
        
        public Cell(int x, int y, boolean playerBoard){
            super(30, 30);
            setFill(Color.LIGHTGREY);
            setStroke(Color.BLACK);
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
        
        /* Create main Pane -> BorderPane and scene */
        BorderPane root = new BorderPane();  
        Scene scene = new Scene(root,1100,700);
        /* Create Menu */  
        MenuBar menubar = new MenuBar();  
        Menu FileMenu = new Menu("Application");  
        MenuItem filemenu1=new MenuItem("Start");  
        MenuItem filemenu2=new MenuItem("Load");  
        MenuItem filemenu3=new MenuItem("Exit");  
        Menu EditMenu=new Menu("Details");  
        MenuItem EditMenu1=new MenuItem("Enemy Ships");  
        MenuItem EditMenu2=new MenuItem("Player Ships");  
        MenuItem EditMenu3=new MenuItem("Enemy Shots");  
        EditMenu.getItems().addAll(EditMenu1,EditMenu2,EditMenu3);  
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
        Cell[][] playerCells = new Cell[10][10];
        Cell[][] enemyCells = new Cell[10][10];

        /* Create all cells for both grids */
        for(int row=0; row<10; row++)
            for(int col=0; col<10; col++){
                playerCells[row][col] = new Cell(row, col, true);
                enemyCells[row][col] = new Cell(row, col, false);
                playerGrid.add(playerCells[row][col], col, row);
                enemyGrid.add(enemyCells[row][col], col, row);
            }
        

        /* place HBox in the middle */    
        root.setCenter(playBox);
        /* Format App Window */
        FileMenu.getItems().addAll(filemenu1,filemenu2,filemenu3);  
        menubar.getMenus().addAll(FileMenu,EditMenu);  
        primaryStage.setScene(scene);  
        primaryStage.setTitle("MediaLab Battleship");
        primaryStage.show();  
        
    }
}