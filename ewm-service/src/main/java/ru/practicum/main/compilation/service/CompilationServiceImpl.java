package ru.practicum.main.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.compilation.dto.CompilationInputDto;
import ru.practicum.main.compilation.dto.CompilationMapper;
import ru.practicum.main.compilation.dto.CompilationOutputDto;
import ru.practicum.main.compilation.model.Compilation;
import ru.practicum.main.compilation.repository.CompilationCriteria;
import ru.practicum.main.compilation.repository.CompilationRepo;
import ru.practicum.main.compilation.repository.CompilationSpecification;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.events.repository.EventRepo;
import ru.practicum.main.exception.exceptions.NotFoundException;
import ru.practicum.main.validator.CompilationValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final EventRepo eventRepo;
    private final CompilationRepo compilationRepo;

    @Override
    @Transactional
    public CompilationOutputDto createCompilation(final CompilationInputDto compilationInputDto) {
        List<Event> events = eventRepo.findAllByIdIn(compilationInputDto.getEvents());
        Compilation compilation = CompilationMapper.compilationInputDtoToCompilation(compilationInputDto, events);
        return CompilationMapper.compilationToCompilationOutputDto(compilationRepo.save(compilation));
    }

    @Override
    @Transactional
    public void deleteCompilation(final Long compId) {
        CompilationValidator.checkCompilationExist(compilationRepo, compId);
        compilationRepo.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationOutputDto updateCompilation(final Long compId, final CompilationInputDto compilationInputDto) {
        Compilation updateComp = compilationRepo.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id %d does not exist"));
        List<Event> events = eventRepo.findAllByIdIn(compilationInputDto.getEvents());
        updateComp = CompilationMapper.updateCompilation(compilationInputDto, updateComp, events);
        return CompilationMapper.compilationToCompilationOutputDto(compilationRepo.save(updateComp));
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationOutputDto searchCompilationById(final Long compId) {
        return CompilationMapper.compilationToCompilationOutputDto(compilationRepo.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id %d does not exist")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationOutputDto> searchCompilations(final Boolean pinned, final Integer from, final Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        CompilationCriteria criteria = CompilationCriteria.builder()
                .pinned(pinned)
                .build();
        CompilationSpecification compilationSpecification = new CompilationSpecification(criteria);
        return compilationRepo.findAll(compilationSpecification, pageable).stream()
                .map(CompilationMapper::compilationToCompilationOutputDto)
                .collect(Collectors.toList());
    }
}
