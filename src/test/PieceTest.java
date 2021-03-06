package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import stratego.Piece;
import stratego.Piece.PieceType;

public class PieceTest {

	@Test
	public void whoWinsTest() {
		Piece redPiece = new Piece(PieceType.GENERAL);
		Piece bluePiece = new Piece(PieceType.GENERAL);
		Piece lakePiece = new Piece(PieceType.LAKE);
		Piece emptyPiece = new Piece(PieceType.EMPTY);
		
		redPiece.setColor(Piece.RED);
		bluePiece.setColor(Piece.BLUE);
		lakePiece.setColor(Piece.NONE);
		emptyPiece.setColor(Piece.NONE);
		
		assertEquals(Piece.whoWins(redPiece, lakePiece), -1);
		assertEquals(Piece.whoWins(lakePiece, redPiece), -1);
		assertEquals(Piece.whoWins(redPiece, emptyPiece), 1);
		assertEquals(Piece.whoWins(redPiece, redPiece), -1);
		assertEquals(Piece.whoWins(redPiece, bluePiece), 0);
		
		redPiece = new Piece(PieceType.BOMB);
		redPiece.setColor(Piece.RED);
		assertEquals(Piece.whoWins(bluePiece, redPiece), 2);
		bluePiece = new Piece(PieceType.MINER);
		redPiece.setColor(Piece.RED);
		bluePiece.setColor(Piece.BLUE);
		assertEquals(Piece.whoWins(redPiece, bluePiece), -1);
		assertEquals(Piece.whoWins(bluePiece, redPiece), 1);
		
		redPiece = new Piece(PieceType.SPY);
		redPiece.setColor(Piece.RED);
		assertEquals(Piece.whoWins(redPiece, bluePiece), 2);
		bluePiece = new Piece(PieceType.MARSHAL);
		bluePiece.setColor(Piece.BLUE);
		assertEquals(Piece.whoWins(redPiece, bluePiece), 1);
		
		redPiece = new Piece(PieceType.GENERAL);
		bluePiece = new Piece(PieceType.CAPTAIN);
		redPiece.setColor(Piece.RED);
		bluePiece.setColor(Piece.BLUE);
		assertEquals(Piece.whoWins(redPiece, bluePiece), 1);
		assertEquals(Piece.whoWins(bluePiece, redPiece), 2);
		
	}
	@Test
	public void isMoveValidTest() {
		Piece redPiece = new Piece(PieceType.GENERAL);
		Piece bluePiece = new Piece(PieceType.SCOUT);
		Piece lakePiece = new Piece(PieceType.LAKE);
		Piece emptyPiece = new Piece(PieceType.EMPTY);
		
		redPiece.setColor(Piece.RED);
		bluePiece.setColor(Piece.BLUE);
		lakePiece.setColor(Piece.NONE);
		emptyPiece.setColor(Piece.NONE);
		
		assertFalse(lakePiece.isMoveValid(0, 0, 1, 1, PieceType.LAKE));
		assertFalse(emptyPiece.isMoveValid(0, 0, 1, 1, PieceType.EMPTY));
		assertFalse(redPiece.isMoveValid(1, 1, 2, 2, PieceType.GENERAL));
		assertFalse(redPiece.isMoveValid(9, 9, 10, 9, PieceType.GENERAL));
		assertFalse(redPiece.isMoveValid(9, 9, 9, 10, PieceType.GENERAL));
		assertFalse(redPiece.isMoveValid(3, 3, 4, 3, PieceType.GENERAL));
		assertFalse(redPiece.isMoveValid(3, 3, 5, 3, PieceType.GENERAL));
		assertFalse(redPiece.isMoveValid(3, 1, 4, 2, PieceType.GENERAL));
		assertFalse(redPiece.isMoveValid(3, 3, 4, 3, PieceType.GENERAL));
		assertFalse(redPiece.isMoveValid(3, 6, 4, 6, PieceType.GENERAL));
		assertFalse(redPiece.isMoveValid(3, 7, 4, 7, PieceType.GENERAL));
		assertFalse(redPiece.isMoveValid(6, 1, 5, 2, PieceType.GENERAL));
		assertFalse(redPiece.isMoveValid(6, 3, 5, 3, PieceType.GENERAL));
		assertFalse(redPiece.isMoveValid(6, 6, 5, 6, PieceType.GENERAL));
		assertFalse(redPiece.isMoveValid(6, 7, 5, 7, PieceType.GENERAL));
		
		assertFalse(redPiece.isMoveValid(0, 0, 0, 2, PieceType.GENERAL));
		assertFalse(redPiece.isMoveValid(0, 0, 2, 0, PieceType.GENERAL));
		
		assertTrue(redPiece.isMoveValid(4, 4, 4, 5, PieceType.GENERAL));
		assertTrue(redPiece.isMoveValid(3, 3, 2, 3, PieceType.GENERAL));
		assertTrue(bluePiece.isMoveValid(1, 1, 3, 1, PieceType.SCOUT));
		assertTrue(bluePiece.isMoveValid(1, 1, 1, 3, PieceType.SCOUT));
		assertTrue(bluePiece.isMoveValid(3, 3, 1, 3, PieceType.SCOUT));
		assertTrue(bluePiece.isMoveValid(3, 3, 3, 1, PieceType.SCOUT));
		assertTrue(bluePiece.isMoveValid(1, 1, 1, 2, PieceType.SCOUT));
		
	}
	@Test
	public void isMoveableTest() {
		Piece redPiece = new Piece(PieceType.GENERAL);
		Piece lakePiece = new Piece(PieceType.LAKE);
		Piece emptyPiece = new Piece(PieceType.EMPTY);
		Piece flagPiece = new Piece(PieceType.FLAG);
		Piece bombPiece = new Piece(PieceType.BOMB);
		
		assertTrue(redPiece.isMoveable());
		assertFalse(lakePiece.isMoveable());
		assertFalse(emptyPiece.isMoveable());
		assertFalse(flagPiece.isMoveable());
		assertFalse(bombPiece.isMoveable());
	}
	@Test
	public void setColorTest() {
		Piece redPiece = new Piece(PieceType.GENERAL);
		Piece bluePiece = new Piece(PieceType.GENERAL);
		Piece lakePiece = new Piece(PieceType.LAKE);
		Piece emptyPiece = new Piece(PieceType.EMPTY);
			
		assertTrue(redPiece.setColor(Piece.RED));
		assertTrue(bluePiece.setColor(Piece.BLUE));
		assertTrue(lakePiece.setColor(Piece.NONE));
		assertTrue(emptyPiece.setColor(Piece.NONE));
		assertFalse(new Piece(PieceType.GENERAL).setColor(3));
		
		
	}
	@Test
	public void toStringTest() {
		Piece piece = new Piece(PieceType.GENERAL);
		assertEquals(piece.toString(), "8");
	}
	@Test
	public void levelTest() {
		Piece piece1 = new Piece(PieceType.EMPTY);
		Piece piece2 = new Piece(PieceType.LAKE);
		Piece piece3 = new Piece(PieceType.MARSHAL);
		Piece piece4 = new Piece(PieceType.GENERAL);
		Piece piece5 = new Piece(PieceType.COLONEL);
		Piece piece6 = new Piece(PieceType.MAJOR);
		Piece piece7 = new Piece(PieceType.CAPTAIN);
		Piece piece8 = new Piece(PieceType.LIEUTENANT);
		Piece piece9 = new Piece(PieceType.SERGEANT);
		Piece piece10 = new Piece(PieceType.MINER);
		Piece piece11 = new Piece(PieceType.SCOUT);
		Piece piece12 = new Piece(PieceType.BOMB);
		Piece piece13 = new Piece(PieceType.SPY);
		Piece piece14 = new Piece(PieceType.FLAG);
		
		assertEquals(piece1.level(), -1);
		assertEquals(piece2.level(), -2);
		assertEquals(piece3.level(), 9);
		assertEquals(piece4.level(), 8);
		assertEquals(piece5.level(), 7);
		assertEquals(piece6.level(), 6);
		assertEquals(piece7.level(), 5);
		assertEquals(piece8.level(), 4);
		assertEquals(piece9.level(), 3);
		assertEquals(piece10.level(), 2);
		assertEquals(piece11.level(), 1);
		assertEquals(piece12.level(), 0);
		assertEquals(piece13.level(), 0);
		assertEquals(piece14.level(), 0);
	}
}
