package com.expercise.interpreter.runtime;

import com.expercise.interpreter.runtime.model.InterpreterContainer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class InterpreterContainerReInitializer {

    // TODO ufuk: log and handle the exceptions

    private final BlockingQueue<InterpreterContainer> containerGarbage = new LinkedBlockingQueue<>();

    private final DockerHelper dockerHelper;

    public InterpreterContainerReInitializer(DockerHelper dockerHelper) {
        this.dockerHelper = dockerHelper;
        startReInitializingEventLoop();
    }

    public void reInitialize(InterpreterContainer interpreterContainer) {
        try {
            containerGarbage.put(interpreterContainer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startReInitializingEventLoop() {
        new Thread(() -> {
            while (true) {
                try {
                    InterpreterContainer interpreterContainer = containerGarbage.take();
                    dockerHelper.destroyContainer(interpreterContainer.getContainerId());
                    String newContainerId = dockerHelper.runNewInterpreterContainer(interpreterContainer.getHostPort());
                    interpreterContainer.setContainerId(newContainerId);
                    interpreterContainer.notifyReInitialized();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Thread-Re-Initializing-Event-Loop").start();
    }

}
