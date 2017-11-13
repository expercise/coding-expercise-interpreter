package com.expercise.interpreterapi.response;

import org.apache.commons.lang.StringUtils;

public class InterpretResponse extends ErrorResponse {

    private final String stdOut;
    private final String stdErr;

    public InterpretResponse(String stdOut, String stdErr) {
        this.stdOut = StringUtils.trim(stdOut);
        this.stdErr = StringUtils.trim(stdErr);
    }

    public String getStdOut() {
        return stdOut;
    }

    public String getStdErr() {
        return stdErr;
    }
}
