package com.expercise.interpreter.core.model;

import com.expercise.interpreter.core.model.challenge.TestCase;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class TestCaseWithResult implements Serializable {

    private TestCase testCase;

    private TestCaseResult testCaseResult;

    private String actualValue;

    public TestCaseWithResult() {
    }

    public TestCaseWithResult(TestCase testCase) {
        this.testCase = testCase;
        this.testCaseResult = TestCaseResult.NEW;
    }

    public TestCase getTestCase() {
        return testCase;
    }

    public TestCaseResult getTestCaseResult() {
        return testCaseResult;
    }

    public void setTestCaseResult(TestCaseResult testCaseResult) {
        this.testCaseResult = testCaseResult;
    }

    public String getActualValue() {
        return actualValue;
    }

    public void setActualValue(String actualValue) {
        this.actualValue = actualValue;
    }

    @JsonIgnore
    public boolean isFailed() {
        return getTestCaseResult() == TestCaseResult.FAILED;
    }

}
