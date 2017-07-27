package fr.vinpav.glados.command.controller;

import fr.vinpav.glados.exception.GladosException;
import fr.vinpav.glados.plugin.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class GladosCommandController {
    protected static final Logger logger = LoggerFactory.getLogger(GladosCommandController.class);

    public void execute(List<String> command) {
        switch (command.size()) {
            case 1: {
                execute(command.get(0));
                break;
            }
            case 2: {
                execute(command.get(0), command.get(1));
                break;
            }
            case 3: {
                execute(command.get(0), command.get(1), command.get(2));
                break;
            }
            default: {
                logger.info("[Glados] > I don't know what you're talking about.");
            }
        }
    }

    private String getCommandList() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[Glados] > You required the command list ? Well...\n");
        stringBuilder.append("----------------------------------------------------\n");
        stringBuilder.append("exit;                  : Shutdown. (You shouldn't do that.)\n");
        stringBuilder.append("help;                  : I know you know this one...\n");
        stringBuilder.append("pluginlist;            : list plugins.\n");
        stringBuilder.append("startall;              : start all plugins.\n");
        stringBuilder.append("stopall;               : stop all plugins.\n");
        stringBuilder.append("----------------------------------------------------\n");
        stringBuilder.append("Type '[controller name] start/stop;' to start/stop a controller.\n");
        stringBuilder.append("Type '[controller name] help;' to see a controller specific commands.\n");

        return stringBuilder.toString();
    }

    /**
     * One argument : used for main Glados commands
     * @param order the command
     */
    private void execute(String order){
        switch (order) {
            case "help": {
                logger.info(getCommandList());
                break;
            }
            case "pluginlist": {
                logger.info("[Glados] > Here's the available plugins list :\n" + PluginManager.getInstance().getPluginList());
                break;
            }
            case "exit": {
                logger.info("[Glados] > Shutting down...");
                PluginManager.getInstance().shutdownAll();
                System.exit(0);
            }
            case "startall": {
                PluginManager.getInstance().startAll();
                break;
            }
            case "stopall": {
                PluginManager.getInstance().shutdownAll();
                break;
            }

            default: {
                logger.info("[Glados] > I don't know what you're talking about.");
            }
        }
    }

    /**
     * Two arguments : used for controller interaction and startup/shutdown
     * @param order start / stop or the controller name
     * @param object the controller name or a controller command
     */
    private void execute(String order, String object){
        execute(order, object, null);
    }

    /**
     * Three arguments : controller commands only
     * @param order the controller name
     * @param object the controller command to execute
     * @param option the command option, if applicable
     */
    private void execute(String order, String object, String option){
        try {
            if (!PluginManager.getInstance().getPluginNames().contains(order)) {
                logger.info("[Glados] > I've never heard about what you call '" + order + "'...");
            } else {
                PluginManager.getInstance().getController(order).execute(Arrays.asList(object,option));
            }
        } catch (GladosException e) {
            logger.error("[Glados] > Shit happens.", e);
        }
    }
}
