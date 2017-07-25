package fr.vinpav.glados;

import fr.vinpav.glados.config.Configuration;
import fr.vinpav.glados.exception.GladosException;
import fr.vinpav.glados.plugin.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class Glados {
    protected static final Logger logger = LoggerFactory.getLogger(Glados.class);

    private Configuration mainConfig;

    public Glados() {
        mainConfig = new Configuration();
    }

    public void start() {
        logger.info("Starting Glados...");
        try {
            mainConfig.load();
            PluginManager.getInstance().initialize(mainConfig);
        } catch (GladosException e) {
            logger.info("Fatal error during Glados initialisation. Terminating...");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void runPlugins() {
        PluginManager.getInstance().executeAll();
    }

    public StringBuilder getRegisteredPlugins() {
        StringBuilder result = new StringBuilder();
        Iterator iterate = PluginManager.getInstance().getPluginList().iterator();

        int i = 0;
        while (iterate.hasNext()) {
            result.append("[Glados] > " + i + " : " + iterate.next() + "\n");
            i++;
        }

        return result;
    }

    public static void main(String[] args) {
        Glados myGlados = new Glados();
        myGlados.start();
        logger.info("[Glados] > Initialization sequence complete.");
        logger.info("[Glados] > Here's the registered plugins list :\n" + myGlados.getRegisteredPlugins().toString());
        myGlados.runPlugins();
    }
}
