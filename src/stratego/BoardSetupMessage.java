package stratego;

import java.io.Serializable;

import stratego.Piece.PieceType;

/**
 * BoardSetupMessage communicates one player's board setup change after the
 * initialization phase.
 * 
 * @author Caroline O'Neill
 *
 */
public class BoardSetupMessage implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private int color;
	private PieceType[][] initialSetup;
	
	/**
	 * Constructor.
	 * @param color setup color, identifies player setup belongs to
	 * @param initialSetup 4 row by 10 column 2D array with setup
	 */
	public BoardSetupMessage(int color, PieceType[][] initialSetup)
	{
		this.color = color;
		this.initialSetup = initialSetup;
	}
	
	/**
	 * Gets player/color for initial setup (blue/top/server, red/bottom/client).
	 * @return color
	 */
	public int getColor()
	{
		return color;
	}
	
	/**
	 * Gets the initialSetup - a 4(row) x 10(col) 2D array.
	 * @return initialSetup
	 */
	public PieceType[][] getInitialSetup()
	{
		return initialSetup;
	}

}
