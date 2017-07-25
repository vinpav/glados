package fr.vinpav.glados.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;

/* This clock displays the local time every minute */
public class ClockPlugin extends Plugin {
    protected static final Logger logger = LoggerFactory.getLogger(ClockPlugin.class);

    int tick;

    public ClockPlugin() {
    }

    @Override
    public void start() {
        tick = Integer.parseInt(getConfiguration().getProperty("plugin.clock.tick"));
        new Thread(this).start();
    }

    @Override
    public void stop() {

    }

    @Override
    public void run() {
        LocalTime thisSec;

        for(;;) {
            thisSec = LocalTime.now();
            logger.info("Time is : " + thisSec);
            try {
                Thread.sleep(tick);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
