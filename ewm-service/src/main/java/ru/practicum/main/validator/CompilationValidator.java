package ru.practicum.main.validator;

import lombok.experimental.UtilityClass;
import ru.practicum.main.compilation.repository.CompilationRepo;
import ru.practicum.main.exception.exceptions.NotFoundException;

@UtilityClass
public class CompilationValidator {
    public void checkCompilationExist(final CompilationRepo compilationRepo, final Long compId) {
        if (!compilationRepo.existsById(compId)) {
            throw new NotFoundException(String.format("Compilation with id = %d does not exist", compId));
        }
    }
}
