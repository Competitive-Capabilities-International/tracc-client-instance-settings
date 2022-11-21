package com.traccsolution.tracc_client_instance_settings.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * @author KeeshanReddy
 * see <a href= "https://www.baeldung.com/spring-http-logging#1-configure-spring-boot-application">Spring – Log Incoming Requests</a>
 */
@Configuration
@Data
public class RequestLoggingConfig extends CommonsRequestLoggingFilter {

    public static final String REQUEST_RECEIVED = "Request received ";
    public static final String REQUEST_PROCESSED = "Request processed ";
    private boolean shouldLogRequests = false;

    private ApplicationProperties applicationProperties;


    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        RequestLoggingConfig loggingFilter = new RequestLoggingConfig();
        loggingFilter.setIncludeQueryString(applicationProperties.isLoggingRequestIncludeQueryString());
        loggingFilter.setIncludeHeaders(applicationProperties.isLoggingRequestIncludeHeaders());
        loggingFilter.setShouldLogRequests(applicationProperties.isLoggingRequestEnabled());
        applicationProperties.getLoggingRequestMaxPayloadLength().ifPresent(loggingFilter::setMaxPayloadLength);
        loggingFilter.setIncludePayload(applicationProperties.isLoggingRequestIncludePayload());
        return loggingFilter;
    }

    @Autowired
    private void setApplicationProperties(ApplicationProperties applicationProperties){
        this.applicationProperties = applicationProperties;
    }

    public RequestLoggingConfig() {
        setBeforeMessagePrefix(REQUEST_RECEIVED);
        setAfterMessagePrefix(REQUEST_PROCESSED);
        setIncludeQueryString(true);
    }


    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return shouldLogRequests;
    }

    /**
     * Writes a log message before the request is processed.
     */
    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        logger.info(message);
    }

    /**
     * Writes a log message after the request is processed.
     */
    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        logger.info(message);
    }
}
