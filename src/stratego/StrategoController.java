package stratego;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
	
	private PieceType[][] blueInitialSetup;
	private PieceType[][] redInitialSetup;
	private HashMap<PieceType, Integer> blueAvailible; // piece : count 
	private HashMap<PieceType, Integer> redAvailible; // piece :  count
	
	public StrategoController()
	{
		model = new StrategoModel();
		
		blueInitialSetup = new PieceType[4][10];
		redInitialSetup = new PieceType[4][10];
		
		blueAvailible = new HashMap<PieceType, Integer>();
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
		
		redAvailible = new HashMap<PieceType, Integer>();
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
		Piece srcPiece = model.getPosition(srcRow, srcCol);
		Piece dstPiece = model.getPosition(dstRow, dstCol);
		int winner = Piece.whoWins(srcPiece, dstPiece);
		
		if (!Piece.isMoveValid(srcRow, srcCol, dstRow, dstCol, srcPiece.type) || winner == -1)
			return false;
		
		// does move skip over any other pieces/lakes (only applicable to scout)
		if (srcPiece.type == PieceType.SCOUT && doesMoveJump(srcRow, srcCol, dstRow, dstCol))
			return false;
		
		// move is valid (srcPiece is not empty or a lake)
		model.removePosition(srcRow, srcCol);
		model.setPosition(dstRow, dstCol, srcPiece);
		
		if (winner == 0) // both removed
		{
			model.removePiece(srcPiece);
			model.removePiece(dstPiece);
			model.removePosition(dstRow, dstCol);
		}
		else if (winner == 1) // attacker remains, defender removed
		{
			model.removePiece(dstPiece);
		}
		else if (winner == 2) // defender remains, attacker removed
		{
			model.removePiece(srcPiece);
			model.setPosition(dstRow, dstCol, dstPiece);
		}
		return true;
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
	 * Takes an initial setup grid and fills in the board.
	 * @param color color to setup
	 */
	public void setBoard(int color)
	{
		fillRemaining(color);
		if (color == Piece.BLUE)
			model.setBoard(blueInitialSetup, color);
		else if (color == Piece.RED)
			model.setBoard(redInitialSetup, color);
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
	}
	
	/**
	 * Gets the current game winner. In the case of no winner yet, 
	 * {@value Piece#NONE} is returned.
	 * @return {@value Piece#NONE} if no winner yet, {@value Piece#BLUE} if
	 * blue/client wins, {@value Piece#RED} if red/server wins.
	 */
	public int winner()
	{
		if (model.getBluePieces().get(PieceType.FLAG) < 1)
			return Piece.RED;
		
		else if (model.getRedPieces().get(PieceType.FLAG) < 1)
			return Piece.BLUE;
		
		else
			return Piece.NONE;
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
