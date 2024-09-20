package ru.practicum.main.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.State;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.exception.exceptions.NotFoundException;
import ru.practicum.main.exception.exceptions.RequestCreatedConflictException;
import ru.practicum.main.request.dto.RequestMapper;
import ru.practicum.main.request.dto.RequestOutputDto;
import ru.practicum.main.request.dto.RequestOutputUpdateStatusDto;
import ru.practicum.main.request.dto.RequestUpdateInputStatusDto;
import ru.practicum.main.request.model.Request;
import ru.practicum.main.request.model.RequestStatus;
import ru.practicum.main.request.repository.RequestRepository;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.UserRepository;
import ru.practicum.main.validator.EventValidator;
import ru.practicum.main.validator.UserValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public RequestOutputDto createRequest(final Long userId, final Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Not found event with id = %d", eventId)));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d does not exist", userId)));
        if (requestRepository.existsAllByRequesterIdAndEventId(userId, eventId)) {
            throw new RequestCreatedConflictException("User already created Request on this Event");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new RequestCreatedConflictException("Event initiator cant send request on this");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new RequestCreatedConflictException("You cant create request in not PUBLISHED Event");
        }
        if (event.getParticipantLimit() <= event.getConfirmedRequest() && event.getParticipantLimit() != 0) {
            throw new RequestCreatedConflictException("Event dont have vacancies to apply");
        }
        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .status(event.getParticipantLimit() == 0 || !event.getRequestModeration() ?
                        RequestStatus.CONFIRMED : RequestStatus.PENDING)
                .build();
        if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
            event.setConfirmedRequest(event.getConfirmedRequest() + 1);
            eventRepository.save(event);
        }
        return RequestMapper.requestToOutputRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestOutputDto> findUserRequests(final Long userId) {
        UserValidator.checkUserExist(userRepository, userId);
        return requestRepository.findAllByRequesterId(userId).stream()
                .map(RequestMapper::requestToOutputRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestOutputDto cancelRequest(final Long userId, final Long requestId) {
        UserValidator.checkUserExist(userRepository, userId);
        Request patchRequest = requestRepository.findAllByRequesterIdAndId(userId, requestId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("User with id = %d does not have request with id = %d", userId, requestId)));
        patchRequest.setStatus(RequestStatus.CANCELED);
        return RequestMapper.requestToOutputRequestDto(requestRepository.save(patchRequest));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestOutputDto> userParticipatesInEvent(final Long userId, final Long eventId) {
        EventValidator.checkEventExist(eventRepository, eventId);
        UserValidator.checkUserExist(userRepository, userId);
        return requestRepository.findAllByEventId(eventId).stream()
                .map(RequestMapper::requestToOutputRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestOutputUpdateStatusDto changeRequestStatus(final Long userId, final Long eventId,
                                                            final RequestUpdateInputStatusDto updateState) {
        UserValidator.checkUserExist(userRepository, userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Not found event with id = %d", eventId)));
        Integer alreadyConfirmed = requestRepository.findConfirmedRequestsOnEvent(eventId).size();
        if (event.getParticipantLimit() <= alreadyConfirmed) {
            throw new RequestCreatedConflictException("Event dont have vacancies to apply");
        }
        List<Request> queryRequests = requestRepository.findAllByIdIn(updateState.getRequestIds());
        if (!queryRequests.stream().allMatch(request -> request.getStatus().equals(RequestStatus.PENDING))) {
            throw new RequestCreatedConflictException("Not all given requests have state PENDING");
        }
        if (updateState.getStatus().equals(RequestStatus.REJECTED)) {
            queryRequests.forEach(request -> request.setStatus(RequestStatus.REJECTED));
            List<Request> saved = requestRepository.saveAll(queryRequests);
            return RequestMapper.requestListToUpdateStateList(saved);
        } else {
            List<Request> result = new ArrayList<>();
            for (int i = 0; i < event.getParticipantLimit() - alreadyConfirmed && i < queryRequests.size(); i++) {
                Request iteration = queryRequests.get(i);
                iteration.setStatus(RequestStatus.CONFIRMED);
                result.add(iteration);
                queryRequests.remove(queryRequests.get(i));
                event.setConfirmedRequest((long) i + 1);
            }
            eventRepository.save(event);
            result.addAll(queryRequests.stream()
                    .peek(request -> request.setStatus(RequestStatus.REJECTED))
                    .toList()
            );
            return RequestMapper.requestListToUpdateStateList(requestRepository.saveAll(result));
        }
    }
}