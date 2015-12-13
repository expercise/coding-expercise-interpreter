package com.expercise.interpreter.rest;

import com.expercise.interpreter.runtime.InterpreterContainerInitializer;
import com.expercise.interpreter.runtime.model.InterpreterContainerMessageCodec;
import io.vertx.core.Vertx;

public final class OrchestratorMain {

    public static void main(String... args) throws Exception {
        // TODO ufuk: get pool size and other settings from args
        int poolSize = 10;

        Vertx vertx = Vertx.vertx();
        vertx.eventBus().registerCodec(new InterpreterContainerMessageCodec());

        vertx.deployVerticle(new InterpreterContainerInitializer(
                event -> vertx.deployVerticle(new OrchestratorServer(poolSize))
        ));
    }

}
