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
    }

    public void startAll() {
        try {
            for (String pluginName : getPluginNames()) {
                try {
                    register(pluginName);
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    logger.warn("Unable to register plugin " + pluginName);
                    e.printStackTrace();
                }
            }
        } catch (GladosException e) {
            logger.error("Plugins startup failed " + e.getMessage());
        }
    }

    public void shutdownAll() {
        try {
            for (String pluginName : getPluginNames()) {
                try {
                    unregister(pluginName);
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    logger.warn("Unable to unregister plugin " + pluginName);
                    e.printStackTrace();
                }
            }
        } catch (GladosException e) {
            logger.error("Plugins shutdown failed " + e.getMessage());
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
            pluginInstance.play();
            plugins.put(pluginName, pluginInstance);
            logger.info(pluginInstance.describe() + " is online.");
        } catch (GladosException e) {
            logger.error("Warning, plugin " + pluginName + " not loaded : " + e.getMessage());
        }
    }

    private void unregister(String pluginName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.info("Unregistering plugin : " + pluginName + "...");
        Plugin plugin = (Plugin)plugins.get(pluginName);
        if (plugin != null) {
            plugin.shutdown();
        }
        plugins.remove(pluginName);
        logger.info(pluginName + " removed.");
    }

    public StringBuilder getRegisteredPlugins() {
        StringBuilder result = new StringBuilder();
        Iterator iterate = getPluginList().iterator();

        int i = 0;
        while (iterate.hasNext()) {
            result.append("[Glados] > " + i + " : " + iterate.next() + "\n");
            i++;
        }

        return result;
    }

    public Set getPluginList() {
        return plugins.keySet();
    }

    public List<String> getPluginNames() throws GladosException {
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
