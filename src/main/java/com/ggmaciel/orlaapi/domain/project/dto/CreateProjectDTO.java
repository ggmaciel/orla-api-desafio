package com.ggmaciel.orlaapi.domain.project.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.ggmaciel.orlaapi.helpers.ConstantHelper.INVALID_NAME_SIZE;

public class CreateProjectDTO {

    @NotNull(message = INVALID_NAME_SIZE)
    @Size(min =  1, max = 255, message = INVALID_NAME_SIZE)
    private String name;

    public CreateProjectDTO() {
    }

    public CreateProjectDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
