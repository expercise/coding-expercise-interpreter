package com.expercise.interpreterapi.interpreter;

import com.expercise.interpreterapi.docker.InterpreterContainer;
import com.expercise.interpreterapi.exception.InterpreterException;
import com.expercise.interpreterapi.model.ProgrammingLanguage;
import com.expercise.interpreterapi.request.validator.ValidProgrammingLanguage;
import com.expercise.interpreterapi.storage.FileService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.beans.Introspector;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class Interpreter {

    private final ApplicationContext applicationContext;
    private final FileService fileService;

    @Autowired
    public Interpreter(ApplicationContext applicationContext, FileService fileService) {
        this.applicationContext = applicationContext;
        this.fileService = fileService;
    }

    public Response interpret(Request request) {
        String uniqueId = UUID.randomUUID().toString().replaceAll("-", "");
        ProgrammingLanguage progLang = ProgrammingLanguage.valueOf(request.getProgrammingLanguage());
        Path hostFilePath = Paths.get("/tmp/eintrp/", progLang.name(), uniqueId, progLang.getFileName());
        Path containerPath = Paths.get("/tmp", uniqueId);

        fileService.write(request.getSourceCode(), hostFilePath);

        InterpreterContainer.Response containerResponse = selectInterpreterContainer(request).run(hostFilePath.getParent(), containerPath);

        fileService.delete(hostFilePath.getParent());

        return new Response(containerResponse.getStdOut(), containerResponse.getStdErr());
    }

    private InterpreterContainer selectInterpreterContainer(Request request) {
        try {
            Object bean = applicationContext.getBean(Introspector.decapitalize(request.getProgrammingLanguage()) + "InterpreterContainer");
            return (InterpreterContainer) bean;
        } catch (BeansException e) {
            throw new InterpreterException("Interpreter executor service could not be found for request: " + request.toString(), e);
        }
    }

    public static class Request {

        private String sourceCode;

        @ValidProgrammingLanguage
        private String programmingLanguage;

        public String getSourceCode() {
            return sourceCode;
        }

        public String getProgrammingLanguage() {
            return programmingLanguage;
        }

        @Override
        public String toString() {
            return "InterpretRequest{" +
                    "sourceCode='" + sourceCode + '\'' +
                    ", programmingLanguage=" + programmingLanguage +
                    '}';
        }
    }

    public static class Response {

        private final String stdOut;
        private final String stdErr;

        public Response(String stdOut, String stdErr) {
            this.stdOut = StringUtils.trim(stdOut);
            this.stdErr = StringUtils.trim(stdErr);
        }

        public String getStdOut() {
            return stdOut;
        }

        public String getStdErr() {
            return stdErr;
        }
    }
}
