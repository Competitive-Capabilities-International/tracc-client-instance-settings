package com.traccsolution.scaffold.dto.parent;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author KeeshanReddy
 */
@Data
@NoArgsConstructor
public class ParentResponseDTO {

    @NotNull
    private UUID id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private UUID organisationId;

}
