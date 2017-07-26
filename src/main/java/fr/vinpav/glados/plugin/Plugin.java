package fr.vinpav.glados.plugin;

import fr.vinpav.glados.command.controller.CommandController;
import fr.vinpav.glados.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Plugin extends Thread implements CommandController {
    protected static final Logger logger = LoggerFactory.getLogger(Plugin.class);

    private Configuration config;

    public String describe() {
        return config.getProperty("plugin.description");
    }

    public void play() {
        logger.info("Starting " + describe());
        start();
    }

    public abstract void shutdown();

    public Configuration getConfiguration() {
        return config;
    }

    public void setConfiguration(Configuration config) {
        this.config = config;
    }

}
