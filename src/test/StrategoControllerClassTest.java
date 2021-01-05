package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import javafx.scene.paint.Color;
import stratego.Piece;
import stratego.StrategoController;
import stratego.StrategoModel;
import stratego.StrategoView;
import stratego.Piece.PieceType;

public class StrategoControllerClassTest {
	@Test
	public void constructerTest() {
		
		StrategoController strategoController = new StrategoController();
		assertTrue(strategoController instanceof StrategoController);
		strategoController.setModelObserver(new StrategoView());
	}

	@Test
	public void addToSetupTest() {
		StrategoController strategoController = new StrategoController();
		
		assertTrue(strategoController.addToSetup(0, 0, PieceType.GENERAL, Piece.RED));
		assertFalse(strategoController.addToSetup(1, 0, PieceType.GENERAL, Piece.RED));
		assertTrue(strategoController.addToSetup(0, 0, PieceType.GENERAL, Piece.BLUE));
		assertFalse(strategoController.addToSetup(0, 1, PieceType.GENERAL, Piece.BLUE));

		assertFalse(strategoController.addToSetup(5, 5, PieceType.LAKE, Piece.NONE));
		
		assertTrue(strategoController.addToSetup(0, 0, PieceType.SPY, Piece.RED));
		assertTrue(strategoController.addToSetup(0, 0, PieceType.SPY, Piece.BLUE));
		
	}
	@Test
	public void getPositionTest() {
		StrategoController strategoController = new StrategoController();
		Piece piece = new Piece(PieceType.EMPTY);
		assertEquals(strategoController.getPosition(0, 0).toString(), piece.toString());
	}
	@Test
	public void setOtherPlayerBoardTest() {
		StrategoController strategoController = new StrategoController();
		strategoController.setOtherPlayerBoard(Piece.BLUE);
		strategoController.setOtherPlayerBoard(Piece.RED);
	}
	@Test
	public void checkAvailableTest() {
		StrategoController strategoController = new StrategoController();
		assertEquals(strategoController.checkAvailable(PieceType.GENERAL, Color.RED), 1);
		assertEquals(strategoController.checkAvailable(PieceType.GENERAL, Color.BLUE), 1);
	}
	@Test
	public void winnerTest() {
		// tie or no one won yet
		StrategoController strategoController = new StrategoController();
		strategoController.setOtherPlayerBoard(Piece.BLUE);
		strategoController.setOtherPlayerBoard(Piece.RED);
		assertEquals(strategoController.winner(), Piece.NONE);
		
		// blue attack red flag, blue win
		Piece redFlag = new Piece(PieceType.FLAG);
		redFlag.setColor(Piece.RED);
		strategoController.removeAddPiece(redFlag, true);
		assertEquals(strategoController.winner(), Piece.BLUE);
		
		// red attack blue flag, red win
		StrategoController strategoController2 = new StrategoController();
		strategoController2.setOtherPlayerBoard(Piece.BLUE);
		strategoController2.setOtherPlayerBoard(Piece.RED);
		
		Piece blueFlag = new Piece(PieceType.FLAG);
		blueFlag.setColor(Piece.BLUE);
		strategoController2.removeAddPiece(blueFlag, true);
		assertEquals(strategoController2.winner(), Piece.RED);
		
		// no piece in blue is able to move, red win
		StrategoController strategoController3 = new StrategoController();
		strategoController3.setOtherPlayerBoard(Piece.BLUE);
		strategoController3.setOtherPlayerBoard(Piece.RED);
		
		StrategoModel strategoModel = new StrategoModel();
		HashMap<PieceType, Integer> bluePieces = strategoModel.getBluePieces();
		HashMap<PieceType, Integer> redPieces = strategoModel.getRedPieces();
		bluePieces.remove(PieceType.FLAG);

		for(PieceType pieceType : bluePieces.keySet()) {
			Piece [] piece = new Piece[bluePieces.get(pieceType)];
			for(int j = 0; j < bluePieces.get(pieceType); j++) {
				piece[j] = new Piece(pieceType);
				piece[j].setColor(Piece.BLUE);
			}
			for(int i = 0; i < bluePieces.get(pieceType); i++) {
				strategoController3.removeAddPiece(piece[i], true);
			}
		}
		assertEquals(strategoController3.winner(), Piece.RED);
		
		
		// no piece in red is able to move, blue win
		StrategoController strategoController4 = new StrategoController();
		strategoController4.setOtherPlayerBoard(Piece.BLUE);
		strategoController4.setOtherPlayerBoard(Piece.RED);
		
		StrategoModel strategoModel2 = new StrategoModel();
		HashMap<PieceType, Integer> bluePieces2 = strategoModel2.getBluePieces();
		HashMap<PieceType, Integer> redPieces2 = strategoModel2.getRedPieces();
		redPieces2.remove(PieceType.FLAG);

		for(PieceType pieceType : redPieces2.keySet()) {
			Piece [] piece2 = new Piece[redPieces2.get(pieceType)];
			for(int j = 0; j < redPieces2.get(pieceType); j++) {
				piece2[j] = new Piece(pieceType);
				piece2[j].setColor(Piece.RED);
			}
			for(int i = 0; i < redPieces2.get(pieceType); i++) {
				strategoController4.removeAddPiece(piece2[i], true);
			}
		}
		assertEquals(strategoController4.winner(), Piece.BLUE);
	}
	
	@Test
	public void removeAddPieceTest() {
		StrategoController strategoController = new StrategoController();
		Piece lake = new Piece(PieceType.LAKE);
		lake.setColor(Piece.NONE);
		strategoController.removeAddPiece(lake, false);
		strategoController.removeAddPiece(null, false);
		
		Piece piece = new Piece(PieceType.GENERAL);
		piece.setColor(Piece.BLUE);
		strategoController.removeAddPiece(piece, false);
	}
	@Test
	public void movePieceTest(){
		StrategoController strategoController = new StrategoController();
		assertFalse(strategoController.movePiece(0, 0, 1, 0));
		assertFalse(strategoController.movePiece(4, 2, 4, 3));
		
	}
}
