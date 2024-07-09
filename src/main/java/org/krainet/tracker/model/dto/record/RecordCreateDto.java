package org.krainet.tracker.model.dto.record;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.krainet.tracker.model.dto.project.ProjectForRecordDto;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordCreateDto {
    @NotNull
    private Timestamp started;

    private Timestamp deadline;

    @NotNull
    private ProjectForRecordDto project;
}
