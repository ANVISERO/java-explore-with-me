package ru.practicum.main.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.exception.exceptions.UserEmailConflictException;
import ru.practicum.main.user.dto.UserInputDto;
import ru.practicum.main.user.dto.UserMapper;
import ru.practicum.main.user.dto.UserOutputDto;
import ru.practicum.main.user.repository.UserCriteria;
import ru.practicum.main.user.repository.UserRepo;
import ru.practicum.main.user.repository.UserSpecification;
import ru.practicum.main.validator.UserValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;

    @Override
    @Transactional
    public UserOutputDto saveUser(final UserInputDto userInputDto) {
        if (userRepo.existsByEmail(userInputDto.getEmail())) {
            throw new UserEmailConflictException("Email already used");
        }
        return UserMapper.UserToOutputUserDto(userRepo.save(UserMapper.UserInputDtoToUser(userInputDto)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserOutputDto> getUsers(final List<Long> ids, final Integer from, final Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        UserCriteria criteria = UserCriteria.builder()
                .ids(ids)
                .build();
        UserSpecification userSpecification = new UserSpecification(criteria);
        return userRepo.findAll(userSpecification, pageable).stream()
                .map(UserMapper::UserToOutputUserDto)
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public void deleteUser(final Long userId) {
        UserValidator.checkUserExist(userRepo, userId);
        userRepo.deleteById(userId);
    }
}
