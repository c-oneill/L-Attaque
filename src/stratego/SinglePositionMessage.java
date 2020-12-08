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
	private Piece removePlace; // piece to remove/place
	private boolean removing; // true = remove, false = add
	
	/**
	 * SinglePositionMessage constructor.
	 * @param row row position
	 * @param col column position
	 * @param piece piece at position
	 * @param removing piece to remove (null if no piece to remove) 
	 * the board
	 */
	public SinglePositionMessage(int row, int col, Piece piece, Piece removePlace, boolean removing)
	{
		this.row = row;
		this.col = col;
		this.piece = piece;
		this.removePlace = removePlace;
		this.removing = removing;
	}
	
	/**
	 * SinglePositionMessage constructor. Null for piece to remove.
	 * @param row row position
	 * @param col column position
	 * @param piece piece at position
	 * the board
	 */
	public SinglePositionMessage(int row, int col, Piece piece)
	{
		this(row, col, piece, null, false);
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
	
	/**
	 * Gets piece to remove from the board. Null if no piece to remove
	 * @return true if piece in message is removed, false otherwise
	 */
	public Piece getPieceToRemovePlace()
	{
		return removePlace;
	}
	
	/**
	 * Indicates is the remove/place piece is removed or added.
	 * @return true if removed, false if added
	 */
	public boolean isRemoved()
	{
		return removing;
	}
	
}

