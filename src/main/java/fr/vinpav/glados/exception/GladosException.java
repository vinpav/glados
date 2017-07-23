package fr.vinpav.glados.exception;

public class GladosException extends Exception {

    public GladosException(String message) {
        super(message);
    }

    public GladosException(String message, Throwable cause) {
        super(message,cause);
    }
}
