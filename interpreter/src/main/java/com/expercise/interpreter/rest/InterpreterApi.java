package com.expercise.interpreter.rest;

import com.expercise.interpreter.com.expercise.interpreter.rest.ValidateSolutionRequest;
import com.expercise.interpreter.com.expercise.interpreter.rest.ValidateSolutionResponse;
import com.expercise.interpreter.core.Constants;
import com.expercise.interpreter.core.model.ChallengeEvaluationContext;
import com.expercise.interpreter.service.SolutionValidationService;
import com.expercise.interpreter.util.HttpUtils;
import com.expercise.interpreter.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import static spark.Spark.port;
import static spark.Spark.post;

public final class InterpreterApi {

    public static void main(String... args) {
        setPortNumber(args);
        post(Constants.VALIDATION_ENDPOINT, (request, response) -> {
            response.type(HttpUtils.JSON_CONTENT_TYPE);

            ValidateSolutionRequest solutionRequest = JsonUtils.fromJsonString(request.body(), ValidateSolutionRequest.class);

            ChallengeEvaluationContext challengeEvaluationContext = new ChallengeEvaluationContext();
            challengeEvaluationContext.setChallenge(solutionRequest.getChallenge());
            challengeEvaluationContext.setProgrammingLanguage(solutionRequest.getProgrammingLanguage());
            challengeEvaluationContext.setSolution(solutionRequest.getSolution());

            SolutionValidationService.getInstance().interpret(challengeEvaluationContext);

            ValidateSolutionResponse solutionResponse = new ValidateSolutionResponse();
            solutionResponse.setInterpreterResult(challengeEvaluationContext.getInterpreterResult());

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
