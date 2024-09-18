package ru.practicum.main.user.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.main.user.model.User;

@UtilityClass
public class UserMapper {
    public User UserInputDtoToUser(final UserInputDto userInputDto) {
        return User.builder()
                .name(userInputDto.getName())
                .email(userInputDto.getEmail())
                .build();
    }

    public UserOutputDto UserToOutputUserDto(final User user) {
        return UserOutputDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .id(user.getId())
                .build();
    }
}
