package ru.practicum.main.requests.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.requests.model.Request;
import ru.practicum.main.requests.model.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface RequestRepo extends JpaRepository<Request, Long> {
    default List<Request> findConfirmedRequestsOnEvent(final Long eventId) {
        return findAllByEvent_IdAndStatus(eventId, RequestStatus.CONFIRMED);
    }

    List<Request> findAllByEvent_IdAndStatus(final Long eventId, final RequestStatus state);

    List<Request> findAllByRequester_Id(final Long requesterId);

    Optional<Request> findAllByRequester_IdAndId(final Long requesterId, final Long id);

    List<Request> findAllByIdIn(final List<Long> id);

    List<Request> findAllByEvent_Id(final Long eventId);

    Boolean existsAllByRequester_IdAndEvent_Id(final Long userId, final Long eventId);
}
