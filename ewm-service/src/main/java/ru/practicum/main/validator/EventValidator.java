package ru.practicum.main.validator;

import lombok.experimental.UtilityClass;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.exception.exceptions.EventTimeException;
import ru.practicum.main.exception.exceptions.NotFoundException;

import java.time.LocalDateTime;

@UtilityClass
public class EventValidator {
    public void checkEventExist(final EventRepository eventRepository, final Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException(String.format("Event with id %d does not exist", eventId));
        }
    }

    public void checkEventStartTime(final LocalDateTime rangeStart, final LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null && rangeEnd.isBefore(rangeStart)) {
            throw new EventTimeException("Start time can not be after end time");
        }
    }
}
