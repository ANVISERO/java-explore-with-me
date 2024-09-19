package ru.practicum.main.exception.exceptions;

public class EventUpdateConflictException extends RuntimeException {
    public EventUpdateConflictException(final String message) {
        super(message);
    }
}
