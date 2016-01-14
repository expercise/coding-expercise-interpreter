package com.expercise.interpreter.core.python;

import com.expercise.interpreter.core.InterpreterFailureType;
import com.expercise.interpreter.core.TypeChecker;
import com.expercise.interpreter.core.model.ChallengeEvaluationContext;
import com.expercise.interpreter.core.model.challenge.Challenge;
import com.expercise.interpreter.core.model.challenge.DataType;
import com.expercise.interpreter.core.model.challenge.TestCase;
import com.expercise.interpreter.testutils.FileTestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TypeChecker.class})
public class PythonInterpreterTest {

    @InjectMocks
    private PythonInterpreter interpreter;

    @Test
    public void shouldSendErrorMessageWhenThereIsSyntaxError() {
        Challenge challenge = new Challenge();

        ChallengeEvaluationContext context = createContext(challenge, "def foo(a, b): Keturn a -+ b;");

        interpreter.interpret(context);

        assertFalse(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), equalTo(InterpreterFailureType.SYNTAX_ERROR));
    }

    @Test
    public void shouldEvaluatePythonCodeWithSingleTestCaseForSuccessfulCase() {
        String sumSolution = FileTestUtils.getFileContentFrom("/python/simplePlus.py");

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
    public void shouldEvaluatePythonCodeFromImportedModule() {
        String sumSolution = FileTestUtils.getFileContentFrom("/python/simpleImport.py");

        Challenge challenge = new Challenge();

        List<DataType> inputTypes = new ArrayList<>();
        inputTypes.add(DataType.Integer);

        challenge.setInputTypes(inputTypes);
        challenge.setOutputType(DataType.Integer);

        TestCase testCase = new TestCase();

        List<String> inputValues = new ArrayList<>();
        inputValues.add("11");
        testCase.setInputs(inputValues);
        testCase.setOutput("121");

        challenge.getTestCases().add(testCase);

        ChallengeEvaluationContext context = createContext(challenge, sumSolution);

        interpreter.interpret(context);

        assertTrue(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), nullValue());
    }

    @Test
    public void shouldEvaluateMultipleTestCaseWithPythonSourceCodeForSuccessfulCase() {
        String sumSolution = FileTestUtils.getFileContentFrom("/python/simplePlus.py");

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
        inputValues2.add("120");
        inputValues2.add("23");
        testCase2.setInputs(inputValues2);
        testCase2.setOutput("143");

        challenge.getTestCases().add(testCase1);
        challenge.getTestCases().add(testCase2);

        ChallengeEvaluationContext context = createContext(challenge, sumSolution);

        interpreter.interpret(context);

        assertTrue(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), nullValue());
    }

    @Test
    public void shouldEvaluateCodeForConcatenationCodeOfStringInputs() {
        String concatSolution = FileTestUtils.getFileContentFrom("/python/simplePlus.py");

        Challenge challenge = new Challenge();

        List<DataType> inputTypes = new ArrayList<>();
        inputTypes.add(DataType.Text);
        inputTypes.add(DataType.Text);

        challenge.setInputTypes(inputTypes);
        challenge.setOutputType(DataType.Text);

        TestCase testCase = new TestCase();

        List<String> inputValues = new ArrayList<>();
        inputValues.add("ahmet");
        inputValues.add("mehmet");
        testCase.setInputs(inputValues);
        testCase.setOutput("ahmetmehmet");

        challenge.getTestCases().add(testCase);

        ChallengeEvaluationContext context = createContext(challenge, concatSolution);

        interpreter.interpret(context);

        assertTrue(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), nullValue());
    }

    @Test
    public void shouldReturnExceptionMessageIfEvaluationCauseAnException() {
        String concatSolution = "def solution(a, b): return a+BB";

        Challenge challenge = new Challenge();

        List<DataType> inputTypes = new ArrayList<>();
        inputTypes.add(DataType.Text);
        inputTypes.add(DataType.Text);

        challenge.setInputTypes(inputTypes);
        challenge.setOutputType(DataType.Text);

        TestCase testCase = new TestCase();

        List<String> inputValues = new ArrayList<>();
        inputValues.add("ahmet");
        inputValues.add("mehmet");
        testCase.setInputs(inputValues);
        testCase.setOutput("ahmetmehmet");

        challenge.getTestCases().add(testCase);

        ChallengeEvaluationContext context = createContext(challenge, concatSolution);

        interpreter.interpret(context);

        assertFalse(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), nullValue());
    }

    @Test
    public void shouldWorkWithTextParameters() {
        String solution = "def solution(a): return len(a)";

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

        ChallengeEvaluationContext context = createContext(challenge, solution);

        interpreter.interpret(context);

        assertTrue(context.getInterpreterResult().isSuccess());
        assertThat(context.getInterpreterResult().getFailureType(), nullValue());
    }

    @Test
    public void shouldReturnTextValue() {
        String solution = "def solution(): return \"Hello World\"";

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
    public void shouldFailedIfReturnValueTypeAndOutputTypeDoesNotMatch() {
        String solution = "def solution(): return \"Text, not Integer\"";

        Challenge challenge = new Challenge();

        challenge.setOutputType(DataType.Integer);

        TestCase testCase = new TestCase();
        testCase.setOutput("1");

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
        return context;
    }

}