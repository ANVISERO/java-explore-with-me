package ru.practicum.main.compilation.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.compilation.dto.CompilationOutputDto;
import ru.practicum.main.compilation.service.CompilationService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class CompilationPublicController {
    private final CompilationService compilationService;

    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationOutputDto searchCompilationById(@PathVariable final Long compId) {
        log.debug("GET request received to find compilation with id = {}", compId);
        return compilationService.searchCompilationById(compId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationOutputDto> searchCompilation(
            @RequestParam(required = false) final Boolean pinned,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero final Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive final Integer size) {
        log.debug("GET request received to find all suitable events");
        return compilationService.searchCompilation(pinned, from, size);
    }
}
