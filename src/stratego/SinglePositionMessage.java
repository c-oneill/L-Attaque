package stratego;

import java.io.Serializable;

import stratego.Piece.PieceType;

/**
 * SingleMoveMessage communicates a single board piece change during 
 * 'battle-mode'.
 * 
 * @author Caroline O'Neill
 *
 */
public class SinglePositionMessage implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private int row;
	private int col;
	private Piece piece;
	
	/**
	 * SinglePositionMessage constructor.
	 * @param row row position
	 * @param col column position
	 * @param piece piece at position
	 */
	public SinglePositionMessage(int row, int col, Piece piece)
	{
		this.row = row;
		this.col = col;
		this.piece = piece;
	}
	
	/**
	 * Gets row of position.
	 * @return row
	 */
	public int getRow()
	{
		return row;
	}
	
	/**
	 * Gets column of position.
	 * @return column
	 */
	public int getCol()
	{
		return col;
	}
	
	/**
	 * Gets piece at position.
	 * @return piece
	 */
	public Piece getPiece()
	{
		return piece;
	}
	
	/**
	 * Gets piece color at position.
	 * @return piece color
	 */
	public int getColor()
	{
		return piece.color();
	}
	
	/**
	 * Gets piece {@link PieceType} at position.
	 * @return piece type
	 */
	public PieceType getPieceType()
	{
		return piece.type;
	}
	
}

