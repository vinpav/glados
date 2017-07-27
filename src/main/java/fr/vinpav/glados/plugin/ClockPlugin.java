package fr.vinpav.glados.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.util.List;

/* This clock displays the local time every 'controller.clock.tick' */
public class ClockPlugin extends Plugin {
    protected static final Logger logger = LoggerFactory.getLogger(ClockPlugin.class);

    int tick;

    public ClockPlugin() {
        setName("clock_plugin");
    }

    @Override
    public void startup() {
        tick = Integer.parseInt(getConfiguration().getProperty("plugin.clock.tick"));
        super.startup();
    }

    @Override
    public void shutdown() {
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
