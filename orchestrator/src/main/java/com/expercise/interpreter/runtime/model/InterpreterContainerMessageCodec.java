package com.expercise.interpreter.runtime.model;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import org.apache.commons.lang3.SerializationUtils;

public class InterpreterContainerMessageCodec implements MessageCodec<InterpreterContainer, Object> {

    @Override
    public void encodeToWire(Buffer buffer, InterpreterContainer interpreterContainer) {
        byte[] data = SerializationUtils.serialize(interpreterContainer);
        buffer.appendBytes(data);
    }

    @Override
    public InterpreterContainer decodeFromWire(int pos, Buffer buffer) {
        int length = buffer.getInt(pos);
        pos += 4;
        byte[] bytes = buffer.getBytes(pos, pos + length);
        return SerializationUtils.deserialize(bytes);
    }

    @Override
    public InterpreterContainer transform(InterpreterContainer interpreterContainer) {
        return interpreterContainer;
    }

    @Override
    public String name() {
        return "InterpreterContainer";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }

}
