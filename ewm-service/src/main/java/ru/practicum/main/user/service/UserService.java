package ru.practicum.main.user.service;


import ru.practicum.main.user.dto.UserInputDto;
import ru.practicum.main.user.dto.UserOutputDto;

import java.util.List;

public interface UserService {
    UserOutputDto saveUser(final UserInputDto userInputDto);

    List<UserOutputDto> getUsers(final List<Long> ids, final Integer from, final Integer size);

    void deleteUser(final Long userId);
}
