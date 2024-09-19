package ru.practicum.main.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.main.exception.exceptions.CategoryDeleteException;
import ru.practicum.main.exception.exceptions.CategoryUniqueNameException;
import ru.practicum.main.exception.exceptions.EventUpdateConflictException;
import ru.practicum.main.exception.exceptions.EventStateConflictException;
import ru.practicum.main.exception.exceptions.EventTimeException;
import ru.practicum.main.exception.exceptions.NotFoundException;
import ru.practicum.main.exception.exceptions.RequestCreatedConflictException;
import ru.practicum.main.exception.exceptions.UserEmailConflictException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEmailException(final UserEmailConflictException e) {
        return sendErrorResponse(e);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDeleteCategoryException(final CategoryDeleteException e) {
        return sendErrorResponse(e);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleCategoryNameException(final CategoryUniqueNameException e) {
        return sendErrorResponse(e);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEventStateConflictException(final EventStateConflictException e) {
        return sendErrorResponse(e);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleRequestCreatedConflictException(final RequestCreatedConflictException e) {
        return sendErrorResponse(e);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEventPatchConflictException(final EventUpdateConflictException e) {
        return sendErrorResponse(e);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotExistsException(final NotFoundException e) {
        return sendErrorResponse(e);
    }

    @ExceptionHandler({ValidationException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validException(final ValidationException e) {
        return sendErrorResponse(e);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse timeException(final EventTimeException e) {
        return sendErrorResponse(e);
    }

    private ErrorResponse sendErrorResponse(final Exception e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @Getter
    @AllArgsConstructor
    private class ErrorResponse {
        private String error;
    }
}
