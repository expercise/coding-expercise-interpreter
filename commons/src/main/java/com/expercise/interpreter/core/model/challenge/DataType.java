package com.expercise.interpreter.core.model.challenge;

public enum DataType {

    Integer(java.lang.Integer.class.getName()) {
        @Override
        public Object convert(String rawValue) {
            return java.lang.Integer.parseInt(rawValue);
        }
    },

    Text(String.class.getName()) {
        @Override
        public Object convert(String rawValue) {
            return rawValue;
        }
    };

    private String className;

    DataType(String className) {
        this.className = className;
    }

    public abstract Object convert(String rawValue);

    public String getClassName() {
        return className;
    }

}
