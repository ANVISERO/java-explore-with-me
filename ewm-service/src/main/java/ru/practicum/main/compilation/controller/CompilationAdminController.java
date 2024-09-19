package ru.practicum.main.compilation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.compilation.dto.CompilationInputDto;
import ru.practicum.main.compilation.dto.CompilationOutputDto;
import ru.practicum.main.compilation.service.CompilationService;
import ru.practicum.main.validator.annotation.ValidationGroups;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(ValidationGroups.Create.class)
    public CompilationOutputDto createCompilation(@RequestBody @Valid final CompilationInputDto compilationInputDto) {
        log.debug("POST request received to create compilation: {}", compilationInputDto);
        return compilationService.createCompilation(compilationInputDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable final Long compId) {
        log.debug("DELETE request received to delete compilation with id = {}", compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    @Validated(ValidationGroups.Update.class)
    public CompilationOutputDto updateCompilation(@PathVariable final Long compId,
                                                  @RequestBody @Valid final CompilationInputDto compilationInputDto) {
        log.debug("PATCH request received to update compilation with id = {}", compId);
        return compilationService.updateCompilation(compId, compilationInputDto);
    }
}
