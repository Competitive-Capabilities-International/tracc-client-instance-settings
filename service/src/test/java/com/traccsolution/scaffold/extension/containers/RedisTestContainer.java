package com.traccsolution.scaffold.extension.containers;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * @author KeeshanReddy
 * Singleton Redis TestContainer for use in Junit tests
 */
public enum RedisTestContainer {
    INSTANCE;

    public static final String REDIS_PORT = "spring.redis.port";
    public static final String REDIS_HOST = "spring.redis.host";
    public static final String LOCALHOST = "localhost";
    public static final String IMAGE_NAME = "redis:6.2.6";
    public static final int CONTAINER_PORT = 6379;

    private GenericContainer container;

    /**
     * This constructor needs to run before the spring app context is initialized
     */
    RedisTestContainer() {
        container = new GenericContainer<>(DockerImageName.parse(IMAGE_NAME));
        container.withExposedPorts(CONTAINER_PORT)
                .start();
        String redisHostPort = String.valueOf(container.getMappedPort(CONTAINER_PORT));
        System.setProperty(REDIS_PORT, redisHostPort);
        System.setProperty(REDIS_HOST, LOCALHOST);
        org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RedisTestContainer.class);
        log.info("redis test container exposed at " + System.getProperty(REDIS_HOST) + ":" + System.getProperty(REDIS_PORT));
    }

    public static GenericContainer initialize() {
        return INSTANCE.container;
    }
}
