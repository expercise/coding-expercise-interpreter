package com.expercise.interpreter.rest;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;

public final class OrchestratorMain {

    public static void main(String... args) throws Exception {
        // TODO ufuk: get pool size and other settings from args
        int poolSize = 10;

        Vertx vertx = Vertx.vertx();
        Verticle orchestratorServer = new OrchestratorServer(poolSize);
        vertx.deployVerticle(orchestratorServer);
    }

}
