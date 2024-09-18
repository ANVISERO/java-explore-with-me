package ru.practicum.main.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.events.dto.ShortOutputEventDto;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationOutputDto {
    private List<ShortOutputEventDto> events;

    private Boolean pinned;

    private String title;

    private Long id;
}
