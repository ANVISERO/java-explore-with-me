package ru.practicum.main.exception.exceptions;

public class UserIdConflictException extends RuntimeException {
    public UserIdConflictException(final String message) {
        super(message);
    }
}
