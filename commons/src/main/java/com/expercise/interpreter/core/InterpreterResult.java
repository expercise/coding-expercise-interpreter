package com.expercise.interpreter.core;

public class InterpreterResult {

    private InterpreterFailureType failureType;

    private boolean success;

    public InterpreterResult() {
    }

    private InterpreterResult(boolean success) {
        this.success = success;
    }

    public static InterpreterResult createSuccessResult() {
        return new InterpreterResult(true);
    }

    public static InterpreterResult createFailedResult() {
        return new InterpreterResult(false);
    }

    public static InterpreterResult noResultFailedResult() {
        InterpreterResult failedResult = createFailedResult();
        failedResult.setFailureType(InterpreterFailureType.NO_RESULT);
        return failedResult;
    }

    public static InterpreterResult syntaxErrorFailedResult() {
        InterpreterResult failedResult = createFailedResult();
        failedResult.setFailureType(InterpreterFailureType.SYNTAX_ERROR);
        return failedResult;
    }

    public static InterpreterResult typeErrorFailedResult() {
        InterpreterResult failedResult = createFailedResult();
        failedResult.setFailureType(InterpreterFailureType.TYPE_ERROR);
        return failedResult;
    }

    public InterpreterFailureType getFailureType() {
        return failureType;
    }

    public void setFailureType(InterpreterFailureType failureType) {
        this.failureType = failureType;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}