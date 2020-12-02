package stratego;

import java.io.Serializable;

/**
 * BoardSetupMessage communicates a one player's board setup change after the
 * initialization phase.
 * 
 * @author Caroline O'Neill
 *
 */
public class BoardSetupMessage implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private int color;
	private Piece[][] initialSetup;
	
	public BoardSetupMessage(int color, Piece[][] initialSetup)
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
	public Piece[][] getInitialSetup()
	{
		return initialSetup;
	}

}
