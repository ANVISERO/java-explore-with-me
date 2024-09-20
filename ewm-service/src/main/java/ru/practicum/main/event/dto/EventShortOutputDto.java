package ru.practicum.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.category.dto.CategoryOutputDto;
import ru.practicum.main.user.dto.UserOutputDto;
import ru.practicum.stats.dto.model.Constant;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class EventShortOutputDto {
    private String annotation;

    private CategoryOutputDto category;

    private Long confirmedRequests;

    private Long id;

    @JsonFormat(pattern = Constant.DATE_TIME_PATTERN)
    private LocalDateTime eventDate;

    private UserOutputDto initiator;

    private Boolean paid;

    private String title;

    private Long views;
}
