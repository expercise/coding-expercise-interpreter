package com.expercise.interpreter.core;

import com.expercise.interpreter.core.model.ChallengeEvaluationContext;
import com.expercise.interpreter.core.model.TestCaseResult;
import com.expercise.interpreter.core.model.TestCaseWithResult;
import com.expercise.interpreter.core.model.challenge.Challenge;
import com.expercise.interpreter.core.model.challenge.DataType;
import com.expercise.interpreter.core.model.challenge.TestCase;
import com.expercise.interpreter.testutils.FileTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TypeChecker.class})
public abstract class BaseInterpreterTest {

    protected abstract Interpreter getInterpreter();

    protected abstract String getFilePath(String fileName);

    @Before
    public void init() {
        mockStatic(TypeChecker.class);
        when(TypeChecker.check(any(), any(DataType.class))).thenReturn(true);
    }

    /*
     * Common test for each interpreter
     */
    @Test
    public void shouldFailWithSyntaxErrorWhenCompilationFailed() {
        Challenge challenge = new Challenge(Arrays.asList(DataType.Integer, DataType.Integer), DataType.Integer);
        TestCase testCase1 = new TestCase(Arrays.asList("12", "23"), "35");
        TestCase testCase2 = new TestCase(Arrays.asList("-15", "20"), "5");
        challenge.addTestCases(testCase1, testCase2);

        ChallengeEvaluationContext context = createContext(challenge, getSolutionFromFile("syntaxError"));

        getInterpreter().interpret(context);

        assertFalse(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), equalTo(InterpreterFailureType.SYNTAX_ERROR));

        assertThat(context.getTestCaseWithResults().size(), equalTo(2));
        assertThat(context.getTestCaseWithResults().get(0).getActualValue(), nullValue());
        assertThat(context.getTestCaseWithResults().get(0).getTestCaseResult(), equalTo(TestCaseResult.NEW));
        assertThat(context.getTestCaseWithResults().get(1).getActualValue(), nullValue());
        assertThat(context.getTestCaseWithResults().get(1).getTestCaseResult(), equalTo(TestCaseResult.NEW));
    }

    @Test
    public void shouldEvaluateSumOfIntegersChallengeSolution() {
        Challenge challenge = new Challenge(Arrays.asList(DataType.Integer, DataType.Integer), DataType.Integer);
        TestCase testCase1 = new TestCase(Arrays.asList("12", "23"), "35");
        TestCase testCase2 = new TestCase(Arrays.asList("-15", "20"), "5");
        challenge.addTestCases(testCase1, testCase2);

        ChallengeEvaluationContext context = createContext(challenge, getSolutionFromFile("sumOfIntegers"));

        getInterpreter().interpret(context);

        assertTrue(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), nullValue());

        assertThat(context.getTestCaseWithResults().size(), equalTo(2));
        assertThat(context.getTestCaseWithResults().get(0).getActualValue(), equalTo("35"));
        assertThat(context.getTestCaseWithResults().get(0).getTestCaseResult(), equalTo(TestCaseResult.PASSED));
        assertThat(context.getTestCaseWithResults().get(1).getActualValue(), equalTo("5"));
        assertThat(context.getTestCaseWithResults().get(1).getTestCaseResult(), equalTo(TestCaseResult.PASSED));
    }

    @Test
    public void shouldEvaluateConcatenationOfStringParamsChallengeSolution() {
        Challenge challenge = new Challenge(Arrays.asList(DataType.Text, DataType.Text), DataType.Text);
        TestCase testCase = new TestCase(Arrays.asList("Abc", "Def"), "AbcDef");
        challenge.addTestCases(testCase);

        ChallengeEvaluationContext context = createContext(challenge, getSolutionFromFile("textConcatenation"));

        getInterpreter().interpret(context);

        assertTrue(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), nullValue());

        assertThat(context.getTestCaseWithResults().size(), equalTo(1));
        assertThat(context.getTestCaseWithResults().get(0).getActualValue(), equalTo("AbcDef"));
        assertThat(context.getTestCaseWithResults().get(0).getTestCaseResult(), equalTo(TestCaseResult.PASSED));
    }

    @Test
    public void shouldFailWhenAllTestCasesFailed() {
        Challenge challenge = new Challenge(Arrays.asList(DataType.Integer, DataType.Integer), DataType.Integer);
        TestCase testCase1 = new TestCase(Arrays.asList("1", "2"), "2");
        TestCase testCase2 = new TestCase(Arrays.asList("5", "5"), "25");
        challenge.addTestCases(testCase1, testCase2);

        ChallengeEvaluationContext context = createContext(challenge, getSolutionFromFile("sumOfIntegers"));

        getInterpreter().interpret(context);

        assertFalse(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), nullValue());

        assertThat(context.getTestCaseWithResults().size(), equalTo(2));
        assertThat(context.getTestCaseWithResults().get(0).getActualValue(), equalTo("3"));
        assertThat(context.getTestCaseWithResults().get(0).getTestCaseResult(), equalTo(TestCaseResult.FAILED));
        assertThat(context.getTestCaseWithResults().get(1).getActualValue(), equalTo("10"));
        assertThat(context.getTestCaseWithResults().get(1).getTestCaseResult(), equalTo(TestCaseResult.FAILED));
    }

    @Test
    public void shouldFailWhenSomeOfTestCasesFailed() {
        Challenge challenge = new Challenge(Arrays.asList(DataType.Integer, DataType.Integer), DataType.Integer);
        TestCase testCase1 = new TestCase(Arrays.asList("0", "1"), "1");
        TestCase testCase2 = new TestCase(Arrays.asList("1", "3"), "13");
        challenge.addTestCases(testCase1, testCase2);

        ChallengeEvaluationContext context = createContext(challenge, getSolutionFromFile("sumOfIntegers"));

        getInterpreter().interpret(context);

        assertFalse(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), nullValue());

        assertThat(context.getTestCaseWithResults().size(), equalTo(2));
        assertThat(context.getTestCaseWithResults().get(0).getActualValue(), equalTo("1"));
        assertThat(context.getTestCaseWithResults().get(0).getTestCaseResult(), equalTo(TestCaseResult.PASSED));
        assertThat(context.getTestCaseWithResults().get(1).getActualValue(), equalTo("4"));
        assertThat(context.getTestCaseWithResults().get(1).getTestCaseResult(), equalTo(TestCaseResult.FAILED));
    }

    @Test
    public void shouldFailWithNoResultWhenSolutionHasNoResultValue() {
        Challenge challenge = new Challenge(Arrays.asList(DataType.Integer), DataType.Integer);
        TestCase testCase = new TestCase(Arrays.asList("1"), "1");
        challenge.addTestCases(testCase);

        ChallengeEvaluationContext context = createContext(challenge, getSolutionFromFile("noResult"));

        getInterpreter().interpret(context);

        assertFalse(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), equalTo(InterpreterFailureType.NO_RESULT));

        assertThat(context.getTestCaseWithResults().size(), equalTo(1));
        assertThat(context.getTestCaseWithResults().get(0).getActualValue(), nullValue());
        assertThat(context.getTestCaseWithResults().get(0).getTestCaseResult(), equalTo(TestCaseResult.NEW));
    }

    @Test
    public void shouldFailWithNoResultWhenNoSolutionMethodFound() {
        Challenge challenge = new Challenge(Arrays.asList(DataType.Integer), DataType.Integer);
        TestCase testCase = new TestCase(Arrays.asList("1"), "1");
        challenge.addTestCases(testCase);

        ChallengeEvaluationContext context = createContext(challenge, getSolutionFromFile("noSolutionMethod"));

        getInterpreter().interpret(context);

        assertFalse(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), equalTo(InterpreterFailureType.NO_RESULT));

        assertThat(context.getTestCaseWithResults().size(), equalTo(1));
        assertThat(context.getTestCaseWithResults().get(0).getActualValue(), nullValue());
        assertThat(context.getTestCaseWithResults().get(0).getTestCaseResult(), equalTo(TestCaseResult.NEW));
    }

    @Test
    public void shouldFailWithTypeErrorWhenExpectedIntegerButReturnedText() {
        Challenge challenge = new Challenge(Arrays.asList(DataType.Integer), DataType.Integer);
        TestCase testCase = new TestCase(Arrays.asList("1"), "1");
        challenge.addTestCases(testCase);

        ChallengeEvaluationContext context = createContext(challenge, getSolutionFromFile("typeMismatchIntegerAndText"));

        when(TypeChecker.check("1", DataType.Integer)).thenReturn(false);

        getInterpreter().interpret(context);

        assertFalse(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), equalTo(InterpreterFailureType.TYPE_ERROR));

        assertThat(context.getTestCaseWithResults().size(), equalTo(1));
        assertThat(context.getTestCaseWithResults().get(0).getActualValue(), nullValue());
        assertThat(context.getTestCaseWithResults().get(0).getTestCaseResult(), equalTo(TestCaseResult.NEW));
    }

    /*
     * Helper methods
     */
    protected ChallengeEvaluationContext createContext(Challenge challenge, String source) {
        ChallengeEvaluationContext context = new ChallengeEvaluationContext();
        context.setChallenge(challenge);
        context.setSolution(source);
        for (TestCase testCase : challenge.getTestCases()) {
            context.addTestCaseWithResult(new TestCaseWithResult(testCase));
        }
        return context;
    }

    protected String getSolutionFromFile(String fileName) {
        return FileTestUtils.getFileContentFrom(getFilePath(fileName));
    }

}
