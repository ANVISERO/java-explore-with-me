package ru.practicum.stats.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class HitDto {
    @NotBlank(message = "App field should not be empty")
    private String app;

    @NotBlank(message = "URI field should not be empty")
    private String uri;

    @NotBlank(message = "Ip field should not be empty")
    private String ip;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.DATE_TIME_PATTERN)
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
}
