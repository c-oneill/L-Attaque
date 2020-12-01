package stratego;

import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class StrategoView extends Application implements Observer{

    private final int SQUARE_SIZE = 70;
    private final int VGAP_PADDING = 8;
    private final int HGAP_PADDING = 8;
    private final int INSETS_PADDING = 4;
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private final int COLUMNS = 10;
    private final int ROWS = 10;
    private final int PIECES = 40;
    private final int WINDOW_HEIGHT = 1000;
    private final int WINDOW_WIDTH = 1000;
    private final int CHATBOX_WIDTH = 300;
    
    private Stage stage;
  //  private VBox window;
    private BorderPane window;
    private GridPane board;
    private GridPane piecesBox;
    private VBox chatBox;
    private MenuBar menuBar; 
    private TextField chatDisplay;
    private TextField chatEntry;
    //private StrategoController controller;
    
    private boolean inputEnabled;
    private boolean isServer;
    private boolean isHuman;
    
    /**
     * <ul><b><i>start</i></b></ul>
     * <ul><ul><p><code>public void start (Stage stage) </code></p></ul>
     *
     * The main entry point for all JavaFX applications.The start method is called after 
     * the init method has returned,and after the system is ready for the application to begin running. 
     *
     * @param stage - the primary stage for this application, onto which the application scene can be set.
     * 
     * @author Kristopher Rangel
     */
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(window);
        
        // Showing stage
        try {
            inputEnabled = false;
            
            // setting the stage
            stage.setHeight(WINDOW_HEIGHT);
            stage.setWidth(WINDOW_WIDTH);
            stage.setTitle("Stratego");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            this.stage = stage;

        }catch(Exception e) {
            System.out.println("Error with javafx while initializing the UI.");
            e.printStackTrace();
        }
        
    }
    
    /**
     * <ul><b><i>init</i></b></ul>
     * <ul><ul><p><code>public void init () </code></p></ul>
     *
     *The application initialization method. This method is called immediately
     *after the Application class is loaded and constructed.
     *
     *<p>This method initializes the element of the scene.</p>
     *
     * @author Kristopher Rangel
     *
     */
    public void init() {
        
        initMenuBar();
        initBoard();
        createGrid(board, ROWS, COLUMNS, Color.WHEAT, Color.RED);
        initPieces();
        createGrid(piecesBox, 2, 6, Color.ALICEBLUE, Color.BLACK);
        initChatBox();
        
        window = new BorderPane();        
        window.setTop(menuBar);
        window.setCenter(board);
        window.setRight(chatBox);
        window.setBottom(piecesBox);
        
    }
    
    private void initChatBox() {
        chatBox = new VBox();
        chatBox.setPrefWidth(CHATBOX_WIDTH);
        chatBox.styleProperty().set("-fx-border-color: green;"); // border
        chatDisplay = new TextField(">> This is the chat box.");
        chatDisplay.setEditable(false);
        chatDisplay.setPrefHeight(WINDOW_HEIGHT);
        chatDisplay.setAlignment(Pos.TOP_LEFT);
        chatEntry = new TextField("Enter Chat here.");
        chatBox.getChildren().addAll(chatDisplay, chatEntry);
        VBox.setVgrow(chatEntry, Priority.ALWAYS);
    }
    
    /**
     * <ul><b><i>initMenuBar</i></b></ul>
     * <ul><ul><p><code>private void initMenuBar () </code></p></ul>
     *
     * Sets up the menu bar and associated menu options.
     *
     * @author Kristopher Rangel
     */
    private void initMenuBar() {
        menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem newGame = new MenuItem("New Game");
        newGame.setOnAction(e -> { getNewGameOptions(); });
        fileMenu.getItems().add(newGame);
        menuBar.getMenus().add(fileMenu);
    }
    
    
    /**
     * <ul><b><i>getNewGameOptions</i></b></ul>
     * <ul><ul><p><code>private void getNewGameOptions () </code></p></ul>
     *
     *
     * @author Kristopher Rangel
     */
    private void getNewGameOptions() {

    }
    
    /**
     * <ul><b><i>initBoard</i></b></ul>
     * <ul><ul><p><code> private void initBoard () </code></p></ul>
     *
     * A run-once function that sets the initial state of the Stratego board.
     * 
     * <p>This function sets the board background, alignment, and padding. Then it
     * invokes the {@link #createCircles()} method.
     *
     * @author Kristopher Rangel
     */
    private void initBoard() {
        board = new GridPane();
        //board.styleProperty().set("-fx-background-image: url('https://abload.de/img/stbohjsmc.png');"
        //        + "-fx-background-repeat: no-repeat;");
        board.setPadding(new Insets(INSETS_PADDING, INSETS_PADDING, INSETS_PADDING, INSETS_PADDING));
        board.setAlignment(Pos.CENTER);
    }
    
    /**
     * <ul><b><i>createCircles</i></b></ul>
     * <ul><ul><p><code> private void createCircles () </code></p></ul>
     *
     * Creates white circle objects and adds them to the board.
     *
     * @author Kristopher Rangel
     */
    private void createGrid(GridPane grid, int rows, int columns, Color bgColor, Color borderColor) {
        grid.getChildren().clear();
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < columns; col++) {
                Rectangle r = new Rectangle(SQUARE_SIZE, SQUARE_SIZE, bgColor);   
                r.strokeProperty().set(borderColor);
                grid.add(r, col, row);
            }
        }
    }
    
    /**
     * <ul><b><i>initPieces</i></b></ul>
     * <ul><ul><p><code> private void initPieces () </code></p></ul>
     *
     * @author Kristopher Rangel
     */
    private void initPieces() {
        piecesBox = new GridPane();
        BackgroundFill bgfill = new BackgroundFill(BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY);
        Background bg = new Background(bgfill);
        piecesBox.styleProperty().set("-fx-border-color: blue;");
        piecesBox.setBackground(bg);
        piecesBox.setHgap(HGAP_PADDING);
        piecesBox.setVgap(VGAP_PADDING);
        piecesBox.setPadding(new Insets(INSETS_PADDING, INSETS_PADDING, INSETS_PADDING, INSETS_PADDING));
        piecesBox.setAlignment(Pos.BASELINE_LEFT);
    }
    /**
     * <ul><b><i>update</i></b></ul>
     * <ul><ul><p><code> public void update (Observable o, Object arg) </code></p></ul>
     *
     * This updates the view based on changes to the observed {@link StrategoModel}
     * object.
     *
     * @param o - the {@link StrategoModel} being observed
     * @param arg - a LINK_NEEDED with information related to the move
     * 
     * @author Kristopher Rangel 
     */
    public void update(Observable o, Object arg) {
        
    }
}
