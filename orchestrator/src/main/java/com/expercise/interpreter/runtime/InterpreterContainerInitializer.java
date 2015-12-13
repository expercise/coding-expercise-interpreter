package com.expercise.interpreter.runtime;

import com.expercise.interpreter.runtime.model.InterpreterContainer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import org.apache.commons.lang3.StringUtils;

public final class InterpreterContainerInitializer extends AbstractVerticle {

    private final DockerHelper dockerHelper = DockerHelper.getInstance();

    private Handler<AsyncResult<Void>> completionHandler;

    public InterpreterContainerInitializer(Handler<AsyncResult<Void>> completionHandler) {
        this.completionHandler = completionHandler;
    }

    @Override
    public void start() throws Exception {
        vertx.eventBus()
                .consumer("InitializeInterpreterContainer", new Handler<Message<InterpreterContainer>>() {
                    @Override
                    public void handle(Message<InterpreterContainer> message) {
                        vertx.<InterpreterContainer>executeBlocking(
                                future -> {
                                    InterpreterContainer oldInterpreterContainer = message.body();

                                    if (StringUtils.isNotBlank(oldInterpreterContainer.getContainerId())) {
                                        dockerHelper.destroyContainer(oldInterpreterContainer.getContainerId());
                                    }

                                    String newContainerId = dockerHelper.runNewInterpreterContainer(oldInterpreterContainer.getHostPort());

                                    InterpreterContainer newInterpreterContainer = new InterpreterContainer(oldInterpreterContainer.getHostPort(), newContainerId);
                                    future.complete(newInterpreterContainer);
                                },
                                false,
                                asyncResult -> vertx.eventBus().send(
                                        "InterpreterContainerInitialized",
                                        asyncResult.result(),
                                        new DeliveryOptions().setCodecName("InterpreterContainer")
                                )
                        );
                    }
                })
                .completionHandler(completionHandler);
    }

}
