package ru.practicum.main.validator.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.main.events.dto.EventInputDto;

import java.time.LocalDateTime;

public class CreateEventStartValidator implements ConstraintValidator<EventStartBefore, EventInputDto> {
    EventStartBefore check;

    @Override
    public void initialize(final EventStartBefore check) {
        this.check = check;
    }

    @Override
    public boolean isValid(final EventInputDto eventInputDto, final ConstraintValidatorContext ctx) {
        return (eventInputDto.getEventDate() != null &&
                eventInputDto.getEventDate().isAfter(LocalDateTime.now().plusHours(check.min())));
    }
}
