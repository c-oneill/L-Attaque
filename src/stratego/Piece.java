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
	LAKE(false, -1),
	
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
	
	private boolean moveable;
	private int level;
	
	private Piece(boolean moveable, int level)
	{
		this.moveable = moveable;
		this.level = level;
	}
	
	/**
	 * Indicates if the move is valid based on Piece move abilities and the 
	 * actual board. Does not factor in attack/defense.
	 * </p> The Scout moves like a rook in chess and any other moveable piece
	 * (not the bomb or flag) can only move one space at a time vertically or
	 * horizontally.
	 * </p> Note, it's not a valid move to remain in the same place.
	 * @param currX current X coordinate of piece
	 * @param currY current Y coordinate of piece
	 * @param X next X coordinate of piece
	 * @param Y next Y coordinate of piece
	 * @param piece piece being moved
	 * @return true if a valid move, false otherwise
	 */
	public static boolean isMoveValid(int currX, int currY, int X, int Y, Piece piece)
	{
		// can the piece move
		if(!piece.moveable)
			return false;
		
		// does the move remain on the board
		if (X >= 10 || Y >= 10)
			return false;
		
		// moveable piece is not a Scout - should move one square at a time
		if (piece != Piece.SCOUT && (Math.abs(currX - X) != 1 || Math.abs(currY - Y) != 1))
			return false;
		
		// no diagonal moves -> currX == X XOR currY == Y
		return currX == X ^ currY == Y;
	}
	
	/**
	 * Indicates which pieces 'wins' when both occupy the same space. 0 means 
	 * both are removed, 1 means attacker remains, 2 means defender remains. If
	 * the attacker is immoveable or the defender is a lake, the comparison is 
	 * invalid and -1 is returned.
	 * @param attacker
	 * @param p2 second piece
	 * @return if 0 both removed, if 1 p1 remains, if 2 p2 remains, returns -1
	 * if its an invalid comparison.
	 */
	public static int whoWins(Piece attacker, Piece defender)
	{
		if (!attacker.isMoveable() || defender == Piece.LAKE)
			return -1;
		
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
}

