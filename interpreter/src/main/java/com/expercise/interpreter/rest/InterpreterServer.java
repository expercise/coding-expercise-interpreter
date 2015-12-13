package com.expercise.interpreter.rest;

import com.expercise.interpreter.com.expercise.interpreter.rest.ValidateSolutionRequest;
import com.expercise.interpreter.com.expercise.interpreter.rest.ValidateSolutionResponse;
import com.expercise.interpreter.core.Constants;
import com.expercise.interpreter.core.model.ChallengeEvaluationContext;
import com.expercise.interpreter.service.SolutionValidationService;
import com.expercise.interpreter.util.HttpUtils;
import com.expercise.interpreter.util.JsonUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public final class InterpreterServer extends AbstractVerticle {

    public InterpreterServer() {
    }

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.route(Constants.VALIDATION_ENDPOINT).handler(BodyHandler.create());
        router.post(Constants.VALIDATION_ENDPOINT).handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", HttpUtils.JSON_CONTENT_TYPE);

            ValidateSolutionRequest solutionRequest = JsonUtils.fromJsonString(routingContext.getBodyAsString(), ValidateSolutionRequest.class);

            ChallengeEvaluationContext challengeEvaluationContext = new ChallengeEvaluationContext();
            challengeEvaluationContext.setChallenge(solutionRequest.getChallenge());
            challengeEvaluationContext.setProgrammingLanguage(solutionRequest.getProgrammingLanguage());
            challengeEvaluationContext.setSolution(solutionRequest.getSolution());

            new SolutionValidationService().interpret(challengeEvaluationContext);

            ValidateSolutionResponse solutionResponse = new ValidateSolutionResponse();
            solutionResponse.setInterpreterResult(challengeEvaluationContext.getInterpreterResult());

            response.end(JsonUtils.toJsonString(solutionResponse));
        });

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(Constants.DEFAULT_SERVER_PORT);
    }

}
