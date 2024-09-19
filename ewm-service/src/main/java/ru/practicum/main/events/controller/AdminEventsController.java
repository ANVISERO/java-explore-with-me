package ru.practicum.main.events.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.events.dto.EventFullOutputDto;
import ru.practicum.main.events.dto.UpdateEventDto;
import ru.practicum.main.events.service.EventsService;
import ru.practicum.main.validator.annotation.EventStartBefore;
import ru.practicum.stats.dto.model.Constant;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminEventsController {
    private final EventsService eventsService;

    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullOutputDto> adminSearchEvents(
            @RequestParam(required = false) final List<Long> users,
            @RequestParam(required = false) final List<String> states,
            @RequestParam(required = false) final List<Long> categories,
            @RequestParam(name = "rangeStart", required = false)
            @DateTimeFormat(pattern = Constant.DATE_TIME_PATTERN) final LocalDateTime rangeStart,
            @RequestParam(name = "rangeEnd", required = false)
            @DateTimeFormat(pattern = Constant.DATE_TIME_PATTERN) final LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero final Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive final Integer size) {
        log.debug("GET request received to find all events by filters");
        return eventsService.adminSearchEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullOutputDto adminUpdateEvent(@PathVariable final Long eventId,
                                               @RequestBody @EventStartBefore @Valid final UpdateEventDto updateEventDto) {
        log.debug("PATCH request received to update event with id = {}: {}", eventId, updateEventDto);
        return eventsService.adminUpdateEvent(eventId, updateEventDto);
    }
}
