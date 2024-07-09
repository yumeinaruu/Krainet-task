package org.krainet.tracker.model.dto.project;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectUpdateDescriptionDto {
    @NotNull
    private Long id;

    @NotNull
    private String description;
}
