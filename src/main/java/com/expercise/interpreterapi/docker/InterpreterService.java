package com.expercise.interpreterapi.docker;

import com.expercise.interpreterapi.exception.InterpreterException;
import com.expercise.interpreterapi.request.InterpretRequest;
import com.expercise.interpreterapi.response.InterpretResponse;
import com.expercise.interpreterapi.model.ProgrammingLanguage;
import com.expercise.interpreterapi.storage.FileService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.beans.Introspector;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class InterpreterService {

    private final ApplicationContext applicationContext;
    private final FileService fileService;

    @Autowired
    public InterpreterService(ApplicationContext applicationContext, FileService fileService) {
        this.applicationContext = applicationContext;
        this.fileService = fileService;
    }

    public InterpretResponse interpret(InterpretRequest request) {
        String uniqueId = UUID.randomUUID().toString().replaceAll("-", "");
        ProgrammingLanguage progLang = ProgrammingLanguage.valueOf(request.getProgrammingLanguage());
        Path hostFilePath = Paths.get("/tmp/eintrp/", progLang.name(), uniqueId, progLang.getFileName());
        Path containerPath = Paths.get("/tmp", uniqueId);

        fileService.write(request.getSourceCode(), hostFilePath);

        InterpretResponse response = selectDockerService(request).runContainer(hostFilePath.getParent(), containerPath);

        fileService.delete(hostFilePath.getParent());

        return response;
    }

    private DockerService selectDockerService(InterpretRequest request) {
        try {
            Object bean = applicationContext.getBean(Introspector.decapitalize(request.getProgrammingLanguage()) + "DockerService");
            return (DockerService) bean;
        } catch (BeansException e) {
            throw new InterpreterException("Docker runner service could not be found for request: " + request.toString(), e);
        }
    }
}
