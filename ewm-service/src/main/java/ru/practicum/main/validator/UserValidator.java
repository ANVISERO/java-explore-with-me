package ru.practicum.main.validator;

import lombok.experimental.UtilityClass;
import ru.practicum.main.exception.exceptions.NotFoundException;
import ru.practicum.main.user.repository.UserRepo;

@UtilityClass
public class UserValidator {
    public void checkUserExist(final UserRepo userRepo, final Long userId) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException(String.format("User with id %d does not exist", userId));
        }
    }
}
