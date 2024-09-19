package ru.practicum.main.exception.exceptions;

public class EventPatchConflictException extends RuntimeException {
    public EventPatchConflictException(final String message) {
        super(message);
    }
}
