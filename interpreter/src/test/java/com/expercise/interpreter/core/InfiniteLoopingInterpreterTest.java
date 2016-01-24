package com.expercise.interpreter.core;

import com.expercise.interpreter.core.model.ChallengeEvaluationContext;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class InfiniteLoopingInterpreterTest {

    @Test
    public void shouldLimitInterpretationTime() throws InterpreterException {
        InfiniteLoopingInterpreter interpreter = new InfiniteLoopingInterpreter();

        ChallengeEvaluationContext context = new ChallengeEvaluationContext();

        interpreter.interpret(context);

        assertFalse(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), equalTo(InterpreterFailureType.NO_RESULT));
    }

}