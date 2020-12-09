package stratego;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import stratego.Piece.PieceType;

/**
 * This class serves as the Controller in the Stratego Program, allowing the 
 * {@link StrategoView} to indirectly interact with the {@link StrategoModel}.
 * Handles the network and connection between players using 
 * {@link StrategoNetwork}.
 * 
 * <p> The controller holds a model and handles the logic for making valid 
 * turns (building on the logic built in to {@link Piece}. Also determines game
 * status.
 * </p> Note:
 * 		-game is over when the opponent flag or all moveable pieces are captured
 * 		-pieces may not 'jump' over each other or lakes
 * 
 * @author Caroline O'Neill
 * 
 * TODO: Need to integrate the network controls
 * TODO: correct MVC architecture to build model here?
 *
 */
public class StrategoController 
{
	private StrategoModel model;
	private StrategoNetwork gameNetwork;
	private StrategoNetwork chatNetwork;
	
	private PieceType[][] blueInitialSetup;
	private PieceType[][] redInitialSetup;
	private HashMap<PieceType, Integer> blueAvailible; // piece : count 
	private HashMap<PieceType, Integer> redAvailible; // piece :  count
	
	private AtomicBoolean chatListening;
	
	public StrategoController()
	{
		model = new StrategoModel();
		
		gameNetwork = null;
		chatNetwork = null;
		
		blueInitialSetup = new PieceType[4][10];
		redInitialSetup = new PieceType[4][10];
		
		blueAvailible = new HashMap<PieceType, Integer>();
		redAvailible = new HashMap<PieceType, Integer>();
		
		resetAvailible(Piece.BLUE);
		resetAvailible(Piece.RED);
		
		chatListening = new AtomicBoolean(true);
	}
	
	/**
	 * Reset HasMaps of piecees availible for placement to include all 40
	 * pieces.
	 * @param color color of piece set reseting
	 */
	private void resetAvailible(int color)
	{
		if (color == Piece.BLUE) 
		{
			blueAvailible.put(PieceType.MARSHAL, 1);
			blueAvailible.put(PieceType.GENERAL, 1);
			blueAvailible.put(PieceType.COLONEL, 2);
			blueAvailible.put(PieceType.MAJOR, 3);
			blueAvailible.put(PieceType.CAPTAIN, 4);
			blueAvailible.put(PieceType.LIEUTENANT, 4);
			blueAvailible.put(PieceType.SERGEANT, 4);
			blueAvailible.put(PieceType.MINER, 5);
			blueAvailible.put(PieceType.SCOUT, 8);
			blueAvailible.put(PieceType.BOMB, 6);
			blueAvailible.put(PieceType.SPY, 1);
			blueAvailible.put(PieceType.FLAG, 1);
		}
		else if (color == Piece.RED) 
		{
			redAvailible.put(PieceType.MARSHAL, 1);
			redAvailible.put(PieceType.GENERAL, 1);
			redAvailible.put(PieceType.COLONEL, 2);
			redAvailible.put(PieceType.MAJOR, 3);
			redAvailible.put(PieceType.CAPTAIN, 4);
			redAvailible.put(PieceType.LIEUTENANT, 4);
			redAvailible.put(PieceType.SERGEANT, 4);
			redAvailible.put(PieceType.MINER, 5);
			redAvailible.put(PieceType.SCOUT, 8);
			redAvailible.put(PieceType.BOMB, 6);
			redAvailible.put(PieceType.SPY, 1);
			redAvailible.put(PieceType.FLAG, 1);
		}
	}
	
	/**
     * Build a client/server connection as a {@link StrategoNetwork} for the
     * game.
     * @param isServer is this instance a server
     * @param server the server to connect to (if it's a client)
     * @param port the port to connect to
     * @return start error
     */
	public boolean buildGameNetwork(boolean isServer, String server, int port)
    {
		gameNetwork = new StrategoNetwork(isServer, server, port);
    	return gameNetwork.getStartError();
    }
	
	/**
     * Build a client/server connection as a {@link StrategoNetwork} for the
     * chat.
     * @param isServer is this instance a server
     * @param server the server to connect to (if it's a client)
     * @param port the port to connect to
     * @return start error
     */
	public boolean buildChatNetwork(boolean isServer, String server, int port)
    {
		chatNetwork = new StrategoNetwork(isServer, server, port);
    	return chatNetwork.getStartError();
    }
	
	/**
     * Gets the error message associated with starting up the game network.
     * @return error message
     */
    public String getGameNetworkError()
    {   String errorMessage = "No network established.";
        if(gameNetwork != null)
            errorMessage =  gameNetwork.getErrorMessage();
        return errorMessage;
    }
    
    /**
     * Gets the error message associated with starting up the chat network.
     * @return error message
     */
    public String getChatNetworkError()
    {   String errorMessage = "No network established.";
        if(chatNetwork != null)
            errorMessage =  chatNetwork.getErrorMessage();
        return errorMessage;
    }
    
    /**
     * Closes the game network connection.
     * @return true if there was no error closing connection, false otherwise
     */
    public boolean closeGameNetwork()
    {   boolean result = false;
        if(gameNetwork != null)
            result = gameNetwork.closeConnection();
    	return result;
    }
    
    /**
     * Closes the chat network connection.
     * @return true if there was no error closing connection, false otherwise
     */
    public boolean closeChatNetwork()
    {
    	chatListening.set(false);
    	boolean result = false;
        if(chatNetwork != null)
            result = chatNetwork.closeConnection();
    	return result;
    }
    
    /**
     * Initiates continuous listening for {@link ChatMessage} on the chat 
     * network input stream.
     * @param chatDisplay chat display
     */
    public void initiateChatListening(Label chatDisplay)
    {
    	chatListening.set(true);
    	System.out.println("initiating chat");
    	System.out.println("chatListening: " + chatListening.get());
		Thread chatRecvThread = new Thread(() -> 
    	{
    		while(chatListening.get())
    		{
    			System.out.println("initiate chat while loop");
        		ChatMessage chatMessage = chatNetwork.readChatMessage();
        		if (chatMessage == null) { return; }
        		System.out.println("chat message recieved");
        		final String chatText = chatMessage.getMessage();
        		final int color = chatMessage.getColor();
        		
        		Platform.runLater(() -> 
        		{
        			// appending to chatDisplay pushed until later in the main thread
        			String colorString = (color == 1) ? "BLUE" : "RED ";
        	        String currentText = chatDisplay.getText();
        	        currentText = currentText + "\n" + colorString +" >> " + chatText;
        	        chatDisplay.setText(currentText);
        		});
    		}

    	});
		chatRecvThread.start();
    }
    
    /**
     * Writes a {@link ChatMessage} to the chat network output stream.
     * @param msg message 
     * @param color color of player the {@link ChatMessage} originates from.
     */
    public void writeChatMessage(String msg, int color)
    {
    	System.out.println("chat message written");
    	chatNetwork.writeChatMessage(new ChatMessage(msg, color));
    }
    
    /**
     * Prepares client to recieve other Player's first message (board setup),
     * then passes off responsibility to the 
     * {@link StrategoController#setBoard(int)} method.
     */
    public void initiateSetupListening()
    {
    	Thread recvSetupThread = new Thread(() -> 
    	{
    		BoardSetupMessage recvMessage = gameNetwork.readStartupMessage();
    		if (recvMessage == null) { return; }
    		
    		PieceType[][] otherInitialSetup =  recvMessage.getInitialSetup();
        	int color = recvMessage.getColor();
        	
        	for (int row = 0; row < 4; row++)
    			for (int col = 0; col < 10; col++)
    				addToSetup(row, col, otherInitialSetup[row][col], color);
        	
    		Platform.runLater(() -> 
    		{
    			// model/view update pushed until later in the main thread
            	setOtherPlayerBoard(color);
    		});
    	});
    	recvSetupThread.start();
    }
    
    public void writeGameOverMsg()
    {
    	System.out.println("sent game over message");
    	gameNetwork.writeMessage(new SinglePositionMessage(-1, -1, null));
    }
    
    /**
     * Sets up {@link StrategoController} to listen for three incoming 
     * {@link SinglePositionMessage}.
     */
    public void initiateTurnListening()
    {
    	Thread recvSetupThread = new Thread(() -> 
    	{
    		SinglePositionMessage recvMsg1 = gameNetwork.readMessage();
    		if (recvMsg1 == null) { return; }
    		final int row1 = recvMsg1.getRow();
    		final int col1 = recvMsg1.getCol();
    		final Piece piece1 = recvMsg1.getPiece();
    		final Piece rp1 = recvMsg1.getPieceToRemovePlace();
        	final boolean removing1 = recvMsg1.isRemoved();
        	
        	// game over message from other user
        	if (row1 == -1 && col1 == -1)
        	{
        		System.out.println("recieved game over message 1");
        		Platform.runLater(() -> 
        		{
        			System.out.println("recieved game over message 2");
        			model.setPosition(row1, col1, piece1);
        		});
        		return;
        	}
    		        	
    		SinglePositionMessage recvMsg2 = gameNetwork.readMessage();
    		if (recvMsg2 == null) { return; }
    		final int row2 = recvMsg2.getRow();
    		final int col2 = recvMsg2.getCol();
    		final Piece piece2 = recvMsg2.getPiece();
    		final Piece rp2 = recvMsg2.getPieceToRemovePlace();
        	final boolean removing2 = recvMsg2.isRemoved();
        	
    		SinglePositionMessage recvMsg3 = gameNetwork.readMessage();
    		if (recvMsg3 == null) { return; }
    		final int row3 = recvMsg3.getRow();
    		final int col3 = recvMsg3.getCol();
    		final Piece piece3 = recvMsg3.getPiece();
    		final Piece rp3 = recvMsg3.getPieceToRemovePlace();
        	final boolean removing3 = recvMsg3.isRemoved();
    		
    		Platform.runLater(() -> 
    		{
    			// model/view update pushed until later in the main thread
    			removeAddPiece(rp1, removing1);
            	model.setPosition(row1, col1, piece1);
            	removeAddPiece(rp2, removing2);
            	model.setPosition(row2, col2, piece2);
            	removeAddPiece(rp3, removing3);
            	model.setPosition(row3, col3, piece3);
    		});
    	});
    	recvSetupThread.start();
    }
	
	/**
	 * Moves a piece from current position to a new specified position. Piece is
	 * only moved if the move follows in-game logic. If an opponent piece
	 * occupies the destination, the source piece "attacks" the destination
	 * piece.
	 * @param srcRow source row location
	 * @param srcCol source column location
	 * @param dstRow destination row location
	 * @param dstCol destination column location
	 * @return true is move logic is valid, false otherwise
	 */
	public boolean movePiece(int srcRow, int srcCol, int dstRow, int dstCol)
	{
	    if(StrategoView.ENABLE_CONSOLE_DEBUG) {
	        System.out.println("MOVE PIECE");
	    }
		Piece srcPiece = model.getPosition(srcRow, srcCol);
		Piece dstPiece = model.getPosition(dstRow, dstCol);
		int winner = Piece.whoWins(srcPiece, dstPiece);
		
		if (!Piece.isMoveValid(srcRow, srcCol, dstRow, dstCol, srcPiece.type) || winner == -1)
			return false;
			
		// does move skip over any other pieces/lakes (only applicable to scout)
		if (srcPiece.type == PieceType.SCOUT && doesMoveJump(srcRow, srcCol, dstRow, dstCol))
			return false;
			
		// move is valid (srcPiece is not empty or a lake)
		// 3 total messages sent locally to update
		model.removePosition(srcRow, srcCol); // 1st locally
		model.setPosition(dstRow, dstCol, srcPiece); // 2nd locally
		
		// 3 total messages sent over network to update
		gameNetwork.writeMessage(new SinglePositionMessage(srcRow, srcCol, new Piece(PieceType.EMPTY), srcPiece, true)); // 1st over network
		gameNetwork.writeMessage(new SinglePositionMessage(dstRow, dstCol, srcPiece, dstPiece, true)); // 2nd over network
		
		if (winner == 0) // both removed
		{
			model.removePiece(srcPiece);
			model.removePiece(dstPiece);
			model.removePosition(dstRow, dstCol); // 3rd locally
			
			gameNetwork.writeMessage(new SinglePositionMessage(dstRow, dstCol, new Piece(PieceType.EMPTY), dstPiece, true)); // 3rd over network
		}
		else if (winner == 1) // attacker remains, defender removed
		{
			model.removePiece(dstPiece);
			model.setPosition(dstRow, dstCol, srcPiece); // unnecessary, but need consistent number of messages sent // 3rd locally
			
			gameNetwork.writeMessage(new SinglePositionMessage(dstRow, dstCol, srcPiece, srcPiece, false)); // 3rd over network
		}
		else if (winner == 2) // defender remains, attacker removed
		{
			model.removePiece(srcPiece);
			model.setPosition(dstRow, dstCol, dstPiece); // 3rd locally
			
			gameNetwork.writeMessage(new SinglePositionMessage(dstRow, dstCol, dstPiece, dstPiece, false)); // 3rd over network
		}
		else // winner == -1
		{
			// unnecessary, but need consistent number of messages sent
			model.setPosition(srcRow, srcCol, srcPiece); // 3rd locally
			gameNetwork.writeMessage(new SinglePositionMessage(srcRow, srcCol, srcPiece)); // 3rd over network
		}
		
		initiateTurnListening();
		
		return true;
	}
	
	/**
	 * Removes indicated piece from the model's pieces availible map for that 
	 * color. This method addresses the issue where pieces sent over the
	 * network to the other player are not actually removed from the pieces
	 * availible map (since
	 * {@link StrategoController#movePiece(int, int, int, int)} is not called).
	 * @param piece
	 * @param removing flag - true if piece is removed, false if added
	 */
	public void removeAddPiece(Piece piece, boolean removing)
	{
		if (piece != null)
		{
			if (removing)
			{
				System.out.println("piece removed");
				model.removePiece(piece);
			}
			else
			{
				System.out.println("piece added");
				model.addPiece(piece);	
			}

		}
	}	
	
	/**
	 * Determines if a given moves jumps other pieces or lakes. Assumes
	 * movement is entirely vertical or entirely horizontal.
	 * @param srcRow source row location
	 * @param srcCol source column location
	 * @param dstRow destination row location
	 * @param dstCol destination column location
	 * @return true if move includes a jump, false otherwise
	 */
	private boolean doesMoveJump(int srcRow, int srcCol, int dstRow, int dstCol)
	{
		// moves horizontally
		if (srcRow == dstRow)
		{
			int diff = Math.abs(srcCol - dstCol);
			int start = Integer.min(srcCol, dstCol);
						
			for (int c = start + 1; c < start + diff; c++)
			{
				if (model.getPosition(dstRow, c).type != PieceType.EMPTY)
					return true;	
			}
		}
		// moves vertically
		else // srcCol == col
		{
			int diff = Math.abs(srcRow - dstRow);
			int start = Integer.min(srcRow, dstRow);
						
			for (int r = start + 1; r < start + diff; r++)
			{
				if (model.getPosition(r, dstCol).type != PieceType.EMPTY)
					return true;	
			}
		}
		return false;
	}
	
	/**
	 * Gets the {@link Piece} at the indicated position
	 * @param row row of position
	 * @param col column of position
	 * @return piece
	 */
	public Piece getPosition(int row, int col)
	{
		return model.getPosition(row, col);
	}
	
	/**
	 * Adds Piece to initial setup for that color at the indicated position.
	 * @param row row placement in setup
	 * @param col col placement in setup
	 * @param pieceType type of piece to place
	 * @param color color of piece to place
	 * @return true if the piece is placed (valid position, valid piece), false 
	 * if setup already has the max. number of that piece type 
	 */
	public boolean addToSetup(int row, int col, PieceType pieceType, int color)
	{
		PieceType[][] initialSetup;
		HashMap<PieceType, Integer> availible;
		if (color == Piece.BLUE)
		{
			initialSetup = blueInitialSetup;
			availible = blueAvailible;
		}
			
		else if (color == Piece.RED)
		{
			initialSetup = redInitialSetup;
			availible = redAvailible;
		}
		else
			return false;
		
		PieceType currPlaced = initialSetup[row][col];
		
		// any of pieceType availible?
		if (availible.get(pieceType) < 1)
			return false;
		
		if (currPlaced == null) // new placement
		{
			availible.put(pieceType, availible.get(pieceType) - 1);	
			initialSetup[row][col] = pieceType;
		}
		else // replacing a piece placement
		{
			availible.put(initialSetup[row][col], availible.get(pieceType) + 1);
			availible.put(pieceType, availible.get(pieceType) - 1);
			initialSetup[row][col] = pieceType;
		}
		return true;
	}
	
	/**
	 * Takes an initial setup grid and fills in the board. Notifies Observers.
	 * @param color color to setup
	 */
	public void setBoard(int color)
	{
		PieceType[][] initialSetup = null;
		fillRemaining(color);
		if (color == Piece.BLUE)
			initialSetup = blueInitialSetup;
		else if (color == Piece.RED)
			initialSetup = redInitialSetup;
		
		model.setBoard(initialSetup, color, true);
		
		BoardSetupMessage setupMessage = new BoardSetupMessage(color, initialSetup);
		gameNetwork.writeStartupMessage(setupMessage);
	}
	
	/**
	 * Takes an initial setup grid and fills in the board. Does not notify
	 * observers.
	 * @param color color to setup
	 */
	public void setOtherPlayerBoard(int color)
	{
		PieceType[][] initialSetup = null;
		if (color == Piece.BLUE)
			initialSetup = blueInitialSetup;
		else if (color == Piece.RED)
			initialSetup = redInitialSetup;
			
		model.setBoard(initialSetup, color, true);
	}
	
	/**
	 * If fills an empty slots in the initial board setup if the user did not
	 * indicate a piece for every space.
	 * @param color player color
	 */
	private void fillRemaining(int color)
	{
		PieceType[][] initialSetup;
		HashMap<PieceType, Integer> availible;
		if (color == Piece.BLUE)
		{
			initialSetup = blueInitialSetup;
			availible = blueAvailible;
		}
			
		else // color == Piece.RED
		{
			initialSetup = redInitialSetup;
			availible = redAvailible;
		}
		
		Iterator<Map.Entry<PieceType, Integer>> it = availible.entrySet().iterator();
		
		Map.Entry<PieceType, Integer> entry = null;
		
		if (it.hasNext())
			entry = it.next();
		else
			return;
		
		for (int r = 0; r < 4; r++)
		{
			for (int c = 0; c < 10; c++)
			{
				// empty slot in initialSetup
				if (initialSetup[r][c] == null)
				{
					// max. number of piece type has already been placed
					while (entry.getValue() < 1)
					{
						it.remove();
						if (it.hasNext())
							entry = it.next();
						else
							return;
					}
					initialSetup[r][c] = entry.getKey();
					availible.put(entry.getKey(), entry.getValue() - 1);
				}
			}
		}
		// reset availible
		resetAvailible(color);
	}
	
	/**
	 * Gets the current game winner. In the case of no winner yet, 
	 * {@value Piece#NONE} is returned.
	 * @return {@value Piece#NONE} if no winner yet, {@value Piece#BLUE} if
	 * blue/client wins, {@value Piece#RED} if red/server wins.
	 */
	public int winner()
	{
		System.out.println("red: " + model.getRedPieces());
		System.out.println("blue: " + model.getBluePieces());
		if (model.getBluePieces().get(PieceType.FLAG) < 1)
			return Piece.RED;
		
		else if (model.getRedPieces().get(PieceType.FLAG) < 1)
			return Piece.BLUE;
		
		else if (!stillMoveablePieces(Piece.BLUE))
			return Piece.RED;
		
		else if (!stillMoveablePieces(Piece.RED))
			return Piece.BLUE;
		
		else
			return Piece.NONE;
	}
	
	/**
	 * Iterates through the pieces on the board and indicates if there are still
	 * at least one moveable piece in the color passed. Game is over when either
	 * flag is captured or either team loses all its moveable pieces.
	 * @param color color of pieces to check
	 * @return true if there are still moveable pieces, false otherwise
	 */
	private boolean stillMoveablePieces(int color)
	{
		HashMap<PieceType, Integer> pieces = null;
		if (color == Piece.BLUE)
			pieces = model.getBluePieces();
		else if (color == Piece.RED)
			pieces = model.getRedPieces();
		else
			return false;
		
		Iterator<Entry<PieceType, Integer>> it = pieces.entrySet().iterator();
		while(it.hasNext()) 
		{
			Entry<PieceType, Integer> entry = it.next();
			if (entry.getKey().isMoveable() && entry.getValue() > 0)
				return true;
		}
		return false;
	}
	
	/**
	 * <ul><b><i>checkAvailable</i></b></ul>
	 * <ul><ul><p><code>public int checkAvailable (PieceType pt, Color color) </code></p></ul>
	 *
	 * Returns the number of pieces available to place on the board during setup
	 * for the given {@link PieceType}.
	 *
	 * @param pt - the <code>PieceType</code> to query
	 * @param color - the {@link Color} of the piece to query
	 * @return the count of pieces left in the given PieceType and color
	 * 
	 * @author Kristopher Rangel
	 */
	public int checkAvailable(PieceType pt, Color color) {
        int count = 0;
        if(color == Color.BLUE) {
            count = blueAvailible.get(pt);
        }else if (color == Color.RED) {
            count = redAvailible.get(pt);
        }
        return count;
    }
    /**
     * <ul><b><i>setModelObserver</i></b></ul>
     * <ul><ul><p><code>public void setModelObserver (StrategoView view) </code></p></ul>
     *
     * Adds a {@link StrategoView} as an observer of the {@link StrategoModel}.
     *
     * @param view - the <code>StrategoView</code> to observer the <code>StrategoModel</code>
     * 
     * @author Kristopher Rangel
     */
    public void setModelObserver(StrategoView view) {
        model.addObserver(view);
    }
	
}
