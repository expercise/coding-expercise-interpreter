package com.expercise.interpreterapi.docker;

import com.expercise.interpreterapi.response.InterpretResponse;
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
public class Python2DockerService extends DockerService {

    public Python2DockerService() throws DockerCertificateException, DockerException, InterruptedException {
    }

    @Override
    protected DockerImage getDockerImage() {
        return DockerImage.PYTHON_2;
    }

    @Override
    public InterpretResponse execute(String containerId) throws DockerException, InterruptedException, IOException {
        final String[] command = {"python", ProgrammingLanguage.Python2.getFileName()};
        final ExecCreation execCreation = docker.execCreate(
                containerId,
                command,
                DockerClient.ExecCreateParam.attachStdout(), DockerClient.ExecCreateParam.attachStderr()
        );
        final LogStream output = docker.execStart(execCreation.id());

        OutputStream stdOut = new ContainerOutputThresholdingOutputStream(new ByteArrayOutputStream(), STDOUT_BYTE_LIMIT);
        OutputStream stdErr = new ContainerOutputThresholdingOutputStream(new ByteArrayOutputStream(), STDERR_BYTE_LIMIT);
        output.attach(stdOut, stdErr, true);
        return new InterpretResponse(stdOut.toString(), stdErr.toString());
    }
}
