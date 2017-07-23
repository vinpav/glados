package fr.vinpav.glados.plugin;

import fr.vinpav.glados.config.Configuration;

public abstract class Plugin implements Runnable {
    private Configuration config;

    public String describe() {
        return config.getProperty("plugin.description");
    }

    public abstract void start();

    public abstract void stop();

    public Configuration getConfiguration() {
        return config;
    }

    public void setConfiguration(Configuration config) {
        this.config = config;
    }
}
