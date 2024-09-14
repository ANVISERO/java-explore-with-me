package ru.practicum.stats.client;

import ru.practicum.stats.dto.model.HitDto;
import ru.practicum.stats.dto.model.StatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface Client {

    void saveHit(final HitDto hitDto);

    List<StatsDto> getStats(final LocalDateTime start, final LocalDateTime end,
                            final List<String> uris, final Boolean unique);
}
