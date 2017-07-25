package fr.vinpav.glados.plugin;

import fr.vinpav.glados.config.Configuration;
import fr.vinpav.glados.exception.GladosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PluginManager {
    protected static final Logger logger = LoggerFactory.getLogger(PluginManager.class);

    private static PluginManager ourInstance = new PluginManager();

    private Configuration config;
    public Map plugins;

    public static PluginManager getInstance() {
        return ourInstance;
    }

    private PluginManager() {

    }

    public void initialize(Configuration config) throws GladosException {
        this.config = config;
        plugins = new HashMap();

        for (String pluginName : getPluginNames()) {
            try {
                register(pluginName);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                logger.info("Warning : Unable to register plugin " + pluginName);
                e.printStackTrace();
            }
        }
    }

    public void executeAll() {
        Collection<Plugin> pluginCollection = plugins.values();

        Iterator<Plugin> it = pluginCollection.iterator();

        while (it.hasNext()) {
            Plugin current = it.next();
            current.start();
        }
    }

    private void register(String pluginName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.info("Registering plugin : " + pluginName + "...");
        Configuration pluginConfig = new Configuration(pluginName + ".config");
        try {
            pluginConfig.load();
            Class pluginClass = Class.forName(pluginConfig.getProperty("plugin.class.name"));
            Plugin pluginInstance = (Plugin) pluginClass.newInstance();
            pluginInstance.setConfiguration(pluginConfig);
            plugins.put(pluginName, pluginInstance);
            logger.info(pluginInstance.describe() + " is online.");
        } catch (GladosException e) {
            logger.info("Warning, plugin " + pluginName + " not loaded : " + e.getMessage());
        }
    }

    public Set getPluginList() {
        return plugins.keySet();
    }

    private List<String> getPluginNames() throws GladosException {
        List<String> pluginNames = null;

        String names = config.getProperty("glados.plugin.names");
        if (names != null) {
            pluginNames = Arrays.asList(names.split("\\s*,\\s*"));
        } else {
            throw new GladosException("Glados initialisation exception : no plugins found.");
        }

        return pluginNames;
    }

}
