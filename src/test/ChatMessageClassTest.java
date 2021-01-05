package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import stratego.ChatMessage;
import stratego.Piece;

public class ChatMessageClassTest {
	@Test
	public void constructorTest() {
		ChatMessage chatMessage = new ChatMessage("Hello", Piece.RED);
		assertTrue(chatMessage instanceof ChatMessage);
	}
	@Test
	public void getMessageTest() {
		ChatMessage chatMessage = new ChatMessage("Hello", Piece.RED);
		assertEquals(chatMessage.getMessage(), "Hello");
	}
	@Test
	public void getColorTest() {
		ChatMessage chatMessage = new ChatMessage("Hello", Piece.RED);
		assertEquals(chatMessage.getColor(), Piece.RED);
	}
}
