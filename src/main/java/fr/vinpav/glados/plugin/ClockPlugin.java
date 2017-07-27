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
    }

    @Override
    public void startup() {
        tick = Integer.parseInt(getConfiguration().getProperty("controller.clock.tick"));
        super.startup();
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

    //@Override
    public void execute(List<String> command) {
        switch (command.get(0)) {
            case "help": {
                logger.info(getCommandList());
                break;
            }
            default: {
                logger.info("[Glados] > I don't know what you're talking about.");
            }
        }
    }

    private String getCommandList() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[Glados] > Here's the " + describe() + " command list\n");
        stringBuilder.append("clock_plugin help;                  : this help.\n");

        return stringBuilder.toString();
    }
}
