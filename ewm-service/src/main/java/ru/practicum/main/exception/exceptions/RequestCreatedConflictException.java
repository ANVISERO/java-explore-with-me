package ru.practicum.main.exception.exceptions;

public class RequestCreatedConflictException extends RuntimeException {
    public RequestCreatedConflictException(final String message) {
        super(message);
    }
}
