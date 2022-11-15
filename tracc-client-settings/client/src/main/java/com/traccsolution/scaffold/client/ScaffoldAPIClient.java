package com.traccsolution.scaffold.client;

import com.traccsolution.scaffold.dto.parent.ParentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

/**
 *  the url property must be supplied by the service that uses this http client
 */
@FeignClient(name ="scaffold-api-client", url = "${scaffold.api.baseUrl}")
public interface ScaffoldAPIClient {
    @GetMapping(value = "/parent/{id}")
    List<ParentResponseDTO> getWorkBundles(@PathVariable("id") UUID id);
}
