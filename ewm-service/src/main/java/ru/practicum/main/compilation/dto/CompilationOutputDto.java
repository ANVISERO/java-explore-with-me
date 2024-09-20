package ru.practicum.main.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.event.dto.EventShortOutputDto;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class CompilationOutputDto {
    private List<EventShortOutputDto> events;

    private Boolean pinned;

    private String title;

    private Long id;
}
