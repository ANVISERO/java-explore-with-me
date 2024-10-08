package ru.practicum.main.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.request.model.RequestStatus;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class RequestUpdateInputStatusDto {
    private List<Long> requestIds;

    private RequestStatus status;
}
