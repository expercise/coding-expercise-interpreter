package com.expercise.interpreter.core;

import com.expercise.interpreter.core.model.challenge.DataType;

import java.util.HashMap;
import java.util.Map;

public final class TypeChecker {

    private static final Map<DataType, Class<?>> JAVA_DATA_TYPE_MAPPING = new HashMap<>();

    static {
        JAVA_DATA_TYPE_MAPPING.put(DataType.Integer, Number.class);
        JAVA_DATA_TYPE_MAPPING.put(DataType.Text, String.class);
    }

    private TypeChecker() {
    }

    public static boolean check(Object value, DataType dataType) {
        Class<?> javaClassOfType = JAVA_DATA_TYPE_MAPPING.get(dataType);
        return javaClassOfType.isInstance(value);
    }

}