package ru.practicum.main.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;
import ru.practicum.main.comment.model.CommentStatus;
import ru.practicum.main.user.dto.UserOutputDto;

import java.time.LocalDateTime;

@Data
@Builder
public final class CommentOutputDto {
    private Long id;

    @NotBlank
    private String text;

    @NotNull
    private LocalDateTime created;

    @NotNull
    private UserOutputDto author;

    @PositiveOrZero
    private Long eventId;

    private CommentStatus status;
}