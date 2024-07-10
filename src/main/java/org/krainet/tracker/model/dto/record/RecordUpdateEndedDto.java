package org.krainet.tracker.model.dto.record;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordUpdateEndedDto {
    @NotNull
    private Long id;

    private Timestamp ended;
}
