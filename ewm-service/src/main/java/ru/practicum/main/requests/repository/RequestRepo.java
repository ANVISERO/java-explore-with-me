package ru.practicum.main.requests.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.requests.model.Request;
import ru.practicum.main.requests.model.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface RequestRepo extends JpaRepository<Request, Long> {
    default List<Request> findConfirmedRequestsOnEvent(final Long eventId) {
        return findAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
    }

    List<Request> findAllByEventIdAndStatus(final Long eventId, final RequestStatus state);

    List<Request> findAllByRequesterId(final Long requesterId);

    Optional<Request> findAllByRequesterIdAndId(final Long requesterId, final Long id);

    List<Request> findAllByIdIn(final List<Long> id);

    List<Request> findAllByEventId(final Long eventId);

    Boolean existsAllByRequesterIdAndEventId(final Long userId, final Long eventId);
}
