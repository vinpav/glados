package fr.vinpav.glados.plugin;

import fr.vinpav.glados.config.Configuration;
import fr.vinpav.glados.exception.GladosException;

import java.util.*;

public class PluginManager {
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

        for (String pluginName : config.getPluginNames()) {
            try {
                register(pluginName);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                System.out.println("Warning : Unable to register plugin " + pluginName);
                e.printStackTrace();
            }
        }
    }

    private void register(String pluginName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        System.out.println("Registering plugin : " + pluginName + "...");
        Configuration pluginConfig = new Configuration(pluginName + ".config");
        try {
            pluginConfig.load();
            Class pluginClass = Class.forName(pluginConfig.getProperty("plugin.class.name"));
            Plugin pluginInstance = (Plugin) pluginClass.newInstance();
            pluginInstance.setConfiguration(pluginConfig);
            plugins.put(pluginName, pluginInstance);
        } catch (GladosException e) {
            System.out.println("Warning, plugin " + pluginName + " not loaded : " + e.getMessage());
        }
        System.out.println("Complete.");
    }

    public Set getPluginList() {
        return plugins.keySet();
    }
}
