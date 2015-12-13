package com.expercise.interpreter.rest;

import com.expercise.interpreter.com.expercise.interpreter.rest.ContainerReadyNotification;
import com.expercise.interpreter.core.Constants;
import com.expercise.interpreter.util.HttpUtils;
import com.expercise.interpreter.util.JsonUtils;
import io.vertx.core.Vertx;

public final class InterpreterMain {

    public static void main(String... args) throws Exception {
        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(new InterpreterServer(), asyncResult -> {
            int hostPort = Integer.parseInt(args[0]);
            vertx.createHttpClient()
                    .post(Constants.DEFAULT_SERVER_PORT, Constants.HOST, Constants.NOTIFY_CONTAINER_READY_ENDPOINT)
                    .putHeader("Accept", HttpUtils.JSON_CONTENT_TYPE)
                    .putHeader("Content-Type", HttpUtils.JSON_CONTENT_TYPE)
                    .handler(ignored -> {
                        // TODO ufuk: log
                    })
                    .end(JsonUtils.toJsonString(new ContainerReadyNotification(hostPort)));
        });


    }

}