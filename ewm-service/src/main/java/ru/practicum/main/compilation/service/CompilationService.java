package ru.practicum.main.compilation.service;


import ru.practicum.main.compilation.dto.CompilationInputDto;
import ru.practicum.main.compilation.dto.CompilationOutputDto;

import java.util.List;

public interface CompilationService {
    CompilationOutputDto createCompilation(final CompilationInputDto compilationInputDto);

    void deleteCompilation(final Long compId);

    CompilationOutputDto updateCompilation(final Long compId, final CompilationInputDto compilationInputDto);

    CompilationOutputDto searchCompilationById(final Long compId);

    List<CompilationOutputDto> searchCompilations(final Boolean pinned, final Integer from, final Integer size);
}
