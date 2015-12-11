package com.expercise.interpreter.runtime;

import com.expercise.interpreter.com.expercise.interpreter.rest.ValidateSolutionRequest;
import com.expercise.interpreter.com.expercise.interpreter.rest.ValidateSolutionResponse;
import com.expercise.interpreter.runtime.model.InterpreterContainer;
import com.expercise.interpreter.util.HttpUtils;
import com.expercise.interpreter.util.JsonUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class InterpreterContainerOrchestrator implements Observer {

    private static final int INITIAL_HOST_PORT = 4568;

    private final BlockingQueue<InterpreterContainer> containerPool = new LinkedBlockingQueue<>();

    private final DockerHelper dockerHelper;

    private final InterpreterContainerReInitializer interpreterContainerReInitializer;

    public InterpreterContainerOrchestrator() {
        this.dockerHelper = new DockerHelper();
        this.interpreterContainerReInitializer = new InterpreterContainerReInitializer(dockerHelper);
    }

    public void initializeContainerPool(int poolSize) {
        for (int hostPort = INITIAL_HOST_PORT; hostPort < poolSize + INITIAL_HOST_PORT; hostPort++) {
            String containerId = dockerHelper.runNewInterpreterContainer(hostPort);

            InterpreterContainer interpreterContainer = new InterpreterContainer(hostPort, containerId);
            interpreterContainer.addObserver(this);
            containerPool.add(interpreterContainer);
        }
    }

    public ValidateSolutionResponse interpret(ValidateSolutionRequest validateSolutionRequest) {
        InterpreterContainer interpreterContainer = null;
        try {
            interpreterContainer = containerPool.take();

            HttpClient client = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost("http://localhost:" + interpreterContainer.getHostPort() + "/validateSolution");
            httpPost.setEntity(new StringEntity(JsonUtils.toJsonString(validateSolutionRequest)));
            httpPost.setHeader("Accept", HttpUtils.JSON_CONTENT_TYPE);
            httpPost.setHeader("Content-type", HttpUtils.JSON_CONTENT_TYPE);

            HttpResponse httpResponse = client.execute(httpPost);

            String responseBodyAsJsonString = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
            return JsonUtils.fromJsonString(responseBodyAsJsonString, ValidateSolutionResponse.class);
        } catch (Exception e) {
            // TODO ufuk: log and handle the exceptions
            e.printStackTrace();
        } finally {
            if (interpreterContainer != null) {
                interpreterContainer.notifyReInitializing();
            }
        }
        return null;
    }

    @Override
    public void update(Observable observable, Object arg) {
        InterpreterContainer interpreterContainer = (InterpreterContainer) observable;
        boolean reInitialized = (Boolean) arg;

        if (reInitialized) {
            containerPool.add(interpreterContainer);
        } else {
            interpreterContainerReInitializer.reInitialize(interpreterContainer);
        }
    }

}
