package ru.practicum.stats.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.practicum.stats.dto.model.HitDto;
import ru.practicum.stats.dto.model.StatsDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class Client {

    private final RestClient restClient = RestClient.builder()
            .baseUrl("http://stats-server:9090")
            .build();

    public void saveHit(final HitDto hitDto) {
        restClient.post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .body(hitDto)
                .retrieve()
                .toBodilessEntity();
    }

    public List<StatsDto> getStats(final LocalDateTime start, final LocalDateTime end,
                                   final List<String> uris, final Boolean unique) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
