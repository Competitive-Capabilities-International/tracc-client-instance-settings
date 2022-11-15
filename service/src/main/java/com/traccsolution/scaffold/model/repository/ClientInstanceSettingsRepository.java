package com.traccsolution.scaffold.model.repository;

import com.traccsolution.scaffold.model.entity.ClientInstanceSettings;
import org.hibernate.id.GUIDGenerator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientInstanceSettingsRepository extends JpaRepository<ClientInstanceSettings,UUID> {

    public ClientInstanceSettings findByOrgId(UUID orgId);

    public ClientInstanceSettings findBysettingId(UUID settingId);
}
