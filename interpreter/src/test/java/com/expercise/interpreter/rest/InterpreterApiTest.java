package com.expercise.interpreter.rest;

import com.expercise.interpreter.com.expercise.interpreter.rest.ValidateSolutionRequest;
import com.expercise.interpreter.com.expercise.interpreter.rest.ValidateSolutionResponse;
import com.expercise.interpreter.core.InterpreterFailureType;
import com.expercise.interpreter.core.InterpreterResult;
import com.expercise.interpreter.core.model.challenge.Challenge;
import com.expercise.interpreter.core.model.challenge.DataType;
import com.expercise.interpreter.core.model.challenge.ProgrammingLanguage;
import com.expercise.interpreter.core.model.challenge.TestCase;
import com.expercise.interpreter.util.HttpUtils;
import com.expercise.interpreter.util.JsonUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import static spark.Spark.stop;

public class InterpreterApiTest {

    @BeforeClass
    public static void startServer() {
        InterpreterApi.main();
    }

    @AfterClass
    public static void stopServer() {
        stop();
    }

    @Test
    public void shouldValidateChallengeAndReturnSuccessResponseIfSolutionValid() throws Exception {
        // Create request object
        Challenge challenge = new Challenge();
        challenge.setInputTypes(Arrays.asList(DataType.Integer, DataType.Integer));
        challenge.setOutputType(DataType.Integer);
        challenge.setTestCases(Arrays.asList(
                new TestCase() {{
                    getInputs().add("1");
                    getInputs().add("2");
                    setOutput("3");
                }},
                new TestCase() {{
                    getInputs().add("-1");
                    getInputs().add("-99");
                    setOutput("-100");
                }}
        ));

        ValidateSolutionRequest request = new ValidateSolutionRequest();
        request.setProgrammingLanguage(ProgrammingLanguage.JavaScript);
        request.setSolution("function solution(a, b) { return a + b; }");
        request.setChallenge(challenge);

        // Do request
        HttpResponse response = executeRequest(request);

        // Check response
        assertThat(response.getStatusLine().getStatusCode(), equalTo(200));

        String responseBodyAsJsonString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
        ValidateSolutionResponse solutionResponse = JsonUtils.fromJsonString(responseBodyAsJsonString, ValidateSolutionResponse.class);

        InterpreterResult interpreterResult = solutionResponse.getInterpreterResult();
        assertTrue(interpreterResult.isSuccess());
        assertThat(interpreterResult.getFailureType(), nullValue());
    }

    @Test
    public void shouldValidateChallengeAndReturnFailedResponseIfSolutionInvalid() throws Exception {
        // Create request object
        Challenge challenge = new Challenge();
        challenge.setInputTypes(Arrays.asList(DataType.Integer, DataType.Integer));
        challenge.setOutputType(DataType.Integer);
        challenge.setTestCases(Arrays.asList(
                new TestCase() {{
                    getInputs().add("1");
                    getInputs().add("2");
                    setOutput("3");
                }},
                new TestCase() {{
                    getInputs().add("-1");
                    getInputs().add("-99");
                    setOutput("-100");
                }}
        ));

        ValidateSolutionRequest request = new ValidateSolutionRequest();
        request.setProgrammingLanguage(ProgrammingLanguage.JavaScript);
        request.setSolution("function solution(a, b) { return \"Invalid Solution\"; }");
        request.setChallenge(challenge);

        // Do request
        HttpResponse response = executeRequest(request);

        // Check response
        assertThat(response.getStatusLine().getStatusCode(), equalTo(200));

        String responseBodyAsJsonString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
        ValidateSolutionResponse solutionResponse = JsonUtils.fromJsonString(responseBodyAsJsonString, ValidateSolutionResponse.class);

        InterpreterResult interpreterResult = solutionResponse.getInterpreterResult();
        assertFalse(interpreterResult.isSuccess());
        assertThat(interpreterResult.getFailureType(), equalTo(InterpreterFailureType.TYPE_ERROR));
    }

    private HttpResponse executeRequest(ValidateSolutionRequest request) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost("http://localhost:4567/validateSolution");
        httpPost.setEntity(new StringEntity(JsonUtils.toJsonString(request)));
        httpPost.setHeader("Accept", HttpUtils.JSON_CONTENT_TYPE);
        httpPost.setHeader("Content-type", HttpUtils.JSON_CONTENT_TYPE);

        return client.execute(httpPost);
    }

}