package com.expercise.interpreter.core.javascript;

import com.expercise.interpreter.core.Interpreter;
import com.expercise.interpreter.core.InterpreterException;
import com.expercise.interpreter.core.InterpreterResult;
import com.expercise.interpreter.core.TypeChecker;
import com.expercise.interpreter.core.model.ChallengeEvaluationContext;
import com.expercise.interpreter.core.model.TestCaseResult;
import com.expercise.interpreter.core.model.TestCaseWithResult;
import com.expercise.interpreter.core.model.challenge.Challenge;
import com.expercise.interpreter.core.model.challenge.DataType;
import com.expercise.interpreter.core.model.challenge.TestCase;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class JavaScriptInterpreter extends Interpreter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaScriptInterpreter.class);

    @Override
    protected void interpretInternal(ChallengeEvaluationContext context) throws InterpreterException {
        ScriptEngine javaScriptEngine = getScriptEngine();

        evaluateSourceCode(context.getSolution(), javaScriptEngine);

        Challenge challenge = context.getChallenge();

        for (TestCaseWithResult eachTestCaseWithResult : context.getTestCaseWithResults()) {
            TestCase eachTestCase = eachTestCaseWithResult.getTestCase();
            Object resultValue = makeFunctionCallAndGetResultValue(javaScriptEngine, challenge, eachTestCase);
            typeCheck(resultValue, challenge.getOutputType());
            processTestCase(eachTestCaseWithResult, resultValue, challenge.getOutputType());
        }

        context.decideInterpreterResult();
    }

    private void typeCheck(Object resultValue, DataType outputType) throws InterpreterException {
        if (!TypeChecker.check(resultValue, outputType)) {
            throw new InterpreterException(InterpreterResult.typeErrorFailedResult());
        }
    }

    private ScriptEngine getScriptEngine() {
        NashornScriptEngine javaScriptEngine = (NashornScriptEngine) new NashornScriptEngineFactory().getScriptEngine("-strict", "--no-java", "--no-syntax-extensions");
        javaScriptEngine.put(ScriptEngine.FILENAME, "solution.js");
        return javaScriptEngine;
    }

    private void evaluateSourceCode(String sourceCode, ScriptEngine javaScriptEngine) throws InterpreterException {
        try {
            javaScriptEngine.eval(sourceCode);
        } catch (ScriptException e) {
            LOGGER.debug("Exception while evaluation", e);
            throw new InterpreterException(InterpreterResult.syntaxErrorFailedResult());
        }
    }

    private Object makeFunctionCallAndGetResultValue(ScriptEngine javaScriptEngine, Challenge challenge, TestCase testCase) throws InterpreterException {
        Object evaluationResult;

        try {
            Object[] convertedInputValues = challenge.getConvertedInputValues(testCase.getInputs()).toArray();
            evaluationResult = ((Invocable) javaScriptEngine).invokeFunction("solution", convertedInputValues);
        } catch (Exception e) {
            LOGGER.debug("Exception while interpreting", e);
            throw new InterpreterException(InterpreterResult.createFailedResult());
        }

        if (evaluationResult == null) {
            throw new InterpreterException(InterpreterResult.noResultFailedResult());
        }

        return evaluationResult;
    }

    private void processTestCase(TestCaseWithResult testCaseWithResult, Object resultValue, DataType outputType) {
        TestCaseResult testCaseResult = TestCaseResult.FAILED;

        if (outputType == DataType.Integer) {
            int evaluationResultAsInteger = ((Number) resultValue).intValue();
            int expectedValue = ((Number) Double.parseDouble(testCaseWithResult.getTestCase().getOutput())).intValue();
            testCaseResult = evaluationResultAsInteger == expectedValue ? TestCaseResult.PASSED : TestCaseResult.FAILED;
            testCaseWithResult.setActualValue(String.valueOf(evaluationResultAsInteger));
        } else if (outputType == DataType.Text) {
            String evaluationResultAsString = resultValue.toString();
            String expectedValue = testCaseWithResult.getTestCase().getOutput();
            testCaseResult = evaluationResultAsString.equals(expectedValue) ? TestCaseResult.PASSED : TestCaseResult.FAILED;
            testCaseWithResult.setActualValue(evaluationResultAsString);
        }

        testCaseWithResult.setTestCaseResult(testCaseResult);
    }

}
