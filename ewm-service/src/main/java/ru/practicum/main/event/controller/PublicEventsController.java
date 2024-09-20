package ru.practicum.main.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.event.dto.EventFullOutputDto;
import ru.practicum.main.event.dto.EventShortOutputDto;
import ru.practicum.main.event.service.EventsService;
import ru.practicum.stats.dto.model.Constant;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventsController {
    private final EventsService eventsService;
    private final HttpServletRequest servletRequest;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortOutputDto> publicSearchEvents(
            @RequestParam(required = false) final String text,
            @RequestParam(required = false) final List<Long> categories,
            @RequestParam(required = false) final Boolean paid,
            @RequestParam(name = "rangeStart", required = false)
            @DateTimeFormat(pattern = Constant.DATE_TIME_PATTERN) final LocalDateTime rangeStart,
            @RequestParam(name = "rangeEnd", required = false)
            @DateTimeFormat(pattern = Constant.DATE_TIME_PATTERN) final LocalDateTime rangeEnd,
            @RequestParam(required = false) final Boolean onlyAvailable,
            @RequestParam(required = false) final String sort,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero final Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive final Integer size) {
        log.trace("GET request received to find all events with filters and text: {}", text);
        return eventsService.publicSearchEvents(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size, servletRequest.getRemoteAddr());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullOutputDto getEventById(@PathVariable final Long id) {
        log.trace("GET request received to find event with id = {}", id);
        return eventsService.getEventById(id, servletRequest.getRemoteAddr());
    }
}
