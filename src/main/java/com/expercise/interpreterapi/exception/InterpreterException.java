package com.expercise.interpreterapi.exception;

public class InterpreterException extends RuntimeException {

    private static final long serialVersionUID = -2755063919178374682L;

    public InterpreterException(String message) {
        super(message);
    }

    public InterpreterException(String message, Exception exception) {
        super(message, exception);
    }
}
