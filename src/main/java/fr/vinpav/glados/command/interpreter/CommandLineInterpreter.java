package fr.vinpav.glados.command.interpreter;

import fr.vinpav.glados.command.controller.CommandController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CommandLineInterpreter implements Interpreter {
    protected static final Logger logger = LoggerFactory.getLogger(CommandLineInterpreter.class);

    Scanner scanner;
    CommandController controller;

    public CommandLineInterpreter() {
        scanner = new Scanner(System.in);
        scanner.useDelimiter(";");
        controller = new CommandController();
    }

    public void listen() {
        for (;;) {
            parseCommandLine();
        }
    }

    public void parseCommandLine() {
        String command = scanner.next().trim();


        if (!command.isEmpty()) {
            List words = Arrays.asList(command.split("([\\W\\s]+)"));
            controller.execute(words);
        }
    }
}
