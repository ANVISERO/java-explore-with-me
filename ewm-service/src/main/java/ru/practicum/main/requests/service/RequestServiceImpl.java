package ru.practicum.main.requests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.events.model.State;
import ru.practicum.main.events.repository.EventRepo;
import ru.practicum.main.exception.exceptions.NotFoundException;
import ru.practicum.main.exception.exceptions.RequestCreatedConflictException;
import ru.practicum.main.requests.dto.RequestMapper;
import ru.practicum.main.requests.dto.RequestOutputDto;
import ru.practicum.main.requests.dto.RequestOutputUpdateStatusDto;
import ru.practicum.main.requests.dto.RequestUpdateInputStatusDto;
import ru.practicum.main.requests.model.Request;
import ru.practicum.main.requests.model.RequestStatus;
import ru.practicum.main.requests.repository.RequestRepo;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.UserRepo;
import ru.practicum.main.validator.EventValidator;
import ru.practicum.main.validator.UserValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepo requestRepo;
    private final EventRepo eventRepo;
    private final UserRepo userRepo;

    @Override
    @Transactional
    public RequestOutputDto createRequest(final Long userId, final Long eventId) {
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Not found event with id = %d", eventId)));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d does not exist", userId)));
        if (requestRepo.existsAllByRequesterIdAndEventId(userId, eventId)) {
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
            eventRepo.save(event);
        }
        return RequestMapper.requestToOutputRequestDto(requestRepo.save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestOutputDto> findUserRequests(final Long userId) {
        UserValidator.checkUserExist(userRepo, userId);
        return requestRepo.findAllByRequesterId(userId).stream()
                .map(RequestMapper::requestToOutputRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestOutputDto cancelRequest(final Long userId, final Long requestId) {
        UserValidator.checkUserExist(userRepo, userId);
        Request patchRequest = requestRepo.findAllByRequesterIdAndId(userId, requestId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("User with id = %d does not have request with id = %d", userId, requestId)));
        patchRequest.setStatus(RequestStatus.CANCELED);
        return RequestMapper.requestToOutputRequestDto(requestRepo.save(patchRequest));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestOutputDto> userParticipatesInEvent(final Long userId, final Long eventId) {
        EventValidator.checkEventExist(eventRepo, eventId);
        UserValidator.checkUserExist(userRepo, userId);
        return requestRepo.findAllByEventId(eventId).stream()
                .map(RequestMapper::requestToOutputRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestOutputUpdateStatusDto changeRequestStatus(final Long userId, final Long eventId,
                                                            final RequestUpdateInputStatusDto updateState) {
        UserValidator.checkUserExist(userRepo, userId);
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Not found event with id = %d", eventId)));
        Integer alreadyConfirmed = requestRepo.findConfirmedRequestsOnEvent(eventId).size();
        if (event.getParticipantLimit() <= alreadyConfirmed) {
            throw new RequestCreatedConflictException("Event dont have vacancies to apply");
        }
        List<Request> queryRequests = requestRepo.findAllByIdIn(updateState.getRequestIds());
        if (!queryRequests.stream().allMatch(request -> request.getStatus().equals(RequestStatus.PENDING))) {
            throw new RequestCreatedConflictException("Not all given requests have state PENDING");
        }
        if (updateState.getStatus().equals(RequestStatus.REJECTED)) {
            queryRequests.forEach(request -> request.setStatus(RequestStatus.REJECTED));
            List<Request> saved = requestRepo.saveAll(queryRequests);
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
            eventRepo.save(event);
            result.addAll(queryRequests.stream()
                    .peek(request -> request.setStatus(RequestStatus.REJECTED))
                    .toList()
            );
            return RequestMapper.requestListToUpdateStateList(requestRepo.saveAll(result));
        }
    }
}