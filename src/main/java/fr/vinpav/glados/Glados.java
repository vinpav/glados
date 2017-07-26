package fr.vinpav.glados;

import fr.vinpav.glados.config.Configuration;
import fr.vinpav.glados.exception.GladosException;
import fr.vinpav.glados.command.interpreter.Interpreter;
import fr.vinpav.glados.plugin.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class Glados {
    protected static final Logger logger = LoggerFactory.getLogger(Glados.class);

    private Configuration mainConfig;

    private Interpreter interpreter;

    public Glados() {
        mainConfig = new Configuration();
    }

    public void start() {
        logger.info("Starting Glados...");
        try {
            mainConfig.load();
            PluginManager.getInstance().initialize(mainConfig);
            initializeInterpreter();
        } catch (GladosException e) {
            logger.error("Fatal error during Glados initialisation. Terminating...");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void initializeInterpreter() throws GladosException {
        try {
            interpreter = (Interpreter)Class.forName(mainConfig.getProperty("glados.command.interpreter")).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new GladosException("Cannot initialize interpreter " + mainConfig.getProperty("glados.command.interpreter"));
        }
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }

    public static void main(String[] args) {
        Glados myGlados = new Glados();
        myGlados.start();
        logger.info("[Glados] > Initialization sequence complete.");
        logger.info("[Glados] > Type 'help;' if you're lost.");
        logger.info("[Glados] > So, tell me about your problems...");
        myGlados.getInterpreter().listen();
    }
}
