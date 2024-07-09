package org.krainet.tracker.model.dto.project;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.krainet.tracker.model.dto.user.UserForOtherClassesDto;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectUpdateUserDto {
    @NotNull
    private Long id;

    private UserForOtherClassesDto user;
}
