package stratego;

import java.util.Observable;
import java.util.Observer;

import java.util.List;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
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

public class StrategoView extends Application implements Observer {

    protected static final boolean ENABLE_INPUT_DEBUG = false;
    protected static final boolean ENABLE_CONSOLE_DEBUG = false;
    
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private final Color BOARD_GRID_COLOR = Color.BLACK;
    private final int BOARD_SIZE = 10;
    private final int SETUP_START_ROW = 6;
    private final int SETUP_INDEX_START = 60;
    private final int SETUP_INDEX_END = 100;

    public static double STANDARD;
    
    private double WINDOW_HEIGHT;
    private double WINDOW_WIDTH;
    private double CHATBOX_WIDTH;
    
    private double VGAP_PADDING;
    private double HGAP_PADDING;
    private double INSETS_PADDING;
    private int GRID_BORDERS;
    
    private double CLOCK_WIDTH;
    private double CLOCK_HEIGHT;
    private double SETUP_DONE_WIDTH;
    private double TIMER_FONT_SIZE;
    
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
    
    private static Color playerColor;
    private int colorInt;
    
    private static ArrayList<Label> countLabels;
    private static ArrayList<PieceView> pieces; // pieces for setup
    public static boolean setupEnabled;
    private boolean inputEnabled;
    private static boolean isServer;
    
    private boolean recvOtherSetup = false;
    private boolean sentSetup = false;
    private int msgRecvCount = 0;
    
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
            inputEnabled = true;
            
            // setting the stage
            stage.setHeight(WINDOW_HEIGHT);
            stage.setWidth(WINDOW_WIDTH);
            stage.setTitle("Stratego");
            stage.setResizable(true);
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
    	
    	// adjust to native resolution
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        double h = primScreenBounds.getHeight();
        
        STANDARD = h / 825;
        
        WINDOW_HEIGHT = STANDARD * 825;
        WINDOW_WIDTH = STANDARD * 1100;
        CHATBOX_WIDTH = STANDARD * 300;
        
        VGAP_PADDING = STANDARD * 8;
        HGAP_PADDING = STANDARD * 8;
        INSETS_PADDING = STANDARD * 4;
        GRID_BORDERS = 3;
        
        CLOCK_WIDTH = STANDARD * 200;
        CLOCK_HEIGHT = STANDARD * 200;
        SETUP_DONE_WIDTH = STANDARD * 150;
        TIMER_FONT_SIZE = STANDARD * 45;
        
        playerColor = Color.RED;
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
    
    // TODO: need more here
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
        MenuItem endGame = new MenuItem("End Game");
        endGame.setOnAction(e -> { gameOver(); }); 
        fileMenu.getItems().addAll(newGame, endGame);
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
                    
                PieceView square = new PieceView(controller, pieceIndex, BOARD_GRID_COLOR, GRID_BORDERS);
                
                // initializing square for no input
                square.setDragEnabled(false);
                square.setDropEnabled(false);
                square.setIsOnBoard(true);
                square.setIsVisible(false);
                
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
     * Initializes the "pieces box" portion of the UI to include the pieces and labels
     * of quantity remaining.
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
                PieceView pv = new PieceView(pieceIndex, BOARD_GRID_COLOR);
                pv.setDropEnabled(false); // can't drop pieces onto the bottom piece tray
                pv.setPlayerColor(playerColor);
                pv.setIsVisible(true);
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

    private void changePieceBoxColor(Color borderColor) {
        for(PieceView pv : pieces) {
            pv.setBorderColor(borderColor);
            pv.saveBorderColor(borderColor);
        }
    }
    
    /**
     * <ul><b><i>updateLabels</i></b></ul>
     * <ul><ul><p><code>private static void updateLabels () </code></p></ul>
     *
     * Updates the appropriate "pieces box" labels to indicate the current number
     * of that piece left to place on the board.
     *
     *<p>If the current number is zero, it also triggers a disable of the drag for the
     *associated piece.</p>
     *
     * @param index - the piece index of the board
     * @param change - the change requested (i.e. -1 for decrement or 1 for increment)
     * 
     * @author Kristopher Rangel
     */
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
     * Initializes the timer UI element and setup done button
     * in the left panel of the display.
     * 
     * <p>The timer and button are only displayed during setup.
     *
     * @author Kristopher Rangel
     */
    private void initTimer() {
        int row = PIECES_ROWS + 1;
        int span = 2;
        Font font = new Font(TIMER_FONT_SIZE);
        clockFace = new Label("02:00");
        clockFace.setFont(font);
        clockFace.setAlignment(Pos.CENTER_RIGHT);
        clockFace.prefWidth(CLOCK_WIDTH);
        clockFace.prefHeight(CLOCK_HEIGHT);
        clockFace.setVisible(false);
        piecesBox.add(clockFace, 0, row, span, span);
        
        setupDone = new Button("Setup Done");
        setupDone.setVisible(false);
        setupDone.setPrefWidth(SETUP_DONE_WIDTH);
        setupDone.autosize();
        setupDone.setAlignment(Pos.CENTER);
        setupDone.setOnAction(e -> { 

            //TODO let timer run until other player is done also
            timer.stopTimer(); 
            endSetup();
            });
        piecesBox.add(setupDone, 0, row + 2, span, span);
    }
    
    /**
     * <ul><b><i>startTimer</i></b></ul>
     * <ul><ul><p><code>private void startTimer () </code></p></ul>
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
     * <ul><b><i>hideTimer</i></b></ul>
     * <ul><ul><p><code>private void hideTimer () </code></p></ul>
     *
     * Hides the timer and setup done button UI elements.
     * 
     * @author Kristopher Rangel
     *
     */
    private void hideTimer() {
        clockFace.setVisible(false);
        setupDone.setVisible(false);
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
            if(isServer) {
                playerColor = Color.RED;
                colorInt = Piece.RED;
            }else {
                playerColor = Color.BLUE;
                colorInt = Piece.BLUE;
            }
            
            startNewGame(server, port);
        }
 
    }
    /**
     * <ul><b><i>exitGameOption</i></b></ul>
     * <ul><ul><p><code>private void endGameOption () </code></p></ul>
     *
     * Performs required functions of ending the game.
     *
     *@author Kristopher Rangel
     */
    private void gameOver() {
        
        //TODO communicate gameOver status to connected server/client
        
        // Showing applicable win message.
        int winner = controller.winner();
        String winMsg = "The game has end at user request.";
        if(winner == colorInt)
            winMsg = "You won the game!";
        else if(winner != 0)
            winMsg = "Your opponent won the game!";
        this.showAlert(AlertType.INFORMATION, winMsg);
        
        // cleanup
        controller.closeNetwork();
        hideTimer();
        timer.stopTimer();
        
        //TODO add end of game stuff
    }
    
    
    /**
     * <ul><b><i>startNewGame</i></b></ul>
     * <ul><ul><p><code>private void startNewGame (String server, int port) </code></p></ul>
     *
     * This function starts a new game with the options selected by the user.
     *
     * @param server - the hostname of the server
     * @param port - the port number
     *
     * @author Kristopher Rangel
     * @author Caroline O'Neill
     */
    private void startNewGame(String server, int port) 
    {
    	// TODO: disable network setup botton
    	
    	// if previous connection exists, close it
    	controller.closeNetwork(); 
    	//setup network connection
        boolean hasConnectionError = controller.buildNetwork(isServer, server, port);
        
        if(hasConnectionError) 
        {
        	showAlert(AlertType.ERROR, controller.getNetworkError());
    	} 
        else 
    	{
        	if (isServer) 
        		stage.setTitle("Stratego (Server)");
        	else 
        		stage.setTitle("Stratego (Client)");
        	
        	inputEnabled = true;
        	controller.initiateSetupListening();
        }
        
        startTimer();
        setupEnabled = true;
        changePieceBoxColor(playerColor);
        
        // Enabling drop on setup area of board
        List<Node> setupArea = board.getChildren().subList(SETUP_INDEX_START, SETUP_INDEX_END);
        setupArea.forEach( e -> {
            PieceView pv = (PieceView) e;
            pv.setDropEnabled(true);
        } );
    }
    
    /**
     * Shows an alert of the given type with the message passed.
     * 
     * @param type alert type
     * @param message message with alert
     * 
     * @author Kristopher Rangel
     */
    private void showAlert(AlertType type, String message) 
    {
        Alert alert = new Alert(type, message);
        // setting alert location to match stage location
        alert.setX(stage.getX());
        alert.setY(stage.getY());
        alert.showAndWait().filter(response -> response == ButtonType.OK);
    }
    
    /**
     * Sets Drag and Drop enabled for all PieceViews in the board.
     * 
     * @author Kristopher Rangel
     */
    private void setBoardEnable() {
        board.getChildren().iterator().forEachRemaining(e -> {
            PieceView pv = (PieceView) e;
            pv.setDropEnabled(true);
            pv.setDragEnabled(true);
        });
    }
    
    /**
     * <ul><b><i>endSetup</i></b></ul>
     * <ul><ul><p><code>private void endSetup () </code></p></ul>
     *
     * Performs actions required at the conclusion of the "setup" period before play.
     *
     * @author Kristopher Rangel
     */
    private void endSetup() {
        int setupRow;
        int setupCol;
        for(int row = SETUP_START_ROW; row < 10; row++) {
            for(int col = 0; col < 10; col++) {
                PieceView pv = (PieceView) board.getChildren().get(row * 10 + col);
                if(pv.getPieceType() != Piece.PieceType.EMPTY) {
                    setupRow = row - 6;
                    setupCol = translate(col);
                    if(!isServer) { // is client 
                        // translating setup row to controller setup orientation (different from normal board translation)
                        setupRow = 3 - setupRow;
                    }
                    controller.addToSetup(setupRow, setupCol, pv.getPieceType(), colorInt);
                }      
            }
        }
        // Zeroing out quantity labels in event controller auto-filled any of the board
        for(Label l : countLabels) { l.setText("0"); }
        
        // disable Pieces box for client and server
        board.setDisable(true);
        // disable board for client and server
        piecesBox.setDisable(true);
        inputEnabled = false;
        
        // Requesting that controller update the model (this will trigger an update() to the StrategoView)
        sentSetup = true;
        controller.setBoard(colorInt);
                
        //TODO need to coordinate server/client setup completion before next
        //TODO after setup stuff
    }
    
    /**
     * Perform GameOver sequence.
     * @param winner color of winner: {@value Piece#RED}, {@value Piece#BLUE}, 
     * or {@value Piece#NONE} (for tie)
     * 
     * @author Caroline O'Neill
     */
    private void gameOver(int winner)
    {    	
    	// display winning message pop-up
    	String msg;
    	if (winner == Piece.NONE)
    		msg = "No winner.";

    	else if (winner == colorInt)
    		msg = "You won!";
    	
    	else // winner == other player
    		msg = "You lost!";

    	showAlert(AlertType.INFORMATION, msg);
    	
    	init();
    	start(stage);
    }
    
    /**
     * <ul><b><i>manualBoardUpdate</i></b></ul>
     * <ul><ul><p><code>private void manualBoardUpdate () </code></p></ul>
     *
     * Does a sweep of the board ensuring the UI matches current board positioning.
     * 
     * <p> Intended to be invoked following the setup period after client/server
     * syncronization.
     * 
     * @author Kristopher Rangel
     *
     */
    private void manualBoardUpdate() {
        for(int row = 0; row < 10; row++) {
            for(int col = 0; col < 10; col++) {
                Piece p = controller.getPosition(row, col);
                int posRow = translate(row);
                int posCol = translate(col);
                PieceView pv = (PieceView) board.getChildren().get(posRow * 10 + posCol);
                pv.update(p);
                
                // setting border color (no piece is black, else colored by piece color)
                Color c = Color.BLACK;                              // could maybe pull color selection into setBorderColor
                if(pv.isVisible(playerColor)) {
                    if(p.color() == 1) { c = Color.BLUE; }
                    else if (p.color() == 2) { c = Color.RED; }
                }
                pv.setBorderColor(c);
                pv.saveBorderColor(c);
                
                if(ENABLE_CONSOLE_DEBUG) { System.out.printf("%d ", pv.convertPieceTypeToIndex(p.type)); }
            }
            if(ENABLE_CONSOLE_DEBUG) { System.out.printf(" End row: %d\n", row); }
        }
    }
    
    /**
     * Updates a single board position, accounting for coordinate translation
     * between the model and client boards.
     * @param modelRow row value in the model
     * @param modelCol column value in the model
     * @param p updated {@link Piece}
     * 
     * @author Caroline O'Neill
     */
    private void updatePosition(int modelRow, int modelCol, Piece p)
    {
    	int posRow = translate(modelRow);
        int posCol = translate(modelCol);
        PieceView pv = (PieceView) board.getChildren().get(posRow * 10 + posCol);
        pv.update(p);
        
        // disallowing player to mover other player's pieces
        if (p.color() != colorInt && p.color() != Piece.NONE)
        {
        	pv.setDragEnabled(false);
        }
        else if (p.color() == colorInt)
        {
        	pv.setDragEnabled(true);
        }
        // all except Lakes should have drop enabled
        if (p.type != PieceType.LAKE)
        {
        	pv.setDropEnabled(true);
        }
        
        // setting border color (no piece is black, else colored by piece color)
        Color c = Color.BLACK;
        if(pv.isVisible(playerColor)) {
            if(p.color() == 1) { c = Color.BLUE; }
            else if (p.color() == 2) { c = Color.RED; }
        }
        pv.setBorderColor(c);
        pv.saveBorderColor(c);
    }
    
    /**
     * Updates board setup, accounting for translating between model and client
     * boards. Does not sweep the entire board, just the area of the initial
     * setup.
     * @param color server or client
     * @param initialSetup initial setup
     * 
     * @author Caroline O'Neill
     */
    private void updateBoardSetup(int color, PieceType[][] initialSetup)
    {
    	int modelStartRow = 0;
		if (color == Piece.RED)
			modelStartRow = 6;
		
		for (int row = 0; row < 4; row++)
		{
			for (int col = 0; col < 10; col++)
			{
				int r = row + modelStartRow;
				int c = col;

				Piece p = new Piece(initialSetup[row][col]);
				p.setColor(color);
				updatePosition(r, c, p);
			}
		}
    }
    
    /**
     * Similar to {@link StrategoView#translate(int)}, but uses color passed
     * rather than the this player's {@value StrategoView#isServer} attribute.
     * @param rowOrColumn value to translate
     * @param color server or client
     * @return translated value
     */
    protected static int translate(int rowOrColumn, int color) 
    {
        int result = rowOrColumn;
        if(color == Piece.BLUE) // if for client
        	result = 9 - result;
        return result;
    }
    
    /**
     * <ul><b><i>translate</i></b></ul>
     * <ul><ul><p><code>protected int translate (int rowOrColumn) </code></p></ul>
     *
     * Translates rows or columns from the {@link StrategoModel} into
     * rows or columns for display on the client. A server uses the same
     * orientation as the model by default.
     *
     * @param rowOrColum - the integer row or column to translate
     * @return the translated row or column
     * 
     * @author Kristopher Rangel
     */
    protected static int translate(int rowOrColumn) {
        int result = rowOrColumn;
        if(!isServer) { result = 9 - result; }
        return result;
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
     * @author Caroline O'Neill
     */
    public void update(Observable o, Object arg) {
        if(ENABLE_INPUT_DEBUG) {
            setBoardEnable();    
        }
        
        if(arg instanceof BoardSetupMessage && setupEnabled) 
        {	
            PieceType[][] initialSetup = ((BoardSetupMessage) arg).getInitialSetup();
            int color = ((BoardSetupMessage) arg).getColor();
            System.out.println("SETUP BOARD");
            updateBoardSetup(color, initialSetup);
            	
            if (sentSetup)
            {
                // Enabling drag on the entire board
                board.getChildren().iterator().forEachRemaining(e -> {
                    PieceView pv = (PieceView) e;
                    pv.setDropEnabled(true);
                });
            }
            if (color != colorInt) // setup recieved
            	recvOtherSetup = true;

            if (recvOtherSetup && sentSetup)
            {
            	// proceed to 'battle phase'
            	//TODO: alert about battle beginning?
            	setupEnabled = false;
            	if (isServer)
            	{
            		// enable server board
            		board.setDisable(false);
            		inputEnabled = true;
            	}
            	else // isClient
            	{
            		inputEnabled = false;
            		board.setDisable(true); // should already be disabled?
            		controller.initiateTurnListening();
            	}
            }
            if(ENABLE_CONSOLE_DEBUG) { System.out.println("setup disabled"); }
        } 
        else if (arg instanceof SinglePositionMessage) 
        {
        	System.out.println("UPDATE POSITION");
        	int row = ((SinglePositionMessage) arg).getRow();
        	int col = ((SinglePositionMessage) arg).getCol();
        	Piece p = ((SinglePositionMessage) arg).getPiece();

        	updatePosition(row, col, p);
        	
        	msgRecvCount++;
        }
        
        // switching player turns
        if (msgRecvCount == 3)
        {
        	System.out.println("MSG COUNT == 3");
        	msgRecvCount = 0;
        	// switch board enabled/disabled
        	if (inputEnabled == true)
        	{
        		board.setDisable(true);
        		inputEnabled = false;
        		// initiateTurnListening is invoked in MovePiece
        	}
        	else
        	{
        		board.setDisable(false);
        		inputEnabled = true;
        	}
        	
        	System.out.println("CHECKING GAME OVER");
            //check if game is over
            int winner = controller.winner();
            System.out.println("Winner: " + winner);
            if (winner != Piece.NONE)
            {
            	System.out.println("GAMEOVER");
            	System.out.println("isServer: " + isServer);
            	gameOver(winner);
            }
        }
        
        
        
        if(ENABLE_CONSOLE_DEBUG) { System.out.println("notified"); }
    }
    
    public static Color getPlayerColor() {
        return playerColor;
    }
}
