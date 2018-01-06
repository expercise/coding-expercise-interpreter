package com.expercise.interpreterapi.docker;

import com.expercise.interpreterapi.exception.InterpreterException;
import org.apache.commons.io.output.ThresholdingOutputStream;

import java.io.OutputStream;

public class ContainerOutputThresholdingOutputStream extends ThresholdingOutputStream {

    private final OutputStream outputStream;

    public ContainerOutputThresholdingOutputStream(OutputStream outputStream, int threshold) {
        super(threshold);
        this.outputStream = outputStream;
    }

    @Override
    protected OutputStream getStream() {
        return this.outputStream;
    }

    @Override
    protected void thresholdReached() {
        throw new InterpreterException("Output byte limit has been exceeded.");
    }

    @Override
    public String toString() {
        return this.outputStream.toString();
    }
}
