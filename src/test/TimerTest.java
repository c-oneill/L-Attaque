package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleStringProperty;
import stratego.Timer;

public class TimerTest {
	@Test
	public void constructerTest() {
		Timer timer = new Timer(120000);
		assertTrue(timer instanceof Timer);
	}
	@Test
	public void getTimeTest() {
		Timer timer = new Timer(120000);
		SimpleStringProperty simpleStringProperty = new SimpleStringProperty("00:00");
		timer.startTimer();
		timer.stopTimer();
		assertEquals(timer.getTime().get(), simpleStringProperty.get());
		
	}
}
