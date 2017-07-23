package fr.vinpav.glados;

import fr.vinpav.glados.config.Configuration;
import fr.vinpav.glados.exception.GladosException;
import fr.vinpav.glados.plugin.PluginManager;

import java.util.Iterator;

public class Glados {

    private Configuration mainConfig;

    public Glados() {
        mainConfig = new Configuration();
    }

    public void start() {
        System.out.println("Starting Glados...");
        try {
            mainConfig.load();
            PluginManager.getInstance().initialize(mainConfig);
        } catch (GladosException e) {
            System.out.println("Fatal error during Glados initialisation. Terminating...");
            e.printStackTrace();
            System.exit(1);
        }
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
        System.out.println("[Glados] > Initialization sequence complete.");
        System.out.println("[Glados] > Here's the registered plugins list :");
        System.out.println(myGlados.getRegisteredPlugins());
    }
}
