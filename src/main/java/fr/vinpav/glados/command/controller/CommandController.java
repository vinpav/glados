package fr.vinpav.glados.command.controller;

import fr.vinpav.glados.exception.GladosException;
import fr.vinpav.glados.plugin.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandController {
    protected static final Logger logger = LoggerFactory.getLogger(CommandController.class);

    public void execute(String command){
        switch (command) {
            case "/help": {
                System.out.println(getCommandList());
                break;
            }
            case "/available": {
                try {
                    System.out.println("[Glados] > Here's the available plugins list :\n" + PluginManager.getInstance().getPluginNames());
                } catch (GladosException e) {
                    logger.error("Couldn't load plugin names list", e);
                }
                break;
            }
            case "/running": {
                System.out.println("[Glados] > Here's the running plugins list :\n" + PluginManager.getInstance().getPluginList());
                break;
            }
            case "/exit": {
                System.out.println("[Glados] > Shutting down...");
                PluginManager.getInstance().shutdownAll();
                System.exit(0);
            }
            case "/startall": {
                PluginManager.getInstance().startAll();
                break;
            }
            case "/stopall": {
                PluginManager.getInstance().shutdownAll();
                break;
            }

            default: {
                System.out.println("[Glados] > I don't know what you're talking about.");
            }
        }
    }

    public String getCommandList() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[Glados] > You required the command list ? Well...\n");
        stringBuilder.append("/available  : list available plugins.\n");
        stringBuilder.append("/exit       : Shutdown. (You shouldn't do that.)\n");
        stringBuilder.append("/help       : I know you know this one...\n");
        stringBuilder.append("/running    : list running plugins.\n");
        stringBuilder.append("/startall   : start all plugins.\n");
        stringBuilder.append("/stopall    : stop all plugins.\n");

        return stringBuilder.toString();
    }
}
