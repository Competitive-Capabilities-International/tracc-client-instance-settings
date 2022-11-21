package com.traccsolution.tracc_client_instance_settings.model.repository;
import com.traccsolution.tracc_client_instance_settings.model.entity.ClientInstanceSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientInstanceSettingsRepository extends JpaRepository<ClientInstanceSettings,UUID> {

    public ClientInstanceSettings findByOrgId(UUID orgId);

    public ClientInstanceSettings findBysettingId(UUID settingId);
}
