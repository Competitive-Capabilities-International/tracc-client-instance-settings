package com.traccsolution.tracc_client_instance_settings.exception;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.Map;

/**
 * This class can be used to indicate to consumers of this API that a RestResource was not found.
 * e.g.
 * <code>
 * RestResourceNotFoundException.builder()
 * .resourceName("work bundle area")
 * .filter("work bundle id",id.toString())
 * .build();
 * </code>
 */
@Data
@Builder
public class RestResourceNotFoundException extends Exception {
    private final String resourceName;
    @Singular
    // map for field names and values used for filter
    private final Map<String, String> filters;
}
