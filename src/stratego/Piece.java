package stratego;

/**
 * Enum type Piece defines the piece types availible in Stratego. Playable and
 * non-playable types.
 * </p> This enum includes some of the logic for how pieces can move on the
 * board: moveable vs unmoveable, scouts move like rooks in chass, and other 
 * non-scout moveable pieces may only move one space horizontally/vertically.
 * </p> It also includes logic for piece interactions with attack/defense:
 * 		-when a piece moves on top of another piece (attacking), the piece with 
 * 			a higher level remains and the other is removed. If the level is the
 * 			same, both pieces are removed.
 * 		-the spy can attack and beat the marshal or flag
 *		-the spy is defeated in any other case
 *		-the bomb defeats any piece that attacks it, except the miner
 *		-the miner defuses the bomb (removing it from the board)
 * 
 * @author Caroline O'Neill
 *
 */
public enum Piece 
{
	EMPTY(false, -1),
	LAKE(false, -2),
	
	MARSHAL(true, 9),
	GENERAL(true, 8),
	COLONEL(true, 7),
	MAJOR(true, 6),
	CAPTAIN(true, 5),
	LIEUTENANT(true, 4),
	SERGEANT(true, 3),
	MINER(true, 2),
	SCOUT(true, 1),
	
	BOMB(false, 0),
	SPY(true, 0),
	FLAG(false, 0);
	
	public static final int NONE = 0; // neither player
	public static final int BLUE = 1; // top/client
	public static final int RED = 2; // bottom/server
	
	private boolean moveable;
	private int level;
	private int color;
	
	private Piece(boolean moveable, int level)
	{
		this.moveable = moveable;
		this.level = level;
		this.color = NONE;
	}
	
	/**
	 * Indicates if the move is valid based on Piece move abilities and the 
	 * actual board. Does not factor in attack/defense.
	 * </p> The Scout moves like a rook in chess and any other moveable piece
	 * (not the bomb or flag) can only move one space at a time vertically or
	 * horizontally.
	 * </p> Note, it's not a valid move to remain in the same place and the move
	 * cant't end up in the lakes 
	 * (row, col) : {(4,2)(4,3)(5,2)(5,3)(4,6)(4,7)(5,6)(5,7)
	 * 
	 * @param currRow current Row coordinate of piece
	 * @param currCol current Col coordinate of piece
	 * @param row next Row of piece
	 * @param col next Col of piece
	 * @param piece piece being moved
	 * @return true if a valid move, false otherwise
	 */
	public static boolean isMoveValid(int currRow, int currCol, int row, int col, Piece piece)
	{
		// can the piece move
		if(!piece.moveable)
			return false;
		
		// does the move remain on the board
		if (col >= StrategoModel.COLUMNS || row >= StrategoModel.ROWS)
			return false;
		
		// does the move land in the lakes
		if ((row == 4 || row == 5) && (col == 2 || col == 3 || col == 6 || col == 7))
			return false;
		
		// moveable piece is not a Scout - should move one square at a time
		if (piece != Piece.SCOUT && (Math.abs(currRow - row) != 1 || Math.abs(currCol - col) != 1))
			return false;
		
		// no diagonal moves -> currRow == row XOR currCol == col
		return currRow == row ^ currCol == col;
	}
	
	/**
	 * Indicates which pieces 'wins' when both occupy the same space. 0 means 
	 * both are removed, 1 means attacker remains, 2 means defender remains. If
	 * the attacker is immoveable or the defender is a lake, the comparison is 
	 * invalid and -1 is returned. Also an invalid comparison if both Pieces
	 * are the same color.
	 * @param attacker
	 * @param p2 second piece
	 * @return if 0 both removed, if 1 p1 remains, if 2 p2 remains, returns -1
	 * if its an invalid comparison.
	 */
	public static int whoWins(Piece attacker, Piece defender)
	{
		if (!attacker.isMoveable() || defender == Piece.LAKE)
			return -1;
		
		if (attacker.color() == defender.color())
			return 0;
		
		if (defender == Piece.BOMB)
			if (attacker == Piece.MINER)
				return 1;
			else
				return 2;
		
		if (attacker == Piece.SPY && defender == Piece.MARSHAL)
			return 1;
				
		if (attacker.level() == defender.level())
			return 0;
		else if (attacker.level() > defender.level())
			return 1;
		else // attacker.level() < defender.level()
			return 2;
	}
	
	/**
	 * Is the piece Moveable?
	 * @return true if moveable, false otherwise
	 */
	public boolean isMoveable()
	{
		return moveable;
	}
	
	/**
	 * Gets the Piece's level.
	 * @return piece leve
	 */
	public int level()
	{
		return level;
	}
	
	/**
	 * Gets the Piece color.  
	 * @return {@link Piece#BLUE}, {@link Piece#RED}, or {@link Piece#NONE}
	 */
	public int color()
	{
		return color;
	}
	
	/**
	 * Sets the piece color. No color set if color is not red, blue, or none.
	 * @param color {@link Piece#BLUE}, {@link Piece#RED}, or {@link Piece#NONE}
	 * @return true is color is set, false otherwise.
	 */
	public boolean setColor(int color)
	{
		if (color == Piece.NONE
				|| color == Piece.BLUE
				|| color == Piece.RED) 
		{
			this.color = color;
			return true;
		}
		return false;	
	}
	
	/**
	 * Gives a string representation of the Piece (its level)
	 */
	@Override
	public String toString()
	{
		return Integer.toString(level);
	}
}

