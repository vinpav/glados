package fr.vinpav.glados.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;

public class HelloWorldPlugin extends Plugin {
    protected static final Logger logger = LoggerFactory.getLogger(HelloWorldPlugin.class);

    public HelloWorldPlugin() {

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
            logger.info("Hello world, it's : " + thisSec);
            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

    }
}
