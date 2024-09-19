package ru.practicum.main.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.category.repository.CategoryRepository;
import ru.practicum.main.events.dto.EventFullOutputDto;
import ru.practicum.main.events.dto.EventInputDto;
import ru.practicum.main.events.dto.EventMapper;
import ru.practicum.main.events.dto.EventShortOutputDto;
import ru.practicum.main.events.dto.UpdateEventDto;
import ru.practicum.main.events.model.AdminUpdateState;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.events.model.State;
import ru.practicum.main.events.model.UserUpdateState;
import ru.practicum.main.events.repository.EventRepo;
import ru.practicum.main.exception.exceptions.EventStateConflictException;
import ru.practicum.main.exception.exceptions.EventUpdateConflictException;
import ru.practicum.main.exception.exceptions.NotFoundException;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.UserRepo;
import ru.practicum.main.validator.EventValidator;
import ru.practicum.main.validator.UserValidator;
import ru.practicum.stats.client.Client;
import ru.practicum.stats.dto.model.HitDto;
import ru.practicum.stats.dto.model.StatsDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Import(Client.class)
public class EventServiceImpl implements EventsService {
    private static final String BASE_URI = "/events";
    private final String app = "ewm-main-service";
    private final EventRepo eventRepo;
    private final UserRepo userRepo;
    private final CategoryRepository categoryRepository;
    private final Client statsClient;

    @Override
    @Transactional
    public EventFullOutputDto addEvent(final Long userId, final EventInputDto eventInputDto) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id = %d does not exist", userId)));
        Category category = categoryRepository.findById(eventInputDto.getCategory())
                .orElseThrow(() -> new NotFoundException(String.format("User with id = %d does not exist", userId)));
        Event event = EventMapper.eventInputDtoToEvent(eventInputDto, user, category);
        event.setState(State.PENDING);
        event.setConfirmedRequest(0L);
        return EventMapper.eventToEventFullOutputDto(eventRepo.save(event));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortOutputDto> getUserEvents(final Long userId, final Integer from, final Integer size) {
        UserValidator.checkUserExist(userRepo, userId);
        Pageable pageable = PageRequest.of(from / size, size);
        return fillEventsHit(eventRepo.findAllByInitiatorId(userId, pageable)).stream()
                .map(EventMapper::eventToEventShortOutputDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullOutputDto getUserEventById(final Long userId, final Long eventId) {
        UserValidator.checkUserExist(userRepo, userId);
        Event event = eventRepo.findAllByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Not found event with id = %d", eventId)));
        return EventMapper.eventToEventFullOutputDto(fillEventsHit(List.of(event)).getFirst());
    }

    @Override
    @Transactional
    public EventFullOutputDto updateEvent(final Long userId, final Long eventId, final UpdateEventDto updateEventDto) {
        UserValidator.checkUserExist(userRepo, userId);
        Event updateEvent = eventRepo.findAllByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Not found event with id = %d", eventId)));
        if (!(updateEvent.getState().equals(State.PENDING) || updateEvent.getState().equals(State.REJECT_EVENT))) {
            throw new EventUpdateConflictException("This event not in normal(PENDING/REJECT_EVENT) state");
        }
        Category updateCategory = updateEvent.getCategory();
        if (updateEventDto.getCategory() != null) {
            updateCategory = categoryRepository.findById(updateEventDto.getCategory())
                    .orElseThrow(() -> new NotFoundException(
                            String.format("Category with id %d does not exist", updateEventDto.getCategory())));
        }
        State updateState = updateEvent.getState();
        if (updateEventDto.getStateAction() != null) {
            try {
                updateState = switch (UserUpdateState.valueOf(updateEventDto.getStateAction())) {
                    case CANCEL_REVIEW -> State.CANCELED;
                    case SEND_TO_REVIEW -> State.PENDING;
                };
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException("Incorrect event State in query");
            }
        }
        return EventMapper.eventToEventFullOutputDto(eventRepo.save(
                EventMapper.updateEvent(updateEventDto, updateEvent, updateCategory, updateState)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullOutputDto> adminSearchEvents(final List<Long> users, final List<String> states,
                                                      final List<Long> categories, LocalDateTime rangeStart,
                                                      final LocalDateTime rangeEnd, final Integer from,
                                                      final Integer size) {
        EventValidator.checkEventStartTime(rangeStart, rangeEnd);
        if (rangeStart == null && rangeEnd == null) {
            rangeStart = LocalDateTime.now();
        }
        Pageable pageable = PageRequest.of(from / size, size);
        List<State> convertStates = List.of();
        try {
            if (states != null) {
                convertStates = states.stream()
                        .map(State::valueOf)
                        .collect(Collectors.toList());
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Incorrect event State in query");
        }
        return fillEventsHit(eventRepo.findEventsByAdminFromParam(users, convertStates, categories, rangeStart,
                rangeEnd, pageable)).stream()
                .map(EventMapper::eventToEventFullOutputDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullOutputDto adminUpdateEvent(final Long eventId, final UpdateEventDto updateEventDto) {
        Event updateEvent = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Not found event with id = %d", eventId)));
        if (updateEvent.getState().equals(State.PUBLISHED) || updateEvent.getState().equals(State.REJECT_EVENT)) {
            throw new EventStateConflictException("Attempt update PUBLISHED/REJECT_EVENT event");
        }
        Category updateCategory = updateEvent.getCategory();
        if (updateEventDto.getCategory() != null) {
            updateCategory = categoryRepository.findById(updateEventDto.getCategory())
                    .orElseThrow(() -> new NotFoundException(
                            String.format("Category with id %d does not exist", updateEventDto.getCategory())));
        }
        State updateState = updateEvent.getState();
        if (updateEventDto.getStateAction() != null) {
            switch (AdminUpdateState.valueOf(updateEventDto.getStateAction())) {
                case PUBLISH_EVENT -> {
                    updateState = State.PUBLISHED;
                    updateEvent.setPublishedOn(LocalDateTime.now());
                }
                case REJECT_EVENT -> updateState = State.REJECT_EVENT;
            }
        }
        return EventMapper.eventToEventFullOutputDto(eventRepo.save(
                EventMapper.updateEvent(updateEventDto, updateEvent, updateCategory, updateState)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortOutputDto> publicSearchEvents(final String text, final List<Long> categories,
                                                        final Boolean paid, LocalDateTime rangeStart,
                                                        final LocalDateTime rangeEnd, final Boolean onlyAvailable,
                                                        final String sort, final Integer from,
                                                        final Integer size, final String ip) {
        EventValidator.checkEventStartTime(rangeStart, rangeEnd);
        sendStats(BASE_URI, ip);
        if (rangeStart == null && rangeEnd == null) {
            rangeStart = LocalDateTime.now();
        }
        Pageable pageable = PageRequest.of(from / size, size);
        if (text == null || text.trim().isEmpty()) {
            return fillEventsHit(eventRepo.findEventsByPublicFromParamWithoutText(categories, paid, rangeStart,
                    rangeEnd, onlyAvailable, sort, pageable)).stream()
                    .map(EventMapper::eventToEventShortOutputDto)
                    .collect(Collectors.toList());
        }
        return fillEventsHit(eventRepo.findEventsByPublicFromParam(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, pageable)).stream()
                .map(EventMapper::eventToEventShortOutputDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullOutputDto getEventById(final Long id, final String ip) {
        Event event = eventRepo.findPublishedEventById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Not found event with id = %d", id)));
        sendStats(BASE_URI + "/" + id, ip);
        return EventMapper.eventToEventFullOutputDto(fillEventsHit(List.of(event)).getFirst());
    }

    private List<Event> fillEventsHit(final List<Event> events) {
        List<StatsDto> statsDtos = statsClient.getStats(
                LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS),
                LocalDateTime.now().plusMinutes(1),
                events.stream()
                        .map(event -> BASE_URI + "/" + event.getId())
                        .collect(Collectors.toList()),
                true);
        Map<Long, Long> result = new HashMap<>();
        statsDtos.forEach(st -> result.put(Long.valueOf(st.getUri().substring(
                st.getUri().lastIndexOf("/") + 1)), st.getHits()));
        return events.stream()
                .peek(event -> event.setViews(result.get(event.getId())))
                .collect(Collectors.toList());
    }

    private void sendStats(final String uri, final String ip) {
        statsClient.saveHit(HitDto.builder().app(app).uri(uri).ip(ip).timestamp(LocalDateTime.now()).build());
    }
}
