package com.expercise.interpreterapi.request;

import com.expercise.interpreterapi.request.validator.ValidProgrammingLanguage;

public class InterpretRequest {

    private String sourceCode;

    @ValidProgrammingLanguage
    private String programmingLanguage;

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getProgrammingLanguage() {
        return programmingLanguage;
    }

    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    @Override
    public String toString() {
        return "InterpretRequest{" +
                "sourceCode='" + sourceCode + '\'' +
                ", programmingLanguage=" + programmingLanguage +
                '}';
    }
}
