package com.ggmaciel.orlaapi.domain.project.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.ggmaciel.orlaapi.helpers.ConstantHelper.INVALID_NAME_SIZE;

@Data
@NoArgsConstructor
public class CreateProjectDTO {

    @NotNull(message = INVALID_NAME_SIZE)
    @Size(min =  1, max = 255, message = INVALID_NAME_SIZE)
    private String name;

    public CreateProjectDTO(String name) {
        this.name = name;
    }
}
