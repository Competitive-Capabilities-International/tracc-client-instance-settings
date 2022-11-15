package com.traccsolution.scaffold.dto;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.UUID;
@Getter
@Setter
public class ClientInstanceSettingsDto implements Serializable {
    private UUID settingId;
    private UUID orgId;
    private UUID instanceId;
    private Integer leQuota;
    private String seQuota;
    private Boolean allowUserCreation;
    private Boolean visibleToClients;
    private Integer userLimit;
    private boolean enableCustomerSupport;
    private String customSupportData;
    public ClientInstanceSettingsDto(UUID settingId, UUID orgId, UUID instanceId, Integer leQuota, String seQuota, Boolean allowUserCreation, Boolean visibleToClients, Integer userLimit, boolean enableCustomerSupport, String customSupportData) {
        this.settingId = settingId;
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
