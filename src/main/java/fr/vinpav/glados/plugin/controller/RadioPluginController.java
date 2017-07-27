package fr.vinpav.glados.plugin.controller;

import fr.vinpav.glados.config.Configuration;
import fr.vinpav.glados.plugin.RadioPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RadioPluginController extends PluginController {
    protected static final Logger logger = LoggerFactory.getLogger(RadioPluginController.class);

    RadioPlugin plugin;

    public RadioPluginController(Configuration aConfig) {
        super(aConfig);
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
            case "list": {
                logger.info(getPlaylist());
                break;
            }
            case "play": {
                try {
                    play(Integer.parseInt(command.get(1)));
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    logger.error("[Glados] > Oh noes ! You broke the radio !", e);
                }
                break;
            }
            case "start": {
                try {
                    startPlugin();
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    logger.error("[Glados] > Oh noes ! You broke the radio !", e);
                }
                break;
            }
            case "stop": {
                try {
                    stopPlugin();
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    logger.error("[Glados] > Oh noes ! You broke the radio !", e);
                }
                break;
            }
            default: {
                logger.info("[Glados] > I don't know what you're talking about.");
            }
        }
    }

    @Override
    public void startPlugin() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.info("Starting " + describe() + "...");
        Class pluginClass = Class.forName(getConfiguration().getProperty("plugin.class.name"));
        plugin = (RadioPlugin) pluginClass.newInstance();
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

    public void play(int channel) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        logger.info("[Glados] > Switching to " + getConfiguration().getProperty("playlist.entry.name." + channel));
        if (plugin != null) {
            plugin.shutdown();
        }
        Class pluginClass = Class.forName(getConfiguration().getProperty("plugin.class.name"));
        plugin = (RadioPlugin) pluginClass.newInstance();
        plugin.setConfiguration(config);
        plugin.setCurrentChannel(channel);
        plugin.startup();
    }

    private String getInfo() {
        StringBuilder info = new StringBuilder();
        info.append(describe());

        if (plugin == null) {
            info.append(" has no channel set at the moment.");
        } else {
            info.append(" is tuned on ");
            info.append(getConfiguration().getProperty("playlist.entry.name." + plugin.getCurrentChannel()));
        }
        return info.toString();
    }

    private String getCommandList() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[Glados] > Here's the " + describe() + " command list\n");
        stringBuilder.append("radio_plugin help;                  : this help.\n");
        stringBuilder.append("radio_plugin info;                  : see channel info.\n");
        stringBuilder.append("radio_plugin list;                  : the radio playlist.\n");
        stringBuilder.append("radio_plugin play [channel number]; : startup the specified entry from the playlist.\n");
        stringBuilder.append("radio_plugin start;                 : turn on the radio.\n");
        stringBuilder.append("radio_plugin stop;                  : turn off the radio.\n");

        return stringBuilder.toString();
    }

    private String getPlaylist() {
        int entryCount = Integer.parseInt(getConfiguration().getProperty("playlist.entry.count"));
        StringBuilder playlist = new StringBuilder();

        for (int i = 1; i<entryCount; i++) {
            playlist.append("\n");
            playlist.append(i);
            playlist.append("/ ");
            playlist.append(getConfiguration().getProperty("playlist.entry.name." + i));
        }

        return playlist.toString();
    }

}
