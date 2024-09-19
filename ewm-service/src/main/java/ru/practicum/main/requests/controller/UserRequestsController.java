package ru.practicum.main.requests.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.requests.dto.RequestOutputDto;
import ru.practicum.main.requests.dto.RequestOutputUpdateStatusDto;
import ru.practicum.main.requests.dto.RequestUpdateInputStatusDto;
import ru.practicum.main.requests.service.RequestService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserRequestsController {
    private final RequestService requestService;

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestOutputDto createRequest(@PathVariable final Long userId,
                                          @RequestParam final Long eventId) {
        log.trace("POST request received to create request from user with id = {}", userId);
        return requestService.createRequest(userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestOutputDto> getUserRequest(@PathVariable final Long userId) {
        log.trace("GET request received to find user requests from id = {}", userId);
        return requestService.findUserRequests(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public RequestOutputDto cancelRequest(@PathVariable final Long userId,
                                          @PathVariable final Long requestId) {
        log.trace("PATCH request received to repeal user request from id = {}", userId);
        return requestService.cancelRequest(userId, requestId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestOutputDto> userParticipatesInEvent(@PathVariable final Long userId,
                                                          @PathVariable final Long eventId) {
        log.trace("GET request received to find event were user participated with id = {}", userId);
        return requestService.userParticipatesInEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public RequestOutputUpdateStatusDto changeRequestStatus(
            @PathVariable final Long userId,
            @PathVariable final Long eventId,
            @RequestBody(required = false) final RequestUpdateInputStatusDto updateState) {
        log.trace("PATCH request received to change requests state: {}", updateState);
        return requestService.changeRequestStatus(userId, eventId, updateState);
    }


}
