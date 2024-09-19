package ru.practicum.main.category.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryOutputDto {
    private Long id;

    private String name;
}
