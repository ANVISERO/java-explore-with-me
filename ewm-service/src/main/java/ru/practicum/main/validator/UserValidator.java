package ru.practicum.main.validator;

import lombok.experimental.UtilityClass;
import ru.practicum.main.exception.exceptions.NotFoundException;
import ru.practicum.main.user.repository.UserRepository;

@UtilityClass
public class UserValidator {
    public void checkUserExist(final UserRepository userRepository, final Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id %d does not exist", userId));
        }
    }
}
