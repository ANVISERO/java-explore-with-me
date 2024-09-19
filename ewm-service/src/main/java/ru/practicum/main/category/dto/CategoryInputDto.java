package ru.practicum.main.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public final class CategoryInputDto {
    @NotBlank
    @Size(max = 50)
    private String name;
}
