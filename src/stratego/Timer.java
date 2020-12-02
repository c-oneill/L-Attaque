package stratego;

import javafx.beans.property.SimpleStringProperty;
import java.util.concurrent.atomic.AtomicBoolean;

public class Timer extends Thread {
    private Thread thread;
    private final AtomicBoolean running;
    private SimpleStringProperty clock;
    private int time; // in milliseconds
    private final int GRADIENT = 50; // timer update period
    
    public Timer(int time) {
        running = new AtomicBoolean(false);
        this.time = time;
        clock = new SimpleStringProperty("00:00");
    }
    
    
    public void startTimer() {
        this.thread = new Thread(this);
        running.set(true);
        thread.setPriority(NORM_PRIORITY);
        thread.start();
    }
    
    public void stopTimer() {
        running.set(false);
        if(thread != null && thread.isAlive()) {
            thread.interrupt();
        }
    }
    
    private void updateTime() {
        int seconds = time / 1000;
        int minutes = seconds / 60;
        seconds %= 60;
        String minStr = String.valueOf(minutes);
        if(minutes < 10) { minStr = "0" + minStr; }
        String secStr = String.valueOf(seconds);
        if(seconds < 10) { secStr = "0" + secStr; }
        
        clock.set(minStr + ":" + secStr);
        
        System.out.printf("%s\n", clock.get());
        
        if(minutes == 0 && seconds == 0) {
            stopTimer();
            System.out.println("Timer done!");
            
            try {
                this.join();
            } catch (InterruptedException e) {
                
                e.printStackTrace();
            }
        }
    }
    
    public SimpleStringProperty getTime() { return this.clock; }
    
    @Override
    public void run() {
        while(running.get()) {
            try {
                while(!thread.isInterrupted()) {
                    sleep(GRADIENT);
                    time -= GRADIENT;
                    updateTime();
                }
            }catch (Exception e) {
                System.out.println("Timer stopped.");
            }
        }
    }
    
}
