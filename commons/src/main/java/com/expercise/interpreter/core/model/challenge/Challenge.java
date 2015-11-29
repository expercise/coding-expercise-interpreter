package com.expercise.interpreter.core.model.challenge;

import java.util.ArrayList;
import java.util.List;

public class Challenge {

    private List<DataType> inputTypes = new ArrayList<>();

    private DataType outputType;

    private List<TestCase> testCases = new ArrayList<>();

    public List<DataType> getInputTypes() {
        return inputTypes;
    }

    public void setInputTypes(List<DataType> inputTypes) {
        this.inputTypes = inputTypes;
    }

    public DataType getOutputType() {
        return outputType;
    }

    public void setOutputType(DataType outputType) {
        this.outputType = outputType;
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }

    public List<Object> getConvertedInputValues(List<String> inputValues) {
        List<Object> inputs = new ArrayList<>();
        for (int index = 0; index < inputTypes.size(); index++) {
            inputs.add(inputTypes.get(index).convert(inputValues.get(index)));
        }
        return inputs;
    }

}
