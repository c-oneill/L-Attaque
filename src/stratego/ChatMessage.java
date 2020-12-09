package stratego;

import java.io.Serializable;

/**
 * ChatMessage communicates communicates chat messages between players.
 * 
 * @author Caroline O'Neill
 */
public class ChatMessage implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private String message;
	private int color;
	
	/**
	 * ChatMessage constructor. 
	 * @param msg message
	 * @param color color of player passing message
	 */
	public ChatMessage(String msg, int color)
	{
		this.message = msg;
		this.color = color;
	}
	
	/**
	 * Gets chat message.
	 * @return message
	 */
	public String getMessage()
	{
		return message;
	}
	
	/**
	 * Gets chat message color.
	 * @return color
	 */
	public int getColor()
	{
		return color;
	}
}
