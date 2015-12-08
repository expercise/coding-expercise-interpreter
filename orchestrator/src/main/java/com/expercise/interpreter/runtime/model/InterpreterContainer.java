package com.expercise.interpreter.runtime.model;

import java.util.Observable;

public class InterpreterContainer extends Observable {

    private int hostPort;

    private String containerId;

    public InterpreterContainer(int hostPort, String containerId) {
        this.hostPort = hostPort;
        this.containerId = containerId;
    }

    public void notifyReInitializing() {
        setChanged();
        notifyObservers(false);
    }

    public void notifyReInitialized() {
        setChanged();
        notifyObservers(true);
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
