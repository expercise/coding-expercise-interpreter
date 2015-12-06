package com.expercise.interpreter.runtime;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public final class InterpreterContainerOrchestrator {

    private static InterpreterContainerOrchestrator instance;

    private static final int INITIAL_HOST_PORT = 4568;

    private static BlockingQueue<Integer> PORT_NUMBER_QUEUE;

    public synchronized static InterpreterContainerOrchestrator getInstance() {
        if (instance == null) {
            instance = new InterpreterContainerOrchestrator();
        }
        return instance;
    }

    public void initializeContainerPool(int poolSize) {
        // TODO ufuk: complete

        PORT_NUMBER_QUEUE = new ArrayBlockingQueue<>(poolSize);

        for (int hostPort = INITIAL_HOST_PORT; hostPort < poolSize + INITIAL_HOST_PORT; hostPort++) {
            PORT_NUMBER_QUEUE.add(hostPort);
            String containerId = DockerHelper.runNewContainer(String.valueOf(hostPort));

            new InterpreterContainer(hostPort, containerId);
        }
    }

}
