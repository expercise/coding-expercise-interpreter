package com.expercise.interpreter.runtime;

public class InterpreterContainer {

    private int hostPort;

    private String containerId;

    public InterpreterContainer(int hostPort, String containerId) {
        this.hostPort = hostPort;
        this.containerId = containerId;
    }

    public int getHostPort() {
        return hostPort;
    }

    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

}
