package com.ggmaciel.orlaapi.domain.employee.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import static com.ggmaciel.orlaapi.helpers.ConstantHelper.ID_MUST_BE_A_VALID_NUMBER;

@Data
public class AddProjectDTO {

    @NotNull(message = ID_MUST_BE_A_VALID_NUMBER)
    @PositiveOrZero(message = ID_MUST_BE_A_VALID_NUMBER)
    private Long employeeId;

    @NotNull(message = ID_MUST_BE_A_VALID_NUMBER)
    @PositiveOrZero(message = ID_MUST_BE_A_VALID_NUMBER)
    private Long projectId;

    public AddProjectDTO(Long employeeId, Long projectId) {
        this.employeeId = employeeId;
        this.projectId = projectId;
    }
}
