package com.expercise.interpreterapi.docker;

import com.expercise.interpreterapi.model.ProgrammingLanguage;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ExecCreation;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class JavaScriptInterpreterContainer extends InterpreterContainer {

    public JavaScriptInterpreterContainer() throws DockerCertificateException, DockerException, InterruptedException {
    }

    @Override
    protected DockerImage getDockerImage() {
        return DockerImage.JAVASCRIPT;
    }

    @Override
    public InterpreterContainer.Response execute(String containerId) throws DockerException, InterruptedException, IOException {
        final String[] command = {"node", ProgrammingLanguage.JavaScript.getFileName()};
        final ExecCreation execCreation = docker.execCreate(
                containerId,
                command,
                DockerClient.ExecCreateParam.attachStdout(), DockerClient.ExecCreateParam.attachStderr()
        );
        final LogStream output = docker.execStart(execCreation.id());

        OutputStream stdOut = new ContainerOutputThresholdingOutputStream(new ByteArrayOutputStream(), STDOUT_BYTE_LIMIT);
        OutputStream stdErr = new ContainerOutputThresholdingOutputStream(new ByteArrayOutputStream(), STDERR_BYTE_LIMIT);
        output.attach(stdOut, stdErr, true);
        return new Response(stdOut.toString(), stdErr.toString());
    }
}
