package fr.vinpav.glados.plugin.controller;

import fr.vinpav.glados.config.Configuration;
import fr.vinpav.glados.plugin.ClockPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ClockPluginController extends PluginController {
    protected static final Logger logger = LoggerFactory.getLogger(ClockPluginController.class);

    ClockPlugin plugin;

    public ClockPluginController(Configuration aConfig) {
        super(aConfig);
    }

    @Override
    public void startPlugin() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.info("Starting " + describe() + "...");
        Class pluginClass = Class.forName(getConfiguration().getProperty("plugin.class.name"));
        plugin = (ClockPlugin) pluginClass.newInstance();
        plugin.setConfiguration(config);
        plugin.startup();
        logger.info(describe() + " started.");
    }

    @Override
    public void stopPlugin() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.info(describe() + " is shutting down...");
        if (plugin != null) {
            plugin.shutdown();
        }
        logger.info(describe() + " stopped.");
    }

    @Override
    public String getCommandList() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[Glados] > Here's the " + describe() + " command list\n");
        stringBuilder.append("clock_plugin help;  : this help.\n");
        stringBuilder.append("clock_plugin info;  : get configuration info.\n");
        stringBuilder.append("clock_plugin start; : turn the clock on.\n");
        stringBuilder.append("clock_plugin stop;  : guess what...\n");

        return stringBuilder.toString();
    }

    @Override
    public void execute(List<String> command) {
        switch (command.get(0)) {
            case "help": {
                logger.info(getCommandList());
                break;
            }
            case "info": {
                logger.info(getInfo());
                break;
            }
            case "start": {
                try {
                    startPlugin();
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    logger.error("[Glados] > So much time wasted...", e);
                }
                break;
            }
            case "stop": {
                try {
                    stopPlugin();
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    logger.error("[Glados] > So much time wasted...", e);
                }
                break;
            }
            default: {
                logger.info("[Glados] > I don't know what you're talking about.");
            }
        }
    }

    private String getInfo() {
        StringBuilder info = new StringBuilder();
        info.append(describe());
        info.append(" tick rate is ");
        info.append(getConfiguration().getProperty("plugin.clock.tick"));
        info.append(" milliseconds.");
        return info.toString();
    }
}
