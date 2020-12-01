package test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import stratego.Piece;
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
			Piece[][] grid = new Piece[10][10];
			model.setBoard(grid, Piece.RED);
			assertEquals(model.getPosition(2, 2), Piece.EMPTY);
			model.setPosition(2, 2, Piece.GENERAL);
			assertEquals(model.getPosition(2, 2), Piece.GENERAL);
			assertEquals(model.removePosition(2, 2), Piece.GENERAL);
			assertEquals(model.getPosition(2, 2), Piece.EMPTY);
			
			assertFalse(model.removePiece(Piece.GENERAL));
			
			Piece piece = Piece.CAPTAIN;
			piece.setColor(Piece.BLUE);
			model.setBoard(grid, Piece.BLUE);
			model.setPosition(8, 8, piece);
			assertTrue(model.removePiece(Piece.CAPTAIN));
			
			piece = Piece.CAPTAIN;
			piece.setColor(Piece.RED);
			model.setBoard(grid, Piece.RED);
			model.setPosition(8, 8, piece);
			assertTrue(model.removePiece(Piece.CAPTAIN));
			
		}
		@Test
		public void getRedPiecesTest() {
			StrategoModel model = new StrategoModel();
			HashMap<Piece, Integer> redPieces = new HashMap<Piece, Integer>();
			redPieces.put(Piece.MARSHAL, 1);
			redPieces.put(Piece.GENERAL, 1);
			redPieces.put(Piece.COLONEL, 2);
			redPieces.put(Piece.MAJOR, 3);
			redPieces.put(Piece.CAPTAIN, 4);
			redPieces.put(Piece.LIEUTENANT, 4);
			redPieces.put(Piece.SERGEANT, 4);
			redPieces.put(Piece.MINER, 5);
			redPieces.put(Piece.SCOUT, 8);
			redPieces.put(Piece.SPY, 1);
			redPieces.put(Piece.FLAG, 1);
			assertEquals(model.getRedPieces(), redPieces);
		}
		@Test
		public void getBluePiecesTest() {
			StrategoModel model = new StrategoModel();
			HashMap<Piece, Integer> bluePieces = new HashMap<Piece, Integer>();
			bluePieces.put(Piece.MARSHAL, 1);
			bluePieces.put(Piece.GENERAL, 1);
			bluePieces.put(Piece.COLONEL, 2);
			bluePieces.put(Piece.MAJOR, 3);
			bluePieces.put(Piece.CAPTAIN, 4);
			bluePieces.put(Piece.LIEUTENANT, 4);
			bluePieces.put(Piece.SERGEANT, 4);
			bluePieces.put(Piece.MINER, 5);
			bluePieces.put(Piece.SCOUT, 8);
			bluePieces.put(Piece.SPY, 1);
			bluePieces.put(Piece.FLAG, 1);
			assertEquals(model.getBluePieces(), bluePieces);
		}
}
