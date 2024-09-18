package ru.practicum.main.exception.exceptions;

public class UserEmailConflictException extends RuntimeException {

    public UserEmailConflictException(final String message) {
        super(message);
    }
}
