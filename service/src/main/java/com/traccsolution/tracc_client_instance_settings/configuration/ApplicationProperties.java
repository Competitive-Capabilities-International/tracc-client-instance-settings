package com.traccsolution.tracc_client_instance_settings.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Optional;

/**
 * <p>
 * Inject this into any other Spring Component to access application property values.
 * Add getter methods for properties you wish to access here which reads from Environment.
 * <b>DO NOT</b> use @Value to reference application properties from  Spring Beans that have the default scope (Singleton / Refresh)
 * as the Spring Actuator refresh endpoint will not refresh bean properties and will only refresh what is inside Environment.
 * Avoid returning primitive types or Intellij will warn you about a possible NullPointerException
 * If a property value is optional and may return null, return an Optional.ofNullable
 * or return false if it makes more sense for Boolean return types
 * <p/>
 * @author KeeshanReddy
 */
@Configuration
public class ApplicationProperties {

    private final Environment env;

    public ApplicationProperties(Environment env) {
        this.env = env;
    }

    public Boolean isLoggingRequestEnabled() {
        return env.getProperty("logging.request.enabled",Boolean.class,false);
    }

    public Boolean isLoggingRequestIncludeHeaders() {
        return env.getProperty("logging.request.headers.include",Boolean.class,false);
    }

    public Boolean isLoggingRequestIncludeQueryString() {
        return env.getProperty("logging.request.queryString.include",Boolean.class,false);
    }

    public Boolean isLoggingRequestIncludePayload() {
        return env.getProperty("logging.request.payload.include",Boolean.class,false);
    }

    public Optional<Integer> getLoggingRequestMaxPayloadLength() {
        return Optional.ofNullable(env.getProperty("logging.request.payload.maxlength",Integer.class));
    }

    public Integer getRedisPort(){
        return env.getProperty("spring.redis.port",Integer.class);
    }

    public String getRedisHost(){
        return env.getProperty("spring.redis.host");
    }

    public String getServletContextPath(){
        return env.getProperty("server.servlet.context-path");
    }

}
