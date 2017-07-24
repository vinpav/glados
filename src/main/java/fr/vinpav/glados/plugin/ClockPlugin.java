package fr.vinpav.glados.plugin;

import java.time.LocalTime;

/* This clock displays the local time every minute */
public class ClockPlugin extends Plugin {

    public ClockPlugin() {
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void run() {
        LocalTime thisSec;

        for(;;) {
            thisSec = LocalTime.now();
            System.out.println("Time is : " + thisSec);
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
