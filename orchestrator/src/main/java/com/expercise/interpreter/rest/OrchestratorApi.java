package com.expercise.interpreter.rest;

import com.expercise.interpreter.com.expercise.interpreter.rest.ValidateSolutionRequest;
import com.expercise.interpreter.com.expercise.interpreter.rest.ValidateSolutionResponse;
import com.expercise.interpreter.core.Constants;
import com.expercise.interpreter.runtime.InterpreterContainerOrchestrator;
import com.expercise.interpreter.util.HttpUtils;
import com.expercise.interpreter.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import static spark.Spark.port;
import static spark.Spark.post;

public final class OrchestratorApi {

    private static InterpreterContainerOrchestrator interpreterContainerOrchestrator = new InterpreterContainerOrchestrator();

    public static void main(String... args) {
        // TODO ufuk: get pool size from args

        interpreterContainerOrchestrator.initializeContainerPool(10);

        startServer(args);
    }

    private static void startServer(String... args) {
        setPortNumber(args);

        post(Constants.VALIDATION_ENDPOINT, (request, response) -> {
            response.type(HttpUtils.JSON_CONTENT_TYPE);

            ValidateSolutionRequest solutionRequest = JsonUtils.fromJsonString(request.body(), ValidateSolutionRequest.class);

            ValidateSolutionResponse solutionResponse = interpreterContainerOrchestrator.interpret(solutionRequest);

            return JsonUtils.toJsonString(solutionResponse);
        });
    }

    private static void setPortNumber(String... args) {
        if (args.length == 0) {
            return;
        }

        String portNumberArgument = args[0];
        if (StringUtils.isNumeric(portNumberArgument)) {
            port(Integer.parseInt(portNumberArgument));
        }
    }

}
