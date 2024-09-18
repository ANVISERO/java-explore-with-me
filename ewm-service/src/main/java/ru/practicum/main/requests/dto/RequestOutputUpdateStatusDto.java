package ru.practicum.main.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestOutputUpdateStatusDto {
    private List<RequestOutputDto> confirmedRequests;

    private List<RequestOutputDto> rejectedRequests;
}
