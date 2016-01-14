package com.expercise.interpreter.core.java;

import com.expercise.interpreter.core.InterpreterFailureType;
import com.expercise.interpreter.core.model.ChallengeEvaluationContext;
import com.expercise.interpreter.core.model.challenge.Challenge;
import com.expercise.interpreter.core.model.challenge.DataType;
import com.expercise.interpreter.core.model.challenge.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class JavaInterpreterTest {

    @InjectMocks
    private JavaInterpreter interpreter;

    @Test
    public void shouldEvaluateSolutionWithTestCases() {
        String sumSolution = "public class Solution { public int solution(Integer a, Integer b) { return a + b; } }";

        Challenge challenge = new Challenge();

        List<DataType> inputTypes = new ArrayList<>();
        inputTypes.add(DataType.Integer);
        inputTypes.add(DataType.Integer);

        challenge.setInputTypes(inputTypes);
        challenge.setOutputType(DataType.Integer);

        TestCase testCase = new TestCase();

        List<String> inputValues = new ArrayList<>();
        inputValues.add("12");
        inputValues.add("23");
        testCase.setInputs(inputValues);
        testCase.setOutput("35");

        challenge.getTestCases().add(testCase);

        ChallengeEvaluationContext context = createContext(challenge, sumSolution);

        interpreter.interpret(context);

        assertTrue(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), nullValue());
    }

    @Test
    public void shouldEvaluateSolutionWithMultipleTestCases() {
        String sumSolution = "public class Solution { public int solution(Integer a, Integer b) { return a + b; } }";

        Challenge challenge = new Challenge();

        List<DataType> inputTypes = new ArrayList<>();
        inputTypes.add(DataType.Integer);
        inputTypes.add(DataType.Integer);

        challenge.setInputTypes(inputTypes);
        challenge.setOutputType(DataType.Integer);

        TestCase testCase1 = new TestCase();
        List<String> inputValues1 = new ArrayList<>();
        inputValues1.add("12");
        inputValues1.add("23");
        testCase1.setInputs(inputValues1);
        testCase1.setOutput("35");

        TestCase testCase2 = new TestCase();
        List<String> inputValues2 = new ArrayList<>();
        inputValues2.add("0");
        inputValues2.add("1");
        testCase2.setInputs(inputValues2);
        testCase2.setOutput("1");

        challenge.getTestCases().add(testCase1);
        challenge.getTestCases().add(testCase2);

        ChallengeEvaluationContext context = createContext(challenge, sumSolution);

        interpreter.interpret(context);

        assertTrue(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), nullValue());
    }

    @Test
    public void shouldFailedResultIfTestCasesNotPassed() {
        String sumSolution = "public class Solution { public int solution(Integer a, Integer b) { return a; } }";

        Challenge challenge = new Challenge();

        List<DataType> inputTypes = new ArrayList<>();
        inputTypes.add(DataType.Integer);
        inputTypes.add(DataType.Integer);

        challenge.setInputTypes(inputTypes);
        challenge.setOutputType(DataType.Integer);

        TestCase testCase = new TestCase();

        List<String> inputValues = new ArrayList<>();
        inputValues.add("12");
        inputValues.add("23");
        testCase.setInputs(inputValues);
        testCase.setOutput("35");

        challenge.getTestCases().add(testCase);

        ChallengeEvaluationContext context = new ChallengeEvaluationContext();
        context.setChallenge(challenge);
        context.setSolution(sumSolution);

        interpreter.interpret(context);

        assertFalse(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), nullValue());
    }

    @Test
    public void shouldReturnFailedResultIfCompilationFails() {
        String solution = "public class Solution { public int solution(Integer a, Integer b) { /* missing return statement */ } }";

        Challenge challenge = new Challenge();

        List<DataType> inputTypes = new ArrayList<>();
        inputTypes.add(DataType.Integer);
        inputTypes.add(DataType.Integer);

        challenge.setInputTypes(inputTypes);
        challenge.setOutputType(DataType.Integer);

        TestCase testCase = new TestCase();

        List<String> inputValues = new ArrayList<>();
        inputValues.add("12");
        inputValues.add("23");
        testCase.setInputs(inputValues);
        testCase.setOutput("35");

        challenge.getTestCases().add(testCase);

        ChallengeEvaluationContext context = createContext(challenge, solution);

        interpreter.interpret(context);

        assertFalse(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), equalTo(InterpreterFailureType.SYNTAX_ERROR));
    }

    @Test
    public void shouldWorkWithTextParameters() {
        String sumSolution = "public class Solution { public int solution(String a) { return a.length(); } }";

        Challenge challenge = new Challenge();

        List<DataType> inputTypes = new ArrayList<>();
        inputTypes.add(DataType.Text);

        challenge.setInputTypes(inputTypes);
        challenge.setOutputType(DataType.Integer);

        TestCase testCase = new TestCase();

        List<String> inputValues = new ArrayList<>();
        inputValues.add("abc");
        testCase.setInputs(inputValues);
        testCase.setOutput("3");

        challenge.getTestCases().add(testCase);

        ChallengeEvaluationContext context = createContext(challenge, sumSolution);

        interpreter.interpret(context);

        assertTrue(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), nullValue());
    }

    @Test
    public void shouldReturnTextValue() {
        String solution = "public class Solution { public String solution(String a) { return \"Hello \" + a; } }";

        Challenge challenge = new Challenge();

        List<DataType> inputTypes = new ArrayList<>();
        inputTypes.add(DataType.Text);

        challenge.setInputTypes(inputTypes);

        challenge.setOutputType(DataType.Text);

        TestCase testCase = new TestCase();

        List<String> inputValues = new ArrayList<>();
        inputValues.add("World");
        testCase.setInputs(inputValues);
        testCase.setOutput("Hello World");

        challenge.getTestCases().add(testCase);

        ChallengeEvaluationContext context = createContext(challenge, solution);

        interpreter.interpret(context);

        assertTrue(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), nullValue());
    }

    private ChallengeEvaluationContext createContext(Challenge challenge, String solution) {
        ChallengeEvaluationContext context = new ChallengeEvaluationContext();
        context.setChallenge(challenge);
        context.setSolution(solution);
        return context;
    }

}