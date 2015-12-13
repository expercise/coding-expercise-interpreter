package com.expercise.interpreter.com.expercise.interpreter.rest;

public class ContainerReadyNotification {

    private int hostPort;

    public ContainerReadyNotification() {
    }

    public ContainerReadyNotification(int hostPort) {
        this.hostPort = hostPort;
    }

    public int getHostPort() {
        return hostPort;
    }

    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }

}
