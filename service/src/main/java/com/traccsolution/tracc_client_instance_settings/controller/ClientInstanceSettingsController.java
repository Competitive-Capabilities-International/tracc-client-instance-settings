package com.traccsolution.tracc_client_instance_settings.controller;

import com.traccsolution.tracc_client_instance_settings.dto.ClientInstanceSettingsDto;
import com.traccsolution.tracc_client_instance_settings.exception.RestResourceNotFoundException;
import com.traccsolution.tracc_client_instance_settings.service.ClientInstanceSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author KeeshanReddy
 */
@RestController
@RequestMapping("/parent")
public class ClientInstanceSettingsController {
    private static final Logger log = LoggerFactory.getLogger(ClientInstanceSettingsDto.class);
    @Autowired
    private ClientInstanceSettingsService clientInstanceSettingsService;

    @RequestMapping(value = "getClientInstanceSettingsByOrgId", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<ClientInstanceSettingsDto> getClientInstanceSettingsByOrgId(@PathVariable UUID id) throws RestResourceNotFoundException {
        log.info("ParentController.getClientInstanceSettingsByOrgId");
        log.info("Client Instance Settings ORG ID: " + id);
        if (id != null) {
            ClientInstanceSettingsDto clientInstanceSettingsDto = clientInstanceSettingsService.getClientInstanceSettingsByOrgId(id);
            return new ResponseEntity<>(clientInstanceSettingsDto, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/createClientInstanceSettings", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Boolean> createInstanceSettings(@RequestBody ClientInstanceSettingsDto clientInstanceSettingsDto) throws Exception {
        log.info("ParentController.createClientInstanceSettings");
        log.info("Client Instance Settings Dto: " + clientInstanceSettingsDto);
        if (clientInstanceSettingsDto != null) {
            Boolean isCreated = clientInstanceSettingsService.createClientInstanceSettings(clientInstanceSettingsDto);
            return new ResponseEntity<>(isCreated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/updateClientInstanceSettings", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Boolean> updateInstanceSettings(@RequestBody ClientInstanceSettingsDto clientInstanceSettingsDto) throws Exception {
        log.info("ParentController.updateClientInstanceSettings");
        log.info("Client Instance Settings Dto: " + clientInstanceSettingsDto);
        if (clientInstanceSettingsDto != null) {
            Boolean isUpdated = clientInstanceSettingsService.updateClientInstanceSettings(clientInstanceSettingsDto);
            return new ResponseEntity<>(isUpdated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}


