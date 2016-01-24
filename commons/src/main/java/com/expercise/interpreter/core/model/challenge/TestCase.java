package com.expercise.interpreter.core.model.challenge;

import java.util.ArrayList;
import java.util.List;

public class TestCase {

    private List<String> inputs = new ArrayList<>();

    private String output;

    public TestCase() {
    }

    public TestCase(List<String> inputs, String output) {
        this.inputs = inputs;
        this.output = output;
    }

    public List<String> getInputs() {
        return inputs;
    }

    public void setInputs(List<String> inputs) {
        this.inputs = inputs;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

}
