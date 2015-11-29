package com.expercise.interpreter.core.javascript;

import com.expercise.interpreter.core.InterpreterFailureType;
import com.expercise.interpreter.core.InterpreterResult;
import com.expercise.interpreter.core.TypeChecker;
import com.expercise.interpreter.core.model.ChallengeEvaluationContext;
import com.expercise.interpreter.core.model.TestCaseResult;
import com.expercise.interpreter.core.model.TestCaseWithResult;
import com.expercise.interpreter.core.model.challenge.Challenge;
import com.expercise.interpreter.core.model.challenge.DataType;
import com.expercise.interpreter.core.model.challenge.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TypeChecker.class})
public class JavaScriptInterpreterTest {

    @InjectMocks
    private JavaScriptInterpreter interpreter;

    @Before
    public void init() {
        mockStatic(TypeChecker.class);
        when(TypeChecker.check(any(), any(DataType.class))).thenReturn(true);
    }

    @Test
    public void shouldEvaluateSolutionWithTestCases() {
        String solution = "function solution(a, b) { return a+b; }";

        Challenge challenge = new Challenge();

        challenge.setInputTypes(Arrays.asList(DataType.Integer, DataType.Integer));
        challenge.setOutputType(DataType.Integer);

        TestCase testCase = new TestCase();
        testCase.setInputs(Arrays.asList("12", "23"));
        testCase.setOutput("35");

        challenge.getTestCases().add(testCase);

        ChallengeEvaluationContext context = new ChallengeEvaluationContext();
        context.setChallenge(challenge);
        context.setSolution(solution);
        TestCaseWithResult successfulTestCase = new TestCaseWithResult(testCase);
        successfulTestCase.setActualValue("35");
        successfulTestCase.setTestCaseResult(TestCaseResult.PASSED);
        context.addTestCaseWithResult(successfulTestCase);

        interpreter.interpret(context);

        assertTrue(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), nullValue());
    }

    @Test
    public void shouldFailedResultIfTestCasesNotPassed() {
        String solution = "function solution(a, b) { return a; }";

        Challenge challenge = new Challenge();

        challenge.setInputTypes(Arrays.asList(DataType.Integer, DataType.Integer));
        challenge.setOutputType(DataType.Integer);

        TestCase testCase = new TestCase();
        testCase.setInputs(Arrays.asList("12", "23"));
        testCase.setOutput("35");

        challenge.getTestCases().add(testCase);

        ChallengeEvaluationContext context = createContext(challenge, solution);

        interpreter.interpret(context);

        InterpreterResult interpreterResult = context.getInterpreterResult();
        assertFalse(interpreterResult.isSuccess());
        assertThat(interpreterResult.getFailureType(), nullValue());

        List<TestCaseWithResult> testCaseWithResults = context.getTestCaseWithResults();
        assertThat(testCaseWithResults.size(), equalTo(1));
        TestCaseWithResult firstTestCaseWithResult = testCaseWithResults.get(0);
        assertThat(firstTestCaseWithResult.getTestCase().getOutput(), equalTo("35"));
        assertThat(firstTestCaseWithResult.getActualValue(), equalTo("12"));
    }

    @Test
    public void shouldFailForBothTwoTestCases() {
        String sumSolution = "function solution(a, b) { return a; }";

        Challenge challenge = new Challenge();

        challenge.setInputTypes(Arrays.asList(DataType.Integer, DataType.Integer));
        challenge.setOutputType(DataType.Integer);

        TestCase testCase1 = new TestCase();
        testCase1.setInputs(Arrays.asList("12", "23"));
        testCase1.setOutput("35");

        TestCase testCase2 = new TestCase();
        testCase2.setInputs(Arrays.asList("11", "21"));
        testCase2.setOutput("32");

        challenge.getTestCases().add(testCase1);
        challenge.getTestCases().add(testCase2);

        ChallengeEvaluationContext context = createContext(challenge, sumSolution);

        interpreter.interpret(context);

        InterpreterResult interpreterResult = context.getInterpreterResult();
        assertFalse(interpreterResult.isSuccess());
        assertThat(interpreterResult.getFailureType(), nullValue());

        List<TestCaseWithResult> testCaseWithResults = context.getTestCaseWithResults();
        assertThat(testCaseWithResults.size(), equalTo(2));
        TestCaseWithResult firstTestCaseWithResult = testCaseWithResults.get(0);
        assertThat(firstTestCaseWithResult.getTestCase().getOutput(), equalTo("35"));
        assertThat(firstTestCaseWithResult.getActualValue(), equalTo("12"));

        TestCaseWithResult secondTestCaseWithResult = testCaseWithResults.get(1);
        assertThat(secondTestCaseWithResult.getTestCase().getOutput(), equalTo("32"));
        assertThat(secondTestCaseWithResult.getActualValue(), equalTo("11"));
    }

    @Test
    public void shouldReturnFailedResultIfSolutionHasNoResult() {
        String solution = "function solution(a, b) {}";

        Challenge challenge = new Challenge();

        challenge.setInputTypes(Arrays.asList(DataType.Integer, DataType.Integer));
        challenge.setOutputType(DataType.Integer);

        TestCase testCase = new TestCase();
        testCase.setInputs(Arrays.asList("12", "23"));
        testCase.setOutput("35");

        challenge.getTestCases().add(testCase);

        ChallengeEvaluationContext context = createContext(challenge, solution);

        interpreter.interpret(context);

        assertFalse(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), equalTo(InterpreterFailureType.NO_RESULT));
    }

    @Test
    public void shouldWorkWithTextParameters() {
        String solution = "function solution(a) { return a.length; }";

        Challenge challenge = new Challenge();

        challenge.setInputTypes(Collections.singletonList(DataType.Text));
        challenge.setOutputType(DataType.Integer);

        TestCase testCase = new TestCase();

        testCase.setInputs(Collections.singletonList("abc"));
        testCase.setOutput("3");

        challenge.getTestCases().add(testCase);

        ChallengeEvaluationContext context = createContext(challenge, solution);

        interpreter.interpret(context);

        assertTrue(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), nullValue());
    }

    @Test
    public void shouldReturnTextValue() {
        String solution = "function solution(a) { return 'Hello World' }";

        Challenge challenge = new Challenge();

        challenge.setOutputType(DataType.Text);

        TestCase testCase = new TestCase();
        testCase.setOutput("Hello World");

        challenge.getTestCases().add(testCase);

        ChallengeEvaluationContext context = createContext(challenge, solution);

        interpreter.interpret(context);

        assertTrue(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), nullValue());
    }

    @Test
    public void shouldNotAllowJavaUsageInJavaScript() {
        String solution = "function solution(a) { return Java.type(\"java.lang.Math\").pow(a, 2); }";

        Challenge challenge = new Challenge();
        challenge.setInputTypes(Collections.singletonList(DataType.Integer));
        challenge.setOutputType(DataType.Integer);

        TestCase testCase = new TestCase();
        testCase.setInputs(Collections.singletonList("2"));
        testCase.setOutput("4");

        challenge.getTestCases().add(testCase);

        ChallengeEvaluationContext context = createContext(challenge, solution);

        interpreter.interpret(context);

        assertFalse(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), nullValue());
    }

    private ChallengeEvaluationContext createContext(Challenge challenge, String source) {
        ChallengeEvaluationContext context = new ChallengeEvaluationContext();
        context.setChallenge(challenge);
        context.setSolution(source);
        for (TestCase testCase : challenge.getTestCases()) {
            context.addTestCaseWithResult(new TestCaseWithResult(testCase));
        }
        return context;
    }

}
