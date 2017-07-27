package fr.vinpav.glados.plugin;

import fr.vinpav.glados.command.controller.CommandController;
import fr.vinpav.glados.config.Configuration;
import fr.vinpav.glados.exception.GladosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class PluginManager {
    protected static final Logger logger = LoggerFactory.getLogger(PluginManager.class);

    private static PluginManager ourInstance = new PluginManager();

    private Configuration config;
    public Map<String,CommandController> controllers;

    public static PluginManager getInstance() {
        return ourInstance;
    }

    private PluginManager() {
    }

    public void initialize(Configuration config) throws GladosException {
        this.config = config;
        controllers = new HashMap<String, CommandController>();
        for (String pluginName : getPluginNames()) {
            try {
                register(pluginName);
            } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
                logger.error("[Glados] > Plugins initialization sequence failed.", e);
            }
        }
    }

    public void startAll() {
        try {
            for (String pluginName : getPluginNames()) {
                controllers.get(pluginName).execute(Arrays.asList("start"));
            }
        } catch (GladosException e) {
            logger.error("[Glados] > Plugins startup failed ", e);
        }
    }

    public void shutdownAll() {
        try {
            for (String pluginName : getPluginNames()) {
                if (controllers.get(pluginName) != null) {
                    controllers.get(pluginName).execute(Arrays.asList("stop"));
                }
            }
        } catch (GladosException e) {
            logger.error("[Glados] > Plugins shutdown failed " + e.getMessage());
        }
    }

    private void register(String pluginName) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        logger.info("[Glados] > Registering controller : " + pluginName + "...");
        Configuration pluginConfig = new Configuration(pluginName + ".config");
        try {
            pluginConfig.load();
            Class controllerClass = Class.forName(pluginConfig.getProperty("controller.class.name"));
            Constructor constructor = controllerClass.getConstructor(Configuration.class);
            CommandController pluginController = (CommandController)constructor.newInstance(pluginConfig);
            controllers.put(pluginName, pluginController);
            logger.info(pluginName + " is ready.");
        } catch (GladosException e) {
            logger.warn("[Glados] > Warning, controller " + pluginName + " not loaded : " + e.getMessage());
        }
    }

    public boolean isRunning(String pluginName) {
        return getPluginList().contains(pluginName);
    }

    public Set getPluginList() {
        return controllers.keySet();
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

    public CommandController getController(String pluginName) {
        return controllers.get(pluginName);
    }
}
