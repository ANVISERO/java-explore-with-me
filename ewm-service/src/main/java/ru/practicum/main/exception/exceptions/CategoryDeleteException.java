package ru.practicum.main.exception.exceptions;

public class CategoryDeleteException extends RuntimeException {
    public CategoryDeleteException(final String message) {
        super(message);
    }
}
