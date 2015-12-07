package com.expercise.interpreter.runtime;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerCertificateException;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.PortBinding;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DockerHelper {

    // TODO ufuk: log and handle the exceptions

    private static final String INTERPRETER_IMAGE_NAME = "expercise/interpreter";

    private static final int SECONDS_TO_WAIT_BEFORE_KILLING = 3;

    private static final int EXPOSED_PORT = 4567;

    private static final Long MEMORY_CONSTRAINT = 128_000_000L;

    private static DockerClient docker;

    static {
        try {
            docker = DefaultDockerClient.fromEnv().build();
        } catch (DockerCertificateException e) {
            e.printStackTrace();
        }
    }

    // TODO ufuk: add other constraints (such as CPU, network, IO)
    public static String runNewContainer(int hostPort) {
        try {
            Map<String, List<PortBinding>> portBindings = new HashMap<>();
            portBindings.put(String.valueOf(EXPOSED_PORT), Collections.singletonList(PortBinding.of("0.0.0.0", hostPort)));
            HostConfig hostConfig = HostConfig.builder()
                    .portBindings(portBindings)
                    .memory(MEMORY_CONSTRAINT)
                    .build();

            ContainerConfig containerConfig = ContainerConfig.builder()
                    .image(INTERPRETER_IMAGE_NAME)
                    .exposedPorts(new String[]{String.valueOf(EXPOSED_PORT)})
                    .hostConfig(hostConfig)
                    .build();

            ContainerCreation containerCreation = docker.createContainer(containerConfig);
            docker.startContainer(containerCreation.id());
            return containerCreation.id();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void destroyContainer(String containerId) {
        try {
            docker.stopContainer(containerId, SECONDS_TO_WAIT_BEFORE_KILLING);
            docker.removeContainer(containerId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
