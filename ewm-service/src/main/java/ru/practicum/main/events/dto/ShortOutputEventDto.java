package ru.practicum.main.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.category.dto.CategoryOutputDto;
import ru.practicum.main.user.dto.UserOutputDto;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortOutputEventDto {
    private String annotation;

    private CategoryOutputDto category;

    private Long confirmedRequests;

    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private UserOutputDto initiator;

    private Boolean paid;

    private String title;

    private Long views;
}
