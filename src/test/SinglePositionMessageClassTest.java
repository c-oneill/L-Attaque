package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import stratego.Piece;
import stratego.Piece.PieceType;
import stratego.SinglePositionMessage;

public class SinglePositionMessageClassTest {
	@Test
	public void constructerTest() {
		
		Piece redPiece = new Piece(PieceType.GENERAL);
		Piece bluePiece = new Piece(PieceType.SCOUT);
		
		redPiece.setColor(Piece.RED);
		bluePiece.setColor(Piece.BLUE);
		
		SinglePositionMessage singlePositionMessage = 
				new SinglePositionMessage(2, 3, redPiece, bluePiece, true);
		assertTrue(singlePositionMessage instanceof SinglePositionMessage);
	}
	@Test
	public void constructerTest2() {
		Piece redPiece = new Piece(PieceType.GENERAL);
		redPiece.setColor(Piece.RED);
		SinglePositionMessage singlePositionMessage = 
				new SinglePositionMessage(1, 0, redPiece);
		assertTrue(singlePositionMessage instanceof SinglePositionMessage);
	}
	@Test
	public void getRowTest() {
		Piece redPiece = new Piece(PieceType.GENERAL);
		Piece bluePiece = new Piece(PieceType.SCOUT);
		
		redPiece.setColor(Piece.RED);
		bluePiece.setColor(Piece.BLUE);
		
		SinglePositionMessage singlePositionMessage = 
				new SinglePositionMessage(2, 3, redPiece, bluePiece, true);

		assertEquals(singlePositionMessage.getRow(), 2);
	}
	@Test
	public void getColTest() {
		Piece redPiece = new Piece(PieceType.GENERAL);
		Piece bluePiece = new Piece(PieceType.SCOUT);
		
		redPiece.setColor(Piece.RED);
		bluePiece.setColor(Piece.BLUE);
		
		SinglePositionMessage singlePositionMessage = 
				new SinglePositionMessage(2, 3, redPiece, bluePiece, true);

		assertEquals(singlePositionMessage.getCol(), 3);
	}
	@Test
	public void getPieceTest() {
		Piece redPiece = new Piece(PieceType.GENERAL);
		Piece bluePiece = new Piece(PieceType.SCOUT);
		
		redPiece.setColor(Piece.RED);
		bluePiece.setColor(Piece.BLUE);
		
		SinglePositionMessage singlePositionMessage = 
				new SinglePositionMessage(2, 3, redPiece, bluePiece, true);

		assertEquals(singlePositionMessage.getPiece(), redPiece);
	}
	@Test
	public void getColorTest() {
		Piece redPiece = new Piece(PieceType.GENERAL);
		Piece bluePiece = new Piece(PieceType.SCOUT);
		
		redPiece.setColor(Piece.RED);
		bluePiece.setColor(Piece.BLUE);
		
		SinglePositionMessage singlePositionMessage = 
				new SinglePositionMessage(2, 3, redPiece, bluePiece, true);

		assertEquals(singlePositionMessage.getColor(), Piece.RED);
	}
	@Test
	public void getPieceTypeTest() {
		Piece redPiece = new Piece(PieceType.GENERAL);
		Piece bluePiece = new Piece(PieceType.SCOUT);
		
		redPiece.setColor(Piece.RED);
		bluePiece.setColor(Piece.BLUE);
		
		SinglePositionMessage singlePositionMessage = 
				new SinglePositionMessage(2, 3, redPiece, bluePiece, true);

		assertEquals(singlePositionMessage.getPieceType(), PieceType.GENERAL);
	}
	@Test
	public void getPieceToRemovePlaceTest() {
		Piece redPiece = new Piece(PieceType.GENERAL);
		Piece bluePiece = new Piece(PieceType.SCOUT);
		
		redPiece.setColor(Piece.RED);
		bluePiece.setColor(Piece.BLUE);
		
		SinglePositionMessage singlePositionMessage = 
				new SinglePositionMessage(2, 3, redPiece, bluePiece, true);

		assertEquals(singlePositionMessage.getPieceToRemovePlace(), bluePiece);
	}
	@Test
	public void isRemovedTest() {
		Piece redPiece = new Piece(PieceType.GENERAL);
		Piece bluePiece = new Piece(PieceType.SCOUT);
		
		redPiece.setColor(Piece.RED);
		bluePiece.setColor(Piece.BLUE);
		
		SinglePositionMessage singlePositionMessage = 
				new SinglePositionMessage(2, 3, redPiece, bluePiece, true);
		assertTrue(singlePositionMessage.isRemoved());
		
		
		SinglePositionMessage singlePositionMessage2 = 
				new SinglePositionMessage(1, 0, redPiece);
		assertFalse(singlePositionMessage2.isRemoved());
	}
}
