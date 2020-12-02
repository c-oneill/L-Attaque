package stratego;

import javafx.beans.property.SimpleStringProperty;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class contains the functionality of a timer.
 * 
 * <p> The time is formatted mm:ss as a string in a {@link SimpleStringProperty}
 * wrapper class. This allows a listener to be set up for updating the UI.
 * 
 * @author Kristopher Rangel
 *
 */
public class Timer extends Thread {
    private Thread thread;
    private final AtomicBoolean running;
    private SimpleStringProperty clock;
    private int time; // in milliseconds
    private final int GRADIENT = 50; // timer update period
    
    /**
     * Constructor.
     * 
     * @param time - the time in milliseconds to initialize the timer to
     * 
     * @author Kristopher Rangel
     */
    public Timer(int time) {
        running = new AtomicBoolean(false);
        this.time = time;
        clock = new SimpleStringProperty("00:00");
    }
    
    
    /**
     * <ul><b><i>startTimer</i></b></ul>
     * <ul><ul><p><code>public void startTimer () </code></p></ul>
     *
     * Starts the thread and initiates the timer.
     *
     * @author Kristopher Rangel
     */
    public void startTimer() {
        this.thread = new Thread(this);
        running.set(true);
        thread.setPriority(NORM_PRIORITY);
        thread.start();
    }
    
    /**
     * <ul><b><i>stopTimer</i></b></ul>
     * <ul><ul><p><code>public void stopTimer () </code></p></ul>
     *
     * This method interrupts the thread and sets the running flag to false.
     *
     * @author Kristopher Rangel
     */
    public void stopTimer() {
        running.set(false);
        if(thread != null && thread.isAlive()) {
            thread.interrupt();
        }
    }
    
    /**
     * <ul><b><i>updateTime</i></b></ul>
     * <ul><ul><p><code>private void updateTime () </code></p></ul>
     *
     * Updates the current time string representation. If the
     * timer has reached zero, it invokes {@link #stopTimer()}.
     * 
     * @author Kristopher Rangel
     */
    private void updateTime() {
        int seconds = time / 1000;
        int minutes = seconds / 60;
        seconds %= 60;
        String minStr = String.valueOf(minutes);
        if(minutes < 10) { minStr = "0" + minStr; }
        String secStr = String.valueOf(seconds);
        if(seconds < 10) { secStr = "0" + secStr; }
        
        clock.set(minStr + ":" + secStr);
        
        if(minutes == 0 && seconds == 0) {
            stopTimer();
        }
    }
    
    /**
     * <ul><b><i>getTime</i></b></ul>
     * <ul><ul><p><code>public SimpleStringProperty getTime () </code></p></ul>
     *
     * Returns the current time as a {@link SimpleStringProperty} object.
     *
     * @return - the current time
     * 
     * @author Kristopher Rangel
     */
    public SimpleStringProperty getTime() { return this.clock; }
    
    /**
     * <ul><b><i>run</i></b></ul>
     * <ul><ul><p><code>public void run () </code></p></ul>
     *
     * Updates the time for the timer as long as the running flag is set to true.
     * 
     * @author Kristopher Rangel
     *
     */
    @Override
    public void run() {
        while(running.get()) {
            try {
                while(!thread.isInterrupted()) {
                    sleep(GRADIENT);
                    time -= GRADIENT;
                    updateTime();
                }
            }catch (Exception e) { System.out.println("Timer stopped."); }
        }
    }
    
}
