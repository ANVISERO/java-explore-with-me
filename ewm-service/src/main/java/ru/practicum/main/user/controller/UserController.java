package ru.practicum.main.user.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.user.dto.UserInputDto;
import ru.practicum.main.user.dto.UserOutputDto;
import ru.practicum.main.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/admin/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserOutputDto saveUser(@RequestBody @Valid final UserInputDto userInputDto) {
        log.trace("POST request received to create user: {}", userInputDto);
        return userService.saveUser(userInputDto);
    }

    @GetMapping("/admin/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserOutputDto> getUsers(
            @RequestParam(required = false) final List<Long> ids,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero final Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive final Integer size) {
        log.trace("GET request received to find users");
        return userService.getUsers(ids, from, size);
    }

    @DeleteMapping("/admin/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable final Long userId) {
        log.trace("DELETE request received to delete users");
        userService.deleteUser(userId);
    }
}
