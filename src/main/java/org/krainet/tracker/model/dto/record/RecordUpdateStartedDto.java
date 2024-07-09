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
public class RecordUpdateStartedDto {
    @NotNull
    private Long id;

    @NotNull
    private Timestamp started;
}
