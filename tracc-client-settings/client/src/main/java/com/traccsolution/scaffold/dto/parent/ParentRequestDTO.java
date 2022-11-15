package com.traccsolution.scaffold.dto.parent;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author KeeshanReddy
 */
@Data
@Builder
public class ParentRequestDTO {

    @NotBlank
    private String name;
    @NotBlank
    private String description;

    @NotNull
    private UUID organisationId;

}
