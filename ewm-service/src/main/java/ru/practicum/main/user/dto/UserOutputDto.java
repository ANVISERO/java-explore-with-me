package ru.practicum.main.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class UserOutputDto {
    private String name;

    private String email;

    private Long id;
}
