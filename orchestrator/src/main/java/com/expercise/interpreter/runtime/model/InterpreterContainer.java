package com.expercise.interpreter.runtime.model;

import java.io.Serializable;

public class InterpreterContainer implements Serializable {

    private static final long serialVersionUID = 1792951626243805018L;

    private int hostPort;

    private String containerId;

    public InterpreterContainer(int hostPort) {
        this.hostPort = hostPort;
    }

    public InterpreterContainer(int hostPort, String containerId) {
        this.hostPort = hostPort;
        this.containerId = containerId;
    }

    public int getHostPort() {
        return hostPort;
    }

    public String getContainerId() {
        return containerId;
    }

}
