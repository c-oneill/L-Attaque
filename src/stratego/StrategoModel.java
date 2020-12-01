package stratego;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Observable;

/**
 * This class serves as the underlying model for the Stratego program.
 * </p> Notes:
 * 		-top/blue is the client
 * 		-top/red is the server
 * @author Caroline O'Neill
 *
 */
public class StrategoModel extends Observable
{
	public static final int COLUMNS = 10;
	public static final int ROWS = 10;
	
	private Piece[][] grid; // grid[row][col]
	private HashMap<Piece, Integer> bluePieces; // piece : count 
	private HashMap<Piece, Integer> redPieces; // piece :  count
	
	public static void main(String[] args) 
	{
		StrategoModel model = new StrategoModel();
		model.printGrid();
	}
	
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
					grid[r][c] = Piece.LAKE;
				else
					grid[r][c] = Piece.EMPTY;
			}
		}
		
		bluePieces = new HashMap<Piece, Integer>();
		bluePieces.put(Piece.MARSHAL, 1);
		bluePieces.put(Piece.GENERAL, 1);
		bluePieces.put(Piece.COLONEL, 2);
		bluePieces.put(Piece.MAJOR, 3);
		bluePieces.put(Piece.CAPTAIN, 4);
		bluePieces.put(Piece.LIEUTENANT, 4);
		bluePieces.put(Piece.SERGEANT, 4);
		bluePieces.put(Piece.MINER, 5);
		bluePieces.put(Piece.SCOUT, 8);
		bluePieces.put(Piece.SPY, 1);
		bluePieces.put(Piece.FLAG, 1);
		
		redPieces = new HashMap<Piece, Integer>();
		redPieces.put(Piece.MARSHAL, 1);
		redPieces.put(Piece.GENERAL, 1);
		redPieces.put(Piece.COLONEL, 2);
		redPieces.put(Piece.MAJOR, 3);
		redPieces.put(Piece.CAPTAIN, 4);
		redPieces.put(Piece.LIEUTENANT, 4);
		redPieces.put(Piece.SERGEANT, 4);
		redPieces.put(Piece.MINER, 5);
		redPieces.put(Piece.SCOUT, 8);
		redPieces.put(Piece.SPY, 1);
		redPieces.put(Piece.FLAG, 1);
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
		
		if (piece != Piece.LAKE)
			grid[row][col] = Piece.EMPTY;
		
		// TODO notify observers with SinglePositionMessage
		
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
			bluePieces.put(piece, bluePieces.get(piece) - 1);
		
		else if (piece.color() == Piece.RED)
			redPieces.put(piece, redPieces.get(piece) - 1);
		
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
		
		// TODO notify observers with SinglePositionMessage
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
	
	public void setBoard(Piece[][] setupGrid, int player)
	{
		int startRow = 0;
		if (player == Piece.RED)
			startRow = 6;
		
		for (int row = startRow; row < startRow + 4; row++)
		{
			for (int col = 0; col < 10; col++)
			{
				grid[row][col] = setupGrid[row][col];
			}
		}
		
		// TODO notify observers with BoardSetupMessage
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
