package com.expercise.interpreter.core.python;

import com.expercise.interpreter.core.Interpreter;
import com.expercise.interpreter.core.InterpreterException;
import com.expercise.interpreter.core.InterpreterResult;
import com.expercise.interpreter.core.model.ChallengeEvaluationContext;
import com.expercise.interpreter.core.model.TestCaseResult;
import com.expercise.interpreter.core.model.TestCaseWithResult;
import com.expercise.interpreter.core.model.challenge.Challenge;
import com.expercise.interpreter.core.model.challenge.DataType;
import com.expercise.interpreter.core.model.challenge.TestCase;
import org.python.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PythonInterpreter extends Interpreter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PythonInterpreter.class);

    private static final Map<DataType, Class<? extends PyObject>> TYPE_MAP = new HashMap<>();

    static {
        TYPE_MAP.put(DataType.Integer, PyInteger.class);
        TYPE_MAP.put(DataType.Text, PyString.class);
    }

    private final org.python.util.PythonInterpreter PYTHON_INTERPRETER;

    public PythonInterpreter() {
        System.setProperty("python.import.site", "false");
        PYTHON_INTERPRETER = new org.python.util.PythonInterpreter();
    }

    @Override
    protected void interpretInternal(ChallengeEvaluationContext context) throws InterpreterException {
        executeSourceCode(PYTHON_INTERPRETER, context.getSolution());

        PyFunction solutionFunctionToCall = getSolutionFunctionToCall(PYTHON_INTERPRETER);

        Challenge challenge = context.getChallenge();

        for (TestCaseWithResult eachTestCaseWithResult : context.getTestCaseWithResults()) {
            TestCase eachTestCase = eachTestCaseWithResult.getTestCase();
            Object resultValue = makeFunctionCallAndGetResultValue(solutionFunctionToCall, challenge, eachTestCase);
            processTestCase(challenge, eachTestCaseWithResult, resultValue);
        }

        context.decideInterpreterResult();
    }

    private void processTestCase(Challenge challenge, TestCaseWithResult eachTestCaseWithResult, Object resultValue) {
        TestCase eachTestCase = eachTestCaseWithResult.getTestCase();
        if (!challenge.getOutputType().convert(eachTestCase.getOutput()).equals(resultValue)) {
            eachTestCaseWithResult.setTestCaseResult(TestCaseResult.FAILED);
            eachTestCaseWithResult.setActualValue(resultValue.toString());
        } else {
            eachTestCaseWithResult.setTestCaseResult(TestCaseResult.PASSED);
            eachTestCaseWithResult.setActualValue(resultValue.toString());
        }
    }

    private void executeSourceCode(org.python.util.PythonInterpreter pythonInterpreter, String sourceCode) throws InterpreterException {
        try {
            pythonInterpreter.exec(sourceCode);
        } catch (PySyntaxError e) {
            LOGGER.debug("Syntax error: ", e);
            throw new InterpreterException(InterpreterResult.syntaxErrorFailedResult());
        }
    }

    private PyFunction getSolutionFunctionToCall(org.python.util.PythonInterpreter pythonInterpreter) throws InterpreterException {
        PyStringMap locals = (PyStringMap) pythonInterpreter.getLocals();

        Optional<PyFunction> functionToCall = (Optional<PyFunction>) locals.values().stream()
                .filter(o -> o instanceof PyFunction)
                .filter(f -> "solution".equals(((PyFunction) f).getFuncName()))
                .findFirst();

        if (!functionToCall.isPresent()) {
            throw new InterpreterException(InterpreterResult.noResultFailedResult());
        }

        return functionToCall.get();
    }

    private Object makeFunctionCallAndGetResultValue(PyFunction solutionFunctionToCall, Challenge challenge, TestCase testCase) throws InterpreterException {
        Object resultAsJavaObject = null;
        try {
            PyObject resultAsPyObject = solutionFunctionToCall.__call__(getArgumentsAsPyObjects(challenge, testCase));
            Class<? extends PyObject> outputType = TYPE_MAP.get(challenge.getOutputType());

            if ("NoneType".equals(resultAsPyObject.getType().getName())) {
                throw new InterpreterException(InterpreterResult.noResultFailedResult());
            }

            if (outputType.isAssignableFrom(PyInteger.class)) {
                resultAsJavaObject = resultAsPyObject.asInt();
            } else if (outputType.isAssignableFrom(PyString.class)) {
                resultAsJavaObject = resultAsPyObject.asString();
            }
        } catch (PyException e) {
            LOGGER.debug("Exception while function call: ", e);
            if ("TypeError".equals(((PyType) e.type).getName())) {
                throw new InterpreterException(InterpreterResult.typeErrorFailedResult());
            } else {
                throw new InterpreterException(InterpreterResult.noResultFailedResult());
            }
        }

        return resultAsJavaObject;
    }

    private PyObject[] getArgumentsAsPyObjects(Challenge challenge, TestCase testCase) throws InterpreterException {
        int argSize = testCase.getInputs().size();

        PyObject[] pyObjects = new PyObject[argSize];

        for (int i = 0; i < argSize; i++) {
            try {
                DataType type = challenge.getInputTypes().get(i);
                Class<?> clazz = Class.forName(type.getClassName());
                Class<? extends PyObject> instanceType = TYPE_MAP.get(type);
                if (type.equals(DataType.Integer)) {
                    clazz = int.class;
                }
                Constructor<? extends PyObject> declaredConstructor = instanceType.getDeclaredConstructor(clazz);
                pyObjects[i] = declaredConstructor.newInstance(type.convert(testCase.getInputs().get(i)));
            } catch (Exception e) {
                LOGGER.debug("Exception while preparing arguments: ", e);
                throw new InterpreterException(InterpreterResult.noResultFailedResult());
            }
        }

        return pyObjects;
    }

}
