package com.expercise.interpreterapi.request;

public enum ProgrammingLanguage {

    JavaScript("js"),
    Python2("py"),
    Python3("py");

    private final String extension;

    ProgrammingLanguage(String extension) {
        this.extension = extension;
    }

    public String getFileName() {
        return "a." + extension;
    }
}
