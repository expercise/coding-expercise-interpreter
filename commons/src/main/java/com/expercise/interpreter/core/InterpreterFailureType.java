package com.expercise.interpreter.core;

public enum InterpreterFailureType {

    NO_RESULT("interpreter.failureTypes.noResult"),
    SYNTAX_ERROR("interpreter.failureTypes.syntaxError"),
    TYPE_ERROR("interpreter.failureTypes.typeError");

    private String messageKey;

    InterpreterFailureType(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }

}
