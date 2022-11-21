package com.traccsolution.tracc_client_instance_settings.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

/**
 *  the url property must be supplied by the service that uses this http client
 */
//@FeignClient(name ="tracc_client_instance_settings-api-client", url = "${tracc_client_instance_settings.api.baseUrl}")
//public interface tracc_client_instance_settingsAPIClient {
//    @GetMapping(value = "/parent/{id}")
//    List<ParentResponseDTO> getWorkBundles(@PathVariable("id") UUID id);
//}
