package com.traccsolution.scaffold.service;

import com.amazonaws.services.xray.model.InvalidRequestException;
import com.traccsolution.scaffold.dto.ClientInstanceSettingsDto;
import com.traccsolution.scaffold.model.entity.ClientInstanceSettings;
import com.traccsolution.scaffold.model.repository.ClientInstanceSettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.ion.NullValueException;

import java.util.UUID;

@Service
public class ClientInstanceSettingsService {

    private static final Logger log = LoggerFactory.getLogger(ClientInstanceSettingsService.class);

    @Autowired
    private ClientInstanceSettingsRepository clientInstanceSettingsRepository;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean createClientInstanceSettings(ClientInstanceSettingsDto clientInstanceSettingsDto) {
        Boolean isCreated = false;
        if (clientInstanceSettingsDto.getOrgId() == null) {
            log.info("TRYING TO CREATE INSTANCE SETTINGS BUT ORG ID IS NULL");
            throw new InvalidRequestException("Org ID cannot be null");
        }
        try {
            ClientInstanceSettings clientInstanceSettings = new ClientInstanceSettings(
                    clientInstanceSettingsDto.getOrgId(), clientInstanceSettingsDto.getInstanceId(), clientInstanceSettingsDto.getLeQuota(),
                    clientInstanceSettingsDto.getSeQuota(), clientInstanceSettingsDto.getAllowUserCreation(),
                    clientInstanceSettingsDto.getVisibleToClients(), clientInstanceSettingsDto.getUserLimit(),
                    clientInstanceSettingsDto.isEnableCustomerSupport(), clientInstanceSettingsDto.getCustomSupportData());

            clientInstanceSettingsRepository.saveAndFlush(clientInstanceSettings);
        } catch (Exception e) {
            log.info("AN ERROR OCCURED: " + e.getMessage());
        }
        return isCreated;
    }

    public ClientInstanceSettingsDto getClientInstanceSettingsByOrgId(UUID id) {
        try {
            ClientInstanceSettings clientInstanceSettings = clientInstanceSettingsRepository.findByOrgId(id);
            ClientInstanceSettingsDto clientSettingsDto = new ClientInstanceSettingsDto(clientInstanceSettings.getSettingId(), id,
                    clientInstanceSettings.getInstanceId(),
                    clientInstanceSettings.getLeQuota(), clientInstanceSettings.getSeQuota(), clientInstanceSettings.getAllowUserCreation(),
                    clientInstanceSettings.getVisibleToClients(), clientInstanceSettings.getUserLimit(), clientInstanceSettings.isEnableCustomerSupport(),
                    clientInstanceSettings.getCustomSupportData());
            return clientSettingsDto;
        } catch (Exception e) {
            log.info("AN ERROR OCCURRED: " + e.getMessage());
            throw new NullValueException();
        }
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean updateClientInstanceSettings(ClientInstanceSettingsDto clientInstanceSettingsDto) {
        Boolean isCreated = false;
        if (clientInstanceSettingsDto.getOrgId() == null) {
            throw new InvalidRequestException("Org ID cannot be null");
        }

        try {
            ClientInstanceSettings clientInstanceSettings = clientInstanceSettingsRepository.findByOrgId(clientInstanceSettingsDto.getSettingId());
            if (clientInstanceSettings != null) {
                ClientInstanceSettings InstanceSettings = new ClientInstanceSettings(
                        clientInstanceSettingsDto.getOrgId(), clientInstanceSettingsDto.getInstanceId(), clientInstanceSettingsDto.getLeQuota(),
                        clientInstanceSettingsDto.getSeQuota(), clientInstanceSettingsDto.getAllowUserCreation(),
                        clientInstanceSettingsDto.getVisibleToClients(), clientInstanceSettingsDto.getUserLimit(),
                        clientInstanceSettingsDto.isEnableCustomerSupport(), clientInstanceSettingsDto.getCustomSupportData());

                clientInstanceSettingsRepository.saveAndFlush(InstanceSettings);
                isCreated = true;
            }

        } catch (Exception e) {
            log.info("AN ERROR OCCURRED: " + e.getMessage());
        }
        return isCreated;
    }
}
