package com.expercise.interpreter.runtime;

import com.expercise.interpreter.com.expercise.interpreter.rest.ValidateSolutionRequest;
import com.expercise.interpreter.com.expercise.interpreter.rest.ValidateSolutionResponse;
import com.expercise.interpreter.runtime.model.InterpreterContainer;
import com.expercise.interpreter.util.HttpUtils;
import com.expercise.interpreter.util.JsonUtils;
import io.vertx.core.AsyncResultHandler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

public final class InterpreterContainerOrchestrator {

    private static final int INITIAL_HOST_PORT = 4568;

    private final BlockingQueue<InterpreterContainer> containerPool = new LinkedBlockingQueue<>();

    private final Map<Integer, String> initializedContainers = new ConcurrentHashMap<>();

    private final Vertx vertx;

    public InterpreterContainerOrchestrator(int poolSize, Vertx vertx) {
        this.vertx = vertx;

        this.vertx.eventBus()
                .<InterpreterContainer>consumer(
                        "InterpreterContainerInitialized",
                        event -> {
                            InterpreterContainer interpreterContainer = event.body();
                            initializedContainers.put(interpreterContainer.getHostPort(), interpreterContainer.getContainerId());
                        }
                )
                .completionHandler(event -> initializeContainerPool(poolSize));

        this.vertx.eventBus()
                .<Integer>consumer(
                        "InterpreterContainerReady",
                        event -> {
                            Integer hostPort = event.body();
                            String containerId = initializedContainers.get(hostPort);
                            containerPool.add(new InterpreterContainer(hostPort, containerId));
                        });
    }

    private void initializeContainerPool(int poolSize) {
        IntStream.range(INITIAL_HOST_PORT, INITIAL_HOST_PORT + poolSize)
                .forEach(eachPort -> vertx.eventBus().send(
                        "InitializeInterpreterContainer",
                        new InterpreterContainer(eachPort),
                        new DeliveryOptions().setCodecName("InterpreterContainer")
                ));
    }

    public void validateSolution(ValidateSolutionRequest validateSolutionRequest, AsyncResultHandler<ValidateSolutionResponse> asyncResultHandler) {
        vertx.executeBlocking(
                future -> {
                    InterpreterContainer interpreterContainer = null;
                    ValidateSolutionResponse result = null;
                    try {
                        interpreterContainer = containerPool.take();

                        HttpClient client = HttpClientBuilder.create().build();
                        HttpPost httpPost = new HttpPost("http://localhost:" + interpreterContainer.getHostPort() + "/validateSolution");
                        httpPost.setEntity(new StringEntity(JsonUtils.toJsonString(validateSolutionRequest)));
                        httpPost.setHeader("Accept", HttpUtils.JSON_CONTENT_TYPE);
                        httpPost.setHeader("Content-Type", HttpUtils.JSON_CONTENT_TYPE);

                        HttpResponse httpResponse = client.execute(httpPost);

                        String responseBodyAsJsonString = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
                        result = JsonUtils.fromJsonString(responseBodyAsJsonString, ValidateSolutionResponse.class);
                    } catch (Exception ignored) {
                        // TODO ufuk: log and handle the exceptions
                        ignored.printStackTrace();
                    } finally {
                        if (interpreterContainer != null) {
                            vertx.eventBus().send(
                                    "InitializeInterpreterContainer",
                                    interpreterContainer,
                                    new DeliveryOptions().setCodecName("InterpreterContainer")
                            );
                        }
                        future.complete(result);
                    }
                },
                false,
                asyncResultHandler);
    }

}
