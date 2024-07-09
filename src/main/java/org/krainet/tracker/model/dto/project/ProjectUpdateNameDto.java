package org.krainet.tracker.model.dto.project;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectUpdateNameDto {
    @NotNull
    private Long id;

    @NotNull
    @Size(min = 2, max = 50)
    private String name;
}
