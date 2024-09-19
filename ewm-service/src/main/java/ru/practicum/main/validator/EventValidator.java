package ru.practicum.main.validator;

import lombok.experimental.UtilityClass;
import ru.practicum.main.events.repository.EventRepo;
import ru.practicum.main.exception.exceptions.EventTimeException;
import ru.practicum.main.exception.exceptions.NotFoundException;

import java.time.LocalDateTime;

@UtilityClass
public class EventValidator {
    public void checkEventExist(final EventRepo eventRepo, final Long eventId) {
        if (!eventRepo.existsById(eventId)) {
            throw new NotFoundException(String.format("Event with id %d does not exist", eventId));
        }
    }

    public void checkEventStartTime(final LocalDateTime rangeStart, final LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null && rangeEnd.isBefore(rangeStart)) {
            throw new EventTimeException("Start time can not be after end time");
        }
    }
}
