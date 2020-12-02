package stratego;

import java.util.HashMap;

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
	
	private Piece[][] blueInitialSetup;
	private Piece[][] redInitialSetup;
	private HashMap<PieceType, Integer> blueAvailible; // piece : count 
	private HashMap<PieceType, Integer> redAvailible; // piece :  count
	
	public StrategoController()
	{
		model = new StrategoModel();
		
		blueInitialSetup = new Piece[4][10];
		redInitialSetup = new Piece[4][10];
		
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
	 * @param piece piece to place
	 * @return true if the piece is placed (valid position, valid piece), false 
	 * if setup already has the max. number of that piece type 
	 */
	public boolean addToSetup(int row, int col, Piece piece)
	{
		return false;
	}
	
	/**
	 * Takes an initial setup grid and fills in the board. No action is taken if
	 * any part of the board is not empty 
	 * @param initialSetup
	 * @param color
	 * @return true if the setup is successful, false if that side is already
	 * setup or has any pieces that are not {@link Piece#EMPTY} 
	 */
	public boolean setBoard(Piece[][] initialSetup, int color)
	{
		// calls setBoard in Model which notifies observers
		return false;
	}
	
	/**
	 * If fills an empty slots in the initial board setup if the user did not
	 * indicate a piece for every space.
	 */
	private void fillRemaining()
	{
		
	}
	
	/**
	 * Gets the current game winner. In the case of no winner yet, 
	 * {@value Piece#NONE} is returned.
	 * @return {@value Piece#NONE} if no winner yet, {@value Piece#BLUE} if
	 * blue/client wins, {@value Piece#RED} if red/server wins.
	 */
	public int winner()
	{
		return 0;
	}
			
}
