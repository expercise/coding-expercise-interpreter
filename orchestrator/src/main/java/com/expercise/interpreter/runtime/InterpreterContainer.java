package com.expercise.interpreter.runtime;

import java.util.Observable;

public class InterpreterContainer extends Observable {

    private int hostPort;

    private String containerId;

    public InterpreterContainer(int hostPort, String containerId) {
        this.hostPort = hostPort;
        this.containerId = containerId;
    }

    public void reInitialize() {
        setChanged();
        notifyObservers();
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
