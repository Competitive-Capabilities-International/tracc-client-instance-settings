package com.traccsolution.scaffold.extension.containers;

import org.testcontainers.containers.PostgreSQLContainer;

/**
 * @author KeeshanReddy
 * Singleton Redis TestContainer for use in Junit tests.
 * Given that application context is initialized once , only one container can be used as the Hiroku Datasource will be initialized just once
 * & referenced by Spring after startup when accessing the datasource again
 */
public enum PostgresTestContainer {
    INSTANCE;

    public static final String SPRING_DATASOURCE_URL = "spring.datasource.url";
    public static final String SPRING_DATASOURCE_USERNAME = "spring.datasource.username";
    public static final String SPRING_DATASOURCE_PASSWORD = "spring.datasource.password";
    public static final String USERNAME = "postgres";
    public static final String PASSWORD = "password";
    public static final int IN_CONTAINER_PORT = 5432;
    public static final String SCAFFOLD_APP_USER = "scaffold_app_user";
    public static final String SCAFFOLD_APP_USER_PASSWORD = "password";
    private PostgreSQLContainer postgreSQLContainer;

    PostgresTestContainer() {
                postgreSQLContainer  = new PostgreSQLContainer<>("postgres:13.4")
                .withDatabaseName("tracc")
                .withUsername(USERNAME)
                .withPassword(PASSWORD)
                .withExposedPorts(IN_CONTAINER_PORT)
                .withInitScript("pre-liquibase-init.sql");
        postgreSQLContainer.start();
        System.setProperty(SPRING_DATASOURCE_URL, postgreSQLContainer.getJdbcUrl());
        // use the microservice app user, & not the db super user from above
        System.setProperty(SPRING_DATASOURCE_USERNAME, SCAFFOLD_APP_USER);
        System.setProperty(SPRING_DATASOURCE_PASSWORD, SCAFFOLD_APP_USER_PASSWORD);
        org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PostgresTestContainer.class);
        log.info("postgres test container exposed at " + System.getProperty(SPRING_DATASOURCE_URL) + ", username=" + System.getProperty(SPRING_DATASOURCE_USERNAME) + ",password=", System.getProperty(PASSWORD));
    }

    public static PostgreSQLContainer initialize(){
        return INSTANCE.postgreSQLContainer;
    }
}
