package ru.practicum.main.requests.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.main.requests.model.Request;
import ru.practicum.main.requests.model.RequestStatus;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {
    public RequestOutputDto requestToOutputRequestDto(final Request request) {
        return RequestOutputDto.builder()
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }

    public RequestOutputUpdateStatusDto requestListToUpdateStateList(final List<Request> requestList) {
        return RequestOutputUpdateStatusDto.builder()
                .confirmedRequests(requestList.stream()
                        .filter(request -> request.getStatus().equals(RequestStatus.CONFIRMED))
                        .map(RequestMapper::requestToOutputRequestDto)
                        .collect(Collectors.toList()))
                .rejectedRequests(requestList.stream()
                        .filter(request -> request.getStatus().equals(RequestStatus.REJECTED))
                        .map(RequestMapper::requestToOutputRequestDto)
                        .collect(Collectors.toList()))
                .build();


    }
}
