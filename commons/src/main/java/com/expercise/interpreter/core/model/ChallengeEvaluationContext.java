package com.expercise.interpreter.core.model;

import com.expercise.interpreter.core.InterpreterResult;
import com.expercise.interpreter.core.model.challenge.Challenge;
import com.expercise.interpreter.core.model.challenge.ProgrammingLanguage;

import java.util.ArrayList;
import java.util.List;

public class ChallengeEvaluationContext {

    private Challenge challenge;

    private String solution;

    private InterpreterResult interpreterResult;

    private ProgrammingLanguage programmingLanguage;

    private List<TestCaseWithResult> testCaseWithResults = new ArrayList<>();

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public InterpreterResult getInterpreterResult() {
        return interpreterResult;
    }

    public void setInterpreterResult(InterpreterResult interpreterResult) {
        this.interpreterResult = interpreterResult;
    }

    public ProgrammingLanguage getProgrammingLanguage() {
        return programmingLanguage;
    }

    public void setProgrammingLanguage(ProgrammingLanguage programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    public List<TestCaseWithResult> getTestCaseWithResults() {
        return testCaseWithResults;
    }

    public void addTestCaseWithResult(TestCaseWithResult testCaseWithResult) {
        getTestCaseWithResults().add(testCaseWithResult);
    }

    public void decideInterpreterResult() {
        boolean passedAllGivenTests = getTestCaseWithResults().stream().noneMatch(TestCaseWithResult::isFailed);
        if (passedAllGivenTests) {
            setInterpreterResult(InterpreterResult.createSuccessResult());
        } else {
            setInterpreterResult(InterpreterResult.createFailedResult());
        }
    }

}
