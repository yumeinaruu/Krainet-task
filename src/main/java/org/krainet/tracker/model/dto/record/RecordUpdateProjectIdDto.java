package org.krainet.tracker.model.dto.record;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.krainet.tracker.model.dto.project.ProjectForRecordDto;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordUpdateProjectIdDto {
    @NotNull
    private Long id;

    @NotNull
    private ProjectForRecordDto project;
}
