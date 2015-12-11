package com.expercise.interpreter.rest;

public final class OrchestratorMain {

    public static void main(String... args) throws Exception {
        // TODO ufuk: get pool size and other settings from args
        new OrchestratorServer(10).start();
    }

}
