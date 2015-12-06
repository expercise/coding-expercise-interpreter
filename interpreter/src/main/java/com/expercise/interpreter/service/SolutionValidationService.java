package com.expercise.interpreter.service;

import com.expercise.interpreter.core.Interpreter;
import com.expercise.interpreter.core.javascript.JavaScriptInterpreter;
import com.expercise.interpreter.core.model.ChallengeEvaluationContext;
import com.expercise.interpreter.core.model.TestCaseWithResult;
import com.expercise.interpreter.core.model.challenge.ProgrammingLanguage;
import com.expercise.interpreter.core.model.challenge.TestCase;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SolutionValidationService {

    private static final Map<ProgrammingLanguage, Interpreter> INTERPRETERS = new ConcurrentHashMap<>();

    private static SolutionValidationService instance;

    private SolutionValidationService() {
        INTERPRETERS.put(ProgrammingLanguage.JavaScript, new JavaScriptInterpreter());
    }

    public synchronized static SolutionValidationService getInstance() {
        if (instance == null) {
            instance = new SolutionValidationService();
        }
        return instance;
    }

    public void interpret(ChallengeEvaluationContext context) {
        for (TestCase testCase : context.getChallenge().getTestCases()) {
            context.addTestCaseWithResult(new TestCaseWithResult(testCase));
        }
        getInterpreterFor(context.getProgrammingLanguage()).interpret(context);
    }

    private Interpreter getInterpreterFor(ProgrammingLanguage programmingLanguage) {
        return INTERPRETERS.get(programmingLanguage);
    }

}
