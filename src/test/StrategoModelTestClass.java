package test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import stratego.Piece;
import stratego.Piece.PieceType;
import stratego.StrategoModel;

	public class StrategoModelTestClass {

		@Test
		public void constructerTest() {
			StrategoModel model = new StrategoModel();
			
			assertTrue(model instanceof StrategoModel);
		}
		@Test
		public void setBoardTest() {
			StrategoModel model = new StrategoModel();
			PieceType[][] grid = new PieceType[10][10];
			Piece redPiece = new Piece(PieceType.GENERAL);
			redPiece.setColor(Piece.RED);
			model.setBoard(grid, redPiece.color());
			Piece emptyPiece = new Piece(PieceType.EMPTY);
			emptyPiece.setColor(Piece.NONE);
			model.setBoard(grid, emptyPiece.color());
		}
		@Test
		public void positionTest() {
			StrategoModel model = new StrategoModel();
			PieceType[][] grid = new PieceType[10][10];
			Piece redPiece = new Piece(PieceType.GENERAL);
			redPiece.setColor(Piece.RED);
			model.setPosition(2, 2, redPiece);
			
			assertEquals(model.getPosition(2, 2), redPiece);
			assertTrue(model.removePiece(redPiece));
			assertEquals(model.removePosition(2, 2), redPiece);
			
			Piece bluePiece = new Piece(PieceType.GENERAL);
			bluePiece.setColor(Piece.BLUE);
			model.setPosition(1, 1, bluePiece);
			
			assertEquals(model.getPosition(1, 1), bluePiece);
			assertTrue(model.removePiece(bluePiece));
			assertEquals(model.removePosition(1, 1), bluePiece);
			
			Piece lakePiece = new Piece(PieceType.LAKE);
			lakePiece.setColor(Piece.NONE);
			model.setPosition(3, 3, lakePiece);
			
			assertEquals(model.getPosition(3, 3), lakePiece);
			assertFalse(model.removePiece(lakePiece));
			assertEquals(model.removePosition(3, 3), lakePiece);
			
		}
		@Test
		public void getRedPiecesTest() {
			StrategoModel model = new StrategoModel();
			HashMap<PieceType, Integer> redPieces = new HashMap<PieceType, Integer>();
			redPieces.put(PieceType.MARSHAL, 1);
			redPieces.put(PieceType.GENERAL, 1);
			redPieces.put(PieceType.COLONEL, 2);
			redPieces.put(PieceType.MAJOR, 3);
			redPieces.put(PieceType.CAPTAIN, 4);
			redPieces.put(PieceType.LIEUTENANT, 4);
			redPieces.put(PieceType.SERGEANT, 4);
			redPieces.put(PieceType.MINER, 5);
			redPieces.put(PieceType.SCOUT, 8);
			redPieces.put(PieceType.BOMB, 6);
			redPieces.put(PieceType.SPY, 1);
			redPieces.put(PieceType.FLAG, 1);
			assertEquals(model.getRedPieces(), redPieces);
		}
		@Test
		public void getBluePiecesTest() {
			StrategoModel model = new StrategoModel();
			HashMap<PieceType, Integer> bluePieces = new HashMap<PieceType, Integer>();
			bluePieces.put(PieceType.MARSHAL, 1);
			bluePieces.put(PieceType.GENERAL, 1);
			bluePieces.put(PieceType.COLONEL, 2);
			bluePieces.put(PieceType.MAJOR, 3);
			bluePieces.put(PieceType.CAPTAIN, 4);
			bluePieces.put(PieceType.LIEUTENANT, 4);
			bluePieces.put(PieceType.SERGEANT, 4);
			bluePieces.put(PieceType.MINER, 5);
			bluePieces.put(PieceType.SCOUT, 8);
			bluePieces.put(PieceType.BOMB, 6);
			bluePieces.put(PieceType.SPY, 1);
			bluePieces.put(PieceType.FLAG, 1);
			assertEquals(model.getBluePieces(), bluePieces);
		}
}
