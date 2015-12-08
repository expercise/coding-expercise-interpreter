package com.expercise.interpreter.runtime;

import com.expercise.interpreter.runtime.model.InterpreterContainer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class InterpreterContainerReInitializer {

    // TODO ufuk: log and handle the exceptions

    public static final InterpreterContainerReInitializer INSTANCE = new InterpreterContainerReInitializer();

    private final BlockingQueue<InterpreterContainer> containerGarbage = new LinkedBlockingQueue<>();

    private InterpreterContainerReInitializer() {
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
                    DockerHelper.destroyContainer(interpreterContainer.getContainerId());
                    String newContainerId = DockerHelper.runNewContainer(interpreterContainer.getHostPort());
                    interpreterContainer.setContainerId(newContainerId);

                    new Thread(() -> {
                        try {
                            Thread.sleep(1000); // Wait for initializing interpreter api
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        interpreterContainer.notifyReInitialized();
                    }, "Thread-Wait-And-Notify-" + interpreterContainer.getHostPort()).start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Thread-Re-Initializing-Event-Loop").start();
    }

}
