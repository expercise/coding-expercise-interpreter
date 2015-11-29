package com.expercise.interpreter.com.expercise.interpreter.rest;

import com.expercise.interpreter.core.model.challenge.Challenge;
import com.expercise.interpreter.core.model.challenge.ProgrammingLanguage;

public class ValidateSolutionRequest {

    private Challenge challenge;

    private String solution;

    private ProgrammingLanguage programmingLanguage;

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

    public ProgrammingLanguage getProgrammingLanguage() {
        return programmingLanguage;
    }

    public void setProgrammingLanguage(ProgrammingLanguage programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

}
