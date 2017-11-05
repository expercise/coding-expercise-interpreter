package com.expercise.interpreterapi.response;

public class InterpretResponse extends BaseResponse {

    private final String stdOut;
    private final String stdErr;

    public InterpretResponse(String stdOut, String stdErr) {
        this.stdOut = stdOut;
        this.stdErr = stdErr;
    }

    public String getStdOut() {
        return stdOut;
    }

    public String getStdErr() {
        return stdErr;
    }
}
