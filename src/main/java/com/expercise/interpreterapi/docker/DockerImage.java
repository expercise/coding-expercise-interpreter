package com.expercise.interpreterapi.docker;

public enum DockerImage {

    JAVASCRIPT("node:8.9.0-slim"),
    PYTHON_2("python:2.7.14-slim-stretch"),
    PYTHON_3("python:3.6.3-slim-stretch");

    private final String imageName;

    DockerImage(String imageName) {
        this.imageName = imageName;
    }

    public String imageName() {
        return imageName;
    }
}
