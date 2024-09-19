package ru.practicum.main.events.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.events.dto.EventFullOutputDto;
import ru.practicum.main.events.dto.EventInputDto;
import ru.practicum.main.events.dto.EventShortOutputDto;
import ru.practicum.main.events.dto.UpdateEventDto;
import ru.practicum.main.events.service.EventsService;
import ru.practicum.main.validator.annotation.EventStartBefore;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserEventsController {
    private final EventsService eventsService;

    @GetMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortOutputDto> getUserEvents(
            @PathVariable final Long userId,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero final Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive final Integer size) {
        log.trace("GET request to get events created user with id: {}", userId);
        return eventsService.getUserEvents(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullOutputDto addEvent(
            @PathVariable final Long userId,
            @RequestBody @EventStartBefore(min = 2) @Valid final EventInputDto eventInputDto) {
        log.trace("POST request to create user: {}", eventInputDto);
        return eventsService.addEvent(userId, eventInputDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullOutputDto getEventById(@PathVariable final Long userId,
                                           @PathVariable final Long eventId) {
        log.trace("GET request to find event with id: {}", eventId);
        return eventsService.getUserEventById(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullOutputDto updateEvent(@PathVariable final Long userId,
                                          @PathVariable final Long eventId,
                                          @RequestBody @EventStartBefore(min = 2) @Valid final UpdateEventDto updateEventDto) {
        log.trace("PATCH request to update event: {}", updateEventDto);
        return eventsService.updateEvent(userId, eventId, updateEventDto);
    }
}
