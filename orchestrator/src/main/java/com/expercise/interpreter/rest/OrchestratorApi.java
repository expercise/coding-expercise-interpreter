package com.expercise.interpreter.rest;

import com.expercise.interpreter.core.Constants;
import com.expercise.interpreter.runtime.InterpreterContainerOrchestrator;
import com.expercise.interpreter.util.HttpUtils;
import org.apache.commons.lang3.StringUtils;

import static spark.Spark.port;
import static spark.Spark.post;

public final class OrchestratorApi {

    private static InterpreterContainerOrchestrator containerOrchestrator = InterpreterContainerOrchestrator.getInstance();

    public static void main(String... args) {
        // TODO ufuk: get pool size from args

        containerOrchestrator.initializeContainerPool(3);

        startServer(args);
    }

    private static void startServer(String... args) {
        setPortNumber(args);
        post(Constants.VALIDATION_ENDPOINT, (request, response) -> {
            response.type(HttpUtils.JSON_CONTENT_TYPE);

            // TODO ufuk: complete

            return null;
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
