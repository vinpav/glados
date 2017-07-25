package fr.vinpav.glados.command.interpreter;

import fr.vinpav.glados.command.controller.CommandController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class CommandLineInterpreter implements Interpreter {
    protected static final Logger logger = LoggerFactory.getLogger(CommandLineInterpreter.class);

    Scanner scanner;
    CommandController controller;

    public CommandLineInterpreter() {
        scanner = new Scanner(System.in);
        controller = new CommandController();
    }

    public void listen() {
        for (;;) {
            parseCommandLine();
        }
    }

    public void parseCommandLine() {
        String command = scanner.next();
        if (!command.isEmpty()) {
            controller.execute(command);
        }
    }
}
