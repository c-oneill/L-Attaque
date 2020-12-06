package stratego;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Observable;

import stratego.Piece.PieceType;

/**
 * This class serves as the underlying model for the Stratego program.
 * </p> Notes:
 * 		-top/blue is the client
 * 		-top/red is the server
 * 
 * @author Caroline O'Neill
 *
 */
public class StrategoModel extends Observable
{
	public static final int COLUMNS = 10;
	public static final int ROWS = 10;
	
	private Piece[][] grid; // grid[row][col]
	private HashMap<PieceType, Integer> bluePieces; // piece : count 
	private HashMap<PieceType, Integer> redPieces; // piece :  count
	
	/**
	 * StrategoModel constructor. Sets an empty board and initializes the
	 * maps of all pieces on the board.
	 */
	public StrategoModel()
	{
		grid = new Piece[ROWS][COLUMNS];
	
		for (int r = 0; r < ROWS; r++)
		{
			for (int c = 0; c < COLUMNS; c++)
			{
				if ((r == 4 || r == 5) && (c == 2 || c == 3 || c == 6 || c == 7))
					grid[r][c] = new Piece(PieceType.LAKE);
				else
					grid[r][c] = new Piece(PieceType.EMPTY);
			}
		}
		
		bluePieces = new HashMap<PieceType, Integer>();
		bluePieces.put(PieceType.MARSHAL, 1);
		bluePieces.put(PieceType.GENERAL, 1);
		bluePieces.put(PieceType.COLONEL, 2);
		bluePieces.put(PieceType.MAJOR, 3);
		bluePieces.put(PieceType.CAPTAIN, 4);
		bluePieces.put(PieceType.LIEUTENANT, 4);
		bluePieces.put(PieceType.SERGEANT, 4);
		bluePieces.put(PieceType.MINER, 5);
		bluePieces.put(PieceType.SCOUT, 8);
		bluePieces.put(PieceType.BOMB, 6);
		bluePieces.put(PieceType.SPY, 1);
		bluePieces.put(PieceType.FLAG, 1);
		
		redPieces = new HashMap<PieceType, Integer>();
		redPieces.put(PieceType.MARSHAL, 1);
		redPieces.put(PieceType.GENERAL, 1);
		redPieces.put(PieceType.COLONEL, 2);
		redPieces.put(PieceType.MAJOR, 3);
		redPieces.put(PieceType.CAPTAIN, 4);
		redPieces.put(PieceType.LIEUTENANT, 4);
		redPieces.put(PieceType.SERGEANT, 4);
		redPieces.put(PieceType.MINER, 5);
		redPieces.put(PieceType.SCOUT, 8);
		redPieces.put(PieceType.BOMB, 6);
		redPieces.put(PieceType.SPY, 1);
		redPieces.put(PieceType.FLAG, 1);
	}
	
	/**
	 * Replaces the position indicated with a {@link Piece#EMPTY} and returns
	 * the previous {@link Piece}. {@link Piece#LAKE} is returned if the 
	 * position is a {@link Piece#LAKE}. Notifies observers of change at the
	 * position with a {@link SinglePositionMessage}.
	 * @param row row of position
	 * @param col column of position
	 * @return replaced Piece
	 */
	public Piece removePosition(int row, int col)
	{
		Piece piece = grid[row][col];
		
		if (piece.type != PieceType.LAKE)
		{
			Piece emptyPiece = new Piece(PieceType.EMPTY); 
			grid[row][col] = emptyPiece;
		
			// notify observers with SinglePositionMessage
			SinglePositionMessage message = new SinglePositionMessage(row, col, emptyPiece);
			setChanged();
			notifyObservers(message);
		}
		
		return piece;
	}
	
	/**
	 * Removes piece from red or blue pieces map. If the piece color is 
	 * {@link Piece#NONE}, then no action is taken and it returns false.
	 * @param piece piece removed
	 * @return true is blue or red piece is removed from the maps, false
	 * otherwise
	 */
	public boolean removePiece(Piece piece)
	{
		if (piece.color() == Piece.BLUE)
			bluePieces.put(piece.type, bluePieces.get(piece.type) - 1);
		
		else if (piece.color() == Piece.RED)
			redPieces.put(piece.type, redPieces.get(piece.type) - 1);
		
		else
			return false;
		
		return true;
	}
	
	/**
	 * Sets the position indicated with the passed piece. Notifies observers of 
	 * change at the position with a {@link SinglePositionMessage}.
	 * @param row row of position
	 * @param col column of position
	 * @param piece piece set
	 */
	public void setPosition(int row, int col, Piece piece)
	{
		grid[row][col] = piece;
		
		// notify observers with SinglePositionMessage
		SinglePositionMessage message = new SinglePositionMessage(row, col, piece);
		setChanged();
		notifyObservers(message);
	}
	
	/**
	 * Gets the {@link Piece} at the indicated position
	 * @param row row of position
	 * @param col column of position
	 * @return piece
	 */
	public Piece getPosition(int row, int col)
	{
		return grid[row][col];
	}
	
	/**
	 * Builds the start formation of pieces for a player. [0][0] is the top
	 * left corner of the formation. Note that position [0][0] at the 'back' for
	 * blue/client anf the 'front' for red/server. Notifies observers of board
	 * setup with a {@link BoardSetupMessage}.
	 * @param setupGrid a 4 x 10 grid indicating the player setup
	 * @param color player color
	 * @param notify if true, notifies observers of setup
	 */
	public void setBoard(PieceType[][] setupGrid, int color, boolean notify)
	{
		int startRow = 0;
		if (color == Piece.RED)
			startRow = 6;
		
		for (int row = startRow; row < startRow + 4; row++)
		{
			for (int col = 0; col < 10; col++)
			{
				Piece newPiece = new Piece(setupGrid[row - startRow][col]);
				newPiece.setColor(color);
				grid[row][col] = newPiece;
			}
		}
		
		if (notify)
		{
			// notify observers with BoardSetupMessage
			BoardSetupMessage message = new BoardSetupMessage(color, setupGrid);
			setChanged();
			notifyObservers(message);
		}
	}

	
	/**
	 * Gets the HashMap of red pieces still on the board.
	 * @return red pieces
	 */
	public HashMap<PieceType, Integer> getRedPieces()
	{
		return redPieces;
	}
	
	/**
	 * Gets the HashMap of blue pieces still on the board.
	 * @return blue pieces
	 */
	public HashMap<PieceType, Integer> getBluePieces()
	{
		return bluePieces;
	}
	
	/**
	 * Prints the underlying grid for debugging purposes.
	 */
	private void printGrid()
	{
		for (int r = 0; r < ROWS; r++)
		{
			System.out.println(Arrays.toString(grid[r]));
		}
	}

	
}
