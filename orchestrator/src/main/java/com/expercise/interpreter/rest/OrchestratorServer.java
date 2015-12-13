package com.expercise.interpreter.rest;

import com.expercise.interpreter.com.expercise.interpreter.rest.ContainerReadyNotification;
import com.expercise.interpreter.com.expercise.interpreter.rest.ValidateSolutionRequest;
import com.expercise.interpreter.com.expercise.interpreter.rest.ValidateSolutionResponse;
import com.expercise.interpreter.core.Constants;
import com.expercise.interpreter.runtime.InterpreterContainerOrchestrator;
import com.expercise.interpreter.util.HttpUtils;
import com.expercise.interpreter.util.JsonUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public final class OrchestratorServer extends AbstractVerticle {

    private int poolSize;

    public OrchestratorServer(int poolSize) {
        this.poolSize = poolSize;
    }

    @Override
    public void start() throws Exception {
        InterpreterContainerOrchestrator containerOrchestrator = new InterpreterContainerOrchestrator(poolSize, vertx);

        Router router = Router.router(vertx);
        router.route(Constants.VALIDATION_ENDPOINT).handler(BodyHandler.create());
        router.post(Constants.VALIDATION_ENDPOINT).handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("Content-Type", HttpUtils.JSON_CONTENT_TYPE);

            ValidateSolutionRequest solutionRequest = JsonUtils.fromJsonString(routingContext.getBodyAsString(), ValidateSolutionRequest.class);

            containerOrchestrator.validateSolution(solutionRequest, asyncResult -> {
                ValidateSolutionResponse result = asyncResult.result();
                response.end(JsonUtils.toJsonString(result));
            });
        });

        router.route(Constants.NOTIFY_CONTAINER_READY_ENDPOINT).handler(BodyHandler.create());
        router.post(Constants.NOTIFY_CONTAINER_READY_ENDPOINT).handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("Content-Type", HttpUtils.JSON_CONTENT_TYPE);

            ContainerReadyNotification containerReadyNotification = JsonUtils.fromJsonString(routingContext.getBodyAsString(), ContainerReadyNotification.class);

            vertx.eventBus().send("InterpreterContainerReady", containerReadyNotification.getHostPort());

            response.end();
        });

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(Constants.DEFAULT_SERVER_PORT);
    }

}
