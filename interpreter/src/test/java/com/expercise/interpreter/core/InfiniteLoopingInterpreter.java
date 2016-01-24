package com.expercise.interpreter.core;

import com.expercise.interpreter.core.model.ChallengeEvaluationContext;

public class InfiniteLoopingInterpreter extends Interpreter {

    @Override
    protected void interpretInternal(ChallengeEvaluationContext context) throws InterpreterException {
        while (true) {
            // Infinite loop
        }
    }

}
