package com.expercise.interpreter.core.java.compiler;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class InMemoryJavaCompiler {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryJavaCompiler.class);

    private final String classesPath = System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID() + File.separator;

    private final String className;

    public InMemoryJavaCompiler(String className) {
        this.className = className;
    }

    public InMemoryJavaCompiler compile(String sourceCode) throws Exception {
        JavaFileObject sourceFile = new InMemoryJavaFileObject(className, sourceCode);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        Iterable options = Arrays.asList("-d", classesPath);
        List<JavaFileObject> files = Collections.singletonList(sourceFile);

        FileUtils.forceMkdir(new File(classesPath));
        JavaCompiler.CompilationTask compilationTask = compiler.getTask(null, fileManager, null, options, null, files);

        if (Boolean.FALSE.equals(compilationTask.call())) {
            LOGGER.debug("Compilation is failed! Source: {}", sourceCode);
            throw new JavaCompilerException("Compilation is failed!");
        }

        return this;
    }

    // TODO ufuk: handle primitive and wrapper parameter type mess (Integer.class vs int.class)
    public Object invoke(String methodName, Object[] args, Class<?>[] parameterTypes) throws Exception {
        Object result = null;

        File directory = new File(classesPath);
        try {
            ClassLoader loader = new URLClassLoader(new URL[]{directory.toURI().toURL()});
            Class compiledClass = loader.loadClass(className);

            Object instance = compiledClass.newInstance();

            Method method = compiledClass.getDeclaredMethod(methodName, parameterTypes);

            result = method.invoke(instance, args);
        } catch (Exception e) {
            LOGGER.debug(String.format("Exception while invoking the method \"%s\": ", methodName), e);
        }

        return result;
    }

    public void clean() {
        try {
            FileUtils.deleteDirectory(new File(classesPath));
        } catch (IOException e) {
            LOGGER.debug(String.format("Exception while cleaning directory \"%s\": ", classesPath), e);
        }
    }

    private static class InMemoryJavaFileObject extends SimpleJavaFileObject {

        private String sourceCode;

        public InMemoryJavaFileObject(String className, String sourceCode) throws Exception {
            super(URI.create("string:///" + className.replace(".", "/") + Kind.SOURCE.extension), Kind.SOURCE);
            this.sourceCode = sourceCode;
        }

        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return sourceCode;
        }

    }

}