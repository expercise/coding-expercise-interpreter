package com.expercise.interpreter.core;

import com.expercise.interpreter.core.model.ChallengeEvaluationContext;
import com.expercise.interpreter.core.model.challenge.DataType;

import java.util.concurrent.*;

public abstract class Interpreter {

    private static final int TIME = 10;

    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    protected abstract void interpretInternal(ChallengeEvaluationContext context) throws InterpreterException;

    public final void interpret(ChallengeEvaluationContext context) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future future = executor.submit(() -> {
            interpretInternal(context);
            return null;
        });

        try {
            future.get(TIME, TIME_UNIT);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof InterpreterException) {
                context.setInterpreterResult(((InterpreterException) cause).getInterpreterResult());
            } else {
                context.setInterpreterResult(InterpreterResult.noResultFailedResult());
            }
        } finally {
            executor.shutdownNow();
        }
    }

    protected void typeCheck(Object resultValue, DataType outputType) throws InterpreterException {
        if (!TypeChecker.check(resultValue, outputType)) {
            throw new InterpreterException(InterpreterResult.typeErrorFailedResult());
        }
    }

}
