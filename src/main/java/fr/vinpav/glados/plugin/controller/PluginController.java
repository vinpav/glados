package fr.vinpav.glados.plugin.controller;

import fr.vinpav.glados.command.controller.CommandController;
import fr.vinpav.glados.config.Configuration;

public abstract class PluginController implements CommandController {
    Configuration config;

    public PluginController(Configuration aConfig) {
        config = aConfig;
    }

    public abstract void startPlugin() throws ClassNotFoundException, IllegalAccessException, InstantiationException;

    public abstract void stopPlugin() throws ClassNotFoundException, IllegalAccessException, InstantiationException;

    public String describe() {
        return config.getProperty("plugin.description");
    }

    public Configuration getConfiguration() {
        return config;
    }

    public void setConfiguration(Configuration config) {
        this.config = config;
    }

    public abstract String getCommandList();
}
