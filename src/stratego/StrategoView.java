package stratego;

import java.util.Observable;
import java.util.Observer;
import java.util.List;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import stratego.Piece.PieceType;
import javafx.beans.InvalidationListener;
/**
 * This class serves as the UI for the Stratego program.
 * 
 * <p>This class is an {@link Observer} of the {@link StrategoModel} class.
 * </p>
 * 
 * @author Kristopher Rangel
 *
 */

public class StrategoView extends Application implements Observer{

    protected static final boolean ENABLE_CONSOLE_DEBUG = false;
    
    private final int VGAP_PADDING = 8;
    private final int HGAP_PADDING = 8;
    private final int INSETS_PADDING = 4;
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private final Color BOARD_GRID_COLOR = Color.BLACK;
    private final int BOARD_SIZE = 10;
    private final int SETUP_INDEX_START = 60;
    private final int SETUP_INDEX_END = 100;
    private final int GRID_BORDERS = 3;
    private final int WINDOW_HEIGHT = 825;
    private final int WINDOW_WIDTH = 1100;
    private final int CHATBOX_WIDTH = 300;
    private final int PIECES_ROWS = 12;
    private final int PIECES_COLS = 2;
    private final int COUNT_FONT_SIZE = 14;
    private int DEFAULT_TIME = 120000; // 2 mins
    private Stage stage;
    private BorderPane window;
    private GridPane board;
    private GridPane piecesBox;
    private VBox chatBox;
    private MenuBar menuBar; 
    private TextField chatDisplay;
    private TextField chatEntry;
    private Label clockFace;
    private StrategoController controller;
    private Timer timer;
    private Button setupDone;
    private Color playerColor;
    private static ArrayList<Label> countLabels;
    private static ArrayList<PieceView> pieces; // pieces for setup
    public static boolean setupEnabled;
    private boolean inputEnabled;
    private boolean isServer;
    
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
            setupEnabled = false;
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
            if(ENABLE_CONSOLE_DEBUG) {
                System.out.println("Error with javafx while initializing the UI.");
                e.printStackTrace();
            }
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
        
        playerColor = Color.BLUE;
        controller = new StrategoController();
        controller.setModelObserver(this);
        
        initMenuBar();
        initBoard();
        initChatBox();
        initPieces();
        initTimer();

        
        window = new BorderPane();        
        window.setTop(menuBar);
        window.setCenter(board);
        window.setRight(chatBox);
        window.setLeft(piecesBox);
        
    }
    
    public void stop() {
        if(timer != null)
            timer.stopTimer();
    }
    
    private void initChatBox() {
        chatBox = new VBox();
        chatBox.setPrefWidth(CHATBOX_WIDTH);
        chatBox.styleProperty().set("-fx-border-color: green;"); // border
        chatDisplay = new TextField(">> This is the chat box.");
        chatDisplay.setEditable(false);
        chatDisplay.setPrefHeight(WINDOW_HEIGHT);
        chatDisplay.setAlignment(Pos.TOP_LEFT);
        chatDisplay.setDisable(true);
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
     * <ul><b><i>initBoard</i></b></ul>
     * <ul><ul><p><code> private void initBoard () </code></p></ul>
     *
     * A run-once function that sets the initial state of the Stratego board.
     *
     * <p> Sets the initial board grid by invoking {@link createGrid}
     *
     * @author Kristopher Rangel
     */
    private void initBoard() {
        board = new GridPane();

        board.setPadding(new Insets(INSETS_PADDING, INSETS_PADDING, INSETS_PADDING, INSETS_PADDING));
        board.setAlignment(Pos.CENTER);
        
        // creating piece layout at bottom
        board.getChildren().clear();
        int pieceIndex = -1;
        for(int row = 0; row < BOARD_SIZE; row++) {
            for(int col = 0; col < BOARD_SIZE; col++) {
                if ((row == 4 || row == 5) && (col == 2 || col == 3 || col == 6 || col == 7)) {
                    pieceIndex = -2;
                }else {
                    pieceIndex = -1;
                }
                    
                PieceView square = new PieceView(pieceIndex, BOARD_GRID_COLOR, GRID_BORDERS);
                
                // initializing square for no input
                square.setDragEnabled(false);
                square.setDropEnabled(false);
                square.setIsOnBoard(true);
                
                // adding square to board
                square.setPosition(row, col);
                board.add(square , col, row);
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
        countLabels = new ArrayList<Label>();
        pieces = new ArrayList<PieceView>();
        String[] rank_images = {"ranks_flag.png","ranks_bomb.png", "ranks_1-spy.png", "ranks_2-scout.png",
                                "ranks_3-Miner.png", "ranks_4-SGT.png", "ranks_5-LT.png", "ranks_6-CPT.png",
                                "ranks_7-MAJ.png", "ranks_8-COL.png", "ranks_9-GEN.png", "ranks_10-Marshall.png"};
        piecesBox = new GridPane();
        BackgroundFill bgfill = new BackgroundFill(BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY);
        Background bg = new Background(bgfill);
        
        piecesBox.styleProperty().set("-fx-border-color: blue;");
        
        piecesBox.setBackground(bg);
        piecesBox.setHgap(HGAP_PADDING);
        piecesBox.setVgap(VGAP_PADDING);
        piecesBox.setPadding(new Insets(INSETS_PADDING, INSETS_PADDING, INSETS_PADDING, INSETS_PADDING));
        piecesBox.setAlignment(Pos.BASELINE_LEFT);

        // creating piece layout at bottom
        piecesBox.getChildren().clear();
        int pieceIndex = 0;
        
        for(int row = 0; row < PIECES_ROWS; row += 2) {
            for(int col = 0; col < PIECES_COLS; col++) {
                PieceView pv = new PieceView(pieceIndex, playerColor);
                pv.setDropEnabled(false); // can't drop pieces onto the bottom piece tray
                pieces.add(pv);
                piecesBox.add(pv , col, row);
                
                int count = controller.checkAvailable(pv.getPieceType(), playerColor);

                pv.setLabelText(String.valueOf(count));
                Label label = pv.getLabel();
                label.setAlignment(Pos.CENTER);
                label.setMaxWidth(Double.MAX_VALUE);
                label.setFont(new Font(COUNT_FONT_SIZE));
                countLabels.add(label);
                piecesBox.add(label, col, row + 1, 1 , 1);
                
                pieceIndex++;
            }
            // Spacer for the timer
            VBox empty = new VBox(70);
            empty.setPrefWidth(70);
            piecesBox.add(empty, 0, PIECES_ROWS);
            
        }
        
        
    }

    
    
    
   
    public static int getLeft(int pieceIndex) {
        Label l = countLabels.get(pieceIndex);
        return Integer.valueOf(l.getText());
    }
    
    
    public static void updateLabels(int index, int change) {
        Label l = countLabels.get(index);
        int count = Integer.valueOf(l.getText());
        count += change;
        l.setText(String.valueOf(count));
        if(count == 0) {
            pieces.get(index).setDragEnabled(false);
        }else {
            pieces.get(index).setDragEnabled(true);
        }
    }
    
    /**
     * <ul><b><i>initTimer</i></b></ul>
     * <ul><ul><p><code>private void initTimer () </code></p></ul>
     *
     * Initializes the timer UI element.
     *
     */
    private void initTimer() {
        int row = PIECES_ROWS + 1;
        int span = 2;
        Font font = new Font(48);
        clockFace = new Label("02:00");
        clockFace.setFont(font);
        clockFace.setAlignment(Pos.CENTER_RIGHT);
        clockFace.prefWidth(200);
        clockFace.prefHeight(200);
        clockFace.setVisible(false);
        piecesBox.add(clockFace, 0, row, span, span);
        
        setupDone = new Button("Setup Done");
        setupDone.setVisible(false);
        setupDone.setPrefWidth(150);
        setupDone.autosize();
        setupDone.setAlignment(Pos.CENTER);
        setupDone.setOnAction(e -> { 
            setupBoardEnable(false);
            //TODO notify connection of setup completion
            //TODO let timer run until other player is done also
            timer.stopTimer(); 
            });
        piecesBox.add(setupDone, 0, row + 2, span, span);
    }
    
    /**
     * <ul><b><i>startTimer</i></b></ul>
     * <ul><ul><p><code> void startTimer () </code></p></ul>
     *
     * Starts the timer.
     * 
     * <p>Intitializes a {@link Timer} objects and adds a listener to
     * update the timer in the UI appropriately.
     *
     * @author Kristopher Rangel
     */
    private void startTimer() {
        clockFace.setVisible(true);
        setupDone.setVisible(true);
        timer = new Timer(DEFAULT_TIME);
        // adding listener for change in timer to update the timer display
        timer.getTime().addListener(new InvalidationListener() {
            @Override
            public void invalidated(javafx.beans.Observable arg0) {
                // updating timer display
                Platform.runLater(
                        () -> {
                            String time = timer.getTime().get();
                            clockFace.setText(" " + time);
                            if(time.equals("00:00")) {
                                endSetup();
                            }
                        } 
                );
            }
        });
        timer.startTimer();
    }
    
    /**
     * <ul><b><i>getNewGameOptions</i></b></ul>
     * <ul><ul><p><code>private void getNewGameOptions () </code></p></ul>
     *
     * This method launches a dialog box that allows the user
     * to enter Network Setup options. Invokes {@link #startNewGame} method.
     *
     * @author Kristopher Rangel
     */
    private void getNewGameOptions() {
        StrategoNewGameMenu newGameMenu = new StrategoNewGameMenu();
        newGameMenu.setX(stage.getX());
        newGameMenu.setY(stage.getY());
        newGameMenu.showAndWait();
        
        if(newGameMenu.userHitOK()) { // user hit okay to start new game
            
            // Getting user options
            String server = newGameMenu.getServer();
            int port = newGameMenu.getPort();
            isServer = newGameMenu.getCreateModeSelection();

            startNewGame(server, port);
        }
 
    }
    
    /**
     * <ul><b><i>startNewGame</i></b></ul>
     * <ul><ul><p><code>private void startNewGame (String server, int port) </code></p></ul>
     *
     * This function starts a new game with the options selected by the user.
     *
     *
     *
     * @param server - the hostname of the server
     * @param port - the port number
     *
     * @author Kristopher Rangel
     */
    private void startNewGame(String server, int port) {

        
        
        //TODO setup network connection here
        
        startTimer();
        setupBoardEnable(true);
        
        //TODO finish start new game procedures
        
    }
    
    /**
     * <ul><b><i>endSetup</i></b></ul>
     * <ul><ul><p><code>private void endSetup () </code></p></ul>
     *
     *  Performs actions required at the conclusion of the "setup" period before play.
     *
     * @author Kristopher Rangel
     */
    private void endSetup() {
        setupBoardEnable(false);
        
        //TODO after setup stuff
    }
    
    private void setupBoardEnable(boolean enableSetup) {
        setupEnabled = enableSetup;
        List<Node> setupArea = board.getChildren().subList(SETUP_INDEX_START, SETUP_INDEX_END);
        setupArea.forEach( e -> {
            PieceView pv = (PieceView) e;
            //TODO maybe disable drag on setup area
            //pv.setDragEnabled(enableSetup);
            pv.setDropEnabled(enableSetup);
        } );
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
