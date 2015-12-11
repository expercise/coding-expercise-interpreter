package com.expercise.interpreter.rest;

import com.expercise.interpreter.com.expercise.interpreter.rest.ValidateSolutionRequest;
import com.expercise.interpreter.com.expercise.interpreter.rest.ValidateSolutionResponse;
import com.expercise.interpreter.core.Constants;
import com.expercise.interpreter.runtime.InterpreterContainerOrchestrator;
import com.expercise.interpreter.util.HttpUtils;
import com.expercise.interpreter.util.JsonUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public final class OrchestratorServer extends AbstractVerticle {

    public InterpreterContainerOrchestrator containerOrchestrator;

    public OrchestratorServer(int poolSize) {
        containerOrchestrator = new InterpreterContainerOrchestrator();
        containerOrchestrator.initializeContainerPool(poolSize);
    }

    @Override
    public void start() throws Exception {
        vertx = Vertx.vertx();

        Router router = Router.router(vertx);
        router.route(Constants.VALIDATION_ENDPOINT).handler(BodyHandler.create());
        router.post(Constants.VALIDATION_ENDPOINT).handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", HttpUtils.JSON_CONTENT_TYPE);

            ValidateSolutionRequest solutionRequest = JsonUtils.fromJsonString(routingContext.getBodyAsString(), ValidateSolutionRequest.class);

            ValidateSolutionResponse solutionResponse = containerOrchestrator.interpret(solutionRequest);

            response.end(JsonUtils.toJsonString(solutionResponse));
        });

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(Constants.DEFAULT_SERVER_PORT);
    }

}
