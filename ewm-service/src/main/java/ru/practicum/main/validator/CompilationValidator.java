package ru.practicum.main.validator;

import lombok.experimental.UtilityClass;
import ru.practicum.main.compilation.repository.CompilationRepository;
import ru.practicum.main.exception.exceptions.NotFoundException;

@UtilityClass
public class CompilationValidator {
    public void checkCompilationExist(final CompilationRepository compilationRepository, final Long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException(String.format("Compilation with id = %d does not exist", compId));
        }
    }
}
