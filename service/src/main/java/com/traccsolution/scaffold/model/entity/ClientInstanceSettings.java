package com.traccsolution.scaffold.model.entity;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.id.GUIDGenerator;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(schema = "client",name = "tbl_client_instance_settings")
public class ClientInstanceSettings {
    @Id
    @Column(name = "settingId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID settingId;

    @Column(name = "orgId")
    private UUID orgId;

    @Column(name = "instanceId")
    private UUID instanceId;

    @Column(name = "leQuota")
    private Integer leQuota;

    @Column(name = "seQuota")
    private String seQuota;

    @Column(name = "allowUserCreation")
    private Boolean allowUserCreation;

    @Column(name = "visibleToClients")
    private Boolean visibleToClients;

    @Column(name = "userLimit")
    private Integer userLimit;

    @Column(name = "enableCustomerSupport")
    private boolean enableCustomerSupport;

    @Column(name = "customSupportData")
    private String customSupportData;

    public ClientInstanceSettings(UUID orgId, UUID instanceId, Integer leQuota, String seQuota, Boolean allowUserCreation, Boolean visibleToClients, Integer userLimit, boolean enableCustomerSupport, String customSupportData) {
        this.orgId = orgId;
        this.instanceId = instanceId;
        this.leQuota = leQuota;
        this.seQuota = seQuota;
        this.allowUserCreation = allowUserCreation;
        this.visibleToClients = visibleToClients;
        this.userLimit = userLimit;
        this.enableCustomerSupport = enableCustomerSupport;
        this.customSupportData = customSupportData;
    }
}
