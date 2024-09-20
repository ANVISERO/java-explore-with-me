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
import ru.practicum.main.compilation.repository.CompilationRepository;
import ru.practicum.main.compilation.repository.CompilationSpecification;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.exception.exceptions.NotFoundException;
import ru.practicum.main.validator.CompilationValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    @Override
    @Transactional
    public CompilationOutputDto createCompilation(final CompilationInputDto compilationInputDto) {
        List<Event> events = eventRepository.findAllByIdIn(compilationInputDto.getEvents());
        Compilation compilation = CompilationMapper.compilationInputDtoToCompilation(compilationInputDto, events);
        return CompilationMapper.compilationToCompilationOutputDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional
    public void deleteCompilation(final Long compId) {
        CompilationValidator.checkCompilationExist(compilationRepository, compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationOutputDto updateCompilation(final Long compId, final CompilationInputDto compilationInputDto) {
        Compilation updateComp = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id %d does not exist"));
        List<Event> events = eventRepository.findAllByIdIn(compilationInputDto.getEvents());
        updateComp = CompilationMapper.updateCompilation(compilationInputDto, updateComp, events);
        return CompilationMapper.compilationToCompilationOutputDto(compilationRepository.save(updateComp));
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationOutputDto searchCompilationById(final Long compId) {
        return CompilationMapper.compilationToCompilationOutputDto(compilationRepository.findById(compId)
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
        return compilationRepository.findAll(compilationSpecification, pageable).stream()
                .map(CompilationMapper::compilationToCompilationOutputDto)
                .collect(Collectors.toList());
    }
}
