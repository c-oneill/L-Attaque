package stratego;


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
	
	/**
	 * Moves a piece from current position to a new specified position. Piece is
	 * only moved if the move follows in-game logic. If an opponent piece
	 * occupies the destination, the source piece "attacks" the destination
	 * piece.
	 * @param currRow source row location
	 * @param currCol source column location
	 * @param row destination row location
	 * @param col destination column location
	 * @return true is move logic is valid, false otherwise
	 */
	public boolean movePiece(int currRow, int currCol, int row, int col)
	{
		Piece srcPiece = model.getPosition(currRow, currCol);
		Piece dstPiece = model.getPosition(row, col);
		int winner = Piece.whoWins(srcPiece, dstPiece);
		
		if (!Piece.isMoveValid(currRow, currCol, row, col, srcPiece))
			return false;
		
		// does move skip over any other pieces/lakes (only applicable to scout)
		if (srcPiece == Piece.SCOUT)
		{
			// moves horizontally
			if (currRow == row)
			{
				int diff = Math.abs(currCol - col);
				int start = Integer.min(currCol, col);
				
				for (int c = start + 1; c < start + diff; c++)
				{
					if (model.getPosition(row, c) != Piece.EMPTY)
						return false;	
				}
			}
			// moves vertically
			else // currCol == col
			{
				int diff = Math.abs(currRow - row);
				int start = Integer.min(currRow, row);
				
				for (int r = start + 1; r < start + diff; r++)
				{
					if (model.getPosition(r, col) != Piece.EMPTY)
						return false;	
				}
			}
		}
		
		
		
		
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
	
	public void addToSetup(int row, int col, Piece piece)
	{
		
	}
	
	public void setBoard()
	{
		
	}
	
	private void fillRemaining()
	{
		
	}
	
	public int getWinner()
	{
		return 0;
	}
			
}
