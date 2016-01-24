package com.expercise.interpreter.core.javascript;

import com.expercise.interpreter.core.BaseInterpreterTest;
import com.expercise.interpreter.core.Interpreter;
import com.expercise.interpreter.core.InterpreterFailureType;
import com.expercise.interpreter.core.model.ChallengeEvaluationContext;
import com.expercise.interpreter.core.model.TestCaseResult;
import com.expercise.interpreter.core.model.challenge.Challenge;
import com.expercise.interpreter.core.model.challenge.DataType;
import com.expercise.interpreter.core.model.challenge.TestCase;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class JavaScriptInterpreterTest extends BaseInterpreterTest {

    @InjectMocks
    private JavaScriptInterpreter interpreter;

    @Override
    protected Interpreter getInterpreter() {
        return interpreter;
    }

    @Override
    protected String getFilePath(String fileName) {
        return "/javascript/" + fileName;
    }

    @Test
    public void shouldNotAllowJavaUsageInJavaScript() {
        Challenge challenge = new Challenge(Collections.singletonList(DataType.Integer), DataType.Integer);
        TestCase testCase1 = new TestCase(Collections.singletonList("1"), "1");
        TestCase testCase2 = new TestCase(Collections.singletonList("2"), "4");
        challenge.addTestCases(testCase1, testCase2);

        ChallengeEvaluationContext context = createContext(challenge, getSolutionFromFile("javaNotAllowed"));

        interpreter.interpret(context);

        assertFalse(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), equalTo(InterpreterFailureType.NO_RESULT));

        assertThat(context.getTestCaseWithResults().size(), equalTo(2));
        assertThat(context.getTestCaseWithResults().get(0).getActualValue(), nullValue());
        assertThat(context.getTestCaseWithResults().get(0).getTestCaseResult(), equalTo(TestCaseResult.NEW));
        assertThat(context.getTestCaseWithResults().get(1).getActualValue(), nullValue());
        assertThat(context.getTestCaseWithResults().get(1).getTestCaseResult(), equalTo(TestCaseResult.NEW));
    }

}
