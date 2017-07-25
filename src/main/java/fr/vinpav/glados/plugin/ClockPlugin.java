package fr.vinpav.glados.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;

/* This clock displays the local time every 'plugin.clock.tick' */
public class ClockPlugin extends Plugin {
    protected static final Logger logger = LoggerFactory.getLogger(ClockPlugin.class);

    int tick;

    public ClockPlugin() {
    }

    @Override
    public void play() {
        tick = Integer.parseInt(getConfiguration().getProperty("plugin.clock.tick"));
        super.play();
    }

    @Override
    public void shutdown() {
        logger.info(describe() + " is shutting down.");
        interrupt();
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
                Thread.currentThread().interrupt();
                break;
            }
        }

    }
}
