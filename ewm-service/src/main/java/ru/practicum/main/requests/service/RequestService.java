package ru.practicum.main.requests.service;


import ru.practicum.main.requests.dto.RequestOutputDto;
import ru.practicum.main.requests.dto.RequestOutputUpdateStatusDto;
import ru.practicum.main.requests.dto.RequestUpdateInputStatusDto;

import java.util.List;

public interface RequestService {
    RequestOutputDto createRequest(final Long userId, final Long eventId);

    List<RequestOutputDto> findUserRequests(final Long userId);

    RequestOutputDto repealRequest(final Long userId, final Long requestId);

    List<RequestOutputDto> userParticipatesInEvent(final Long userId, final Long eventId);

    RequestOutputUpdateStatusDto changeRequestStatus(final Long userId, final Long eventId,
                                                     final RequestUpdateInputStatusDto updateState);
}