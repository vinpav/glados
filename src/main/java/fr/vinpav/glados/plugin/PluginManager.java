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
    public Map<String,Plugin> plugins;

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
                startPlugin(pluginName);
            }
        } catch (GladosException e) {
            logger.error("[Glados] > Plugins startup failed " + e.getMessage());
        }
    }

    public void startPlugin(String pluginName) {
        try {
            register(pluginName);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            logger.warn("[Glados] > Unable to register plugin " + pluginName);
            e.printStackTrace();
        }
    }

    public void shutdownAll() {
        try {
            for (String pluginName : getPluginNames()) {
                stopPlugin(pluginName);
            }
        } catch (GladosException e) {
            logger.error("[Glados] > Plugins shutdown failed " + e.getMessage());
        }
    }

    public void stopPlugin(String pluginName) {
        try {
            unregister(pluginName);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            logger.warn("[Glados] > Unable to unregister plugin " + pluginName);
            e.printStackTrace();
        }
    }

    private void register(String pluginName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.info("[Glados] > Registering plugin : " + pluginName + "...");
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
            logger.warn("[Glados] > Warning, plugin " + pluginName + " not loaded : " + e.getMessage());
        }
    }

    private void unregister(String pluginName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.info("[Glados] > Unregistering plugin : " + pluginName + "...");
        Plugin plugin = getPlugin(pluginName);
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

    public boolean isRunning(String pluginName) {
        return getPluginList().contains(pluginName);
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

    public Plugin getPlugin(String pluginName) {
        return plugins.get(pluginName);
    }

}
