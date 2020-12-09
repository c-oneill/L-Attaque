package test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import stratego.BoardSetupMessage;
import stratego.Piece;
import stratego.Piece.PieceType;

public class BoardSetupMessageClassTest {
	@Test
	public void constructerTest() {
		
		PieceType[][] pieceTypes = new PieceType[4][10];
		BoardSetupMessage boardSetupMessage = new BoardSetupMessage(Piece.RED, pieceTypes);
		
		assertTrue(boardSetupMessage instanceof BoardSetupMessage);
	}
	@Test
	public void getColorTest() {
		PieceType[][] pieceTypes = new PieceType[4][10];
		BoardSetupMessage boardSetupMessage = new BoardSetupMessage(Piece.RED, pieceTypes);
		
		assertEquals(boardSetupMessage.getColor(), Piece.RED);
		
		PieceType[][] pieceTypes2 = new PieceType[4][10];
		BoardSetupMessage boardSetupMessage2 = new BoardSetupMessage(Piece.BLUE, pieceTypes2);
		
		assertEquals(boardSetupMessage2.getColor(), Piece.BLUE);
		
	}
	@Test
	public void getInitialSetupTest() {
		PieceType[][] pieceTypes = new PieceType[4][10];
		pieceTypes[0][0] = PieceType.FLAG;
		pieceTypes[1][1] = PieceType.GENERAL;
		pieceTypes[1][0] = PieceType.BOMB;
		pieceTypes[0][1] = PieceType.BOMB;
		BoardSetupMessage boardSetupMessage = new BoardSetupMessage(Piece.RED, pieceTypes);
		
		assertEquals(boardSetupMessage.getInitialSetup(), pieceTypes);
		
	}
}
