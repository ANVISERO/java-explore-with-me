package ru.practicum.main.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "SELECT e FROM Event AS e " +
            "WHERE (:users IS NULL OR e.initiator.id IN :users) " +
            "AND (:states IS NULL OR e.state IN :states) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "OR (CAST(:rangeStart AS date) IS NULL) " +
            "OR (CAST(:rangeStart AS date) IS NULL AND e.eventDate < CAST(:rangeEnd AS date)) " +
            "OR (CAST(:rangeEnd AS date) IS NULL AND e.eventDate > CAST(:rangeStart AS date)) " +
            "GROUP BY e.id " +
            "ORDER BY e.id ASC")
    List<Event> findEventsByAdminFromParam(@Param("users") final List<Long> users,
                                           @Param("states") final List<State> states,
                                           @Param("categories") final List<Long> categories,
                                           @Param("rangeStart") final LocalDateTime rangeStart,
                                           @Param("rangeEnd") final LocalDateTime rangeEnd,
                                           final Pageable pageable);


    @Query(value = "SELECT e FROM Event AS e " +
            "WHERE (e.state = 'PUBLISHED') " +
            "AND (:text IS NULL) " +
            "OR (LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "OR (LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "OR (LOWER(e.title) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "OR (CAST(:rangeStart AS date) IS NULL) " +
            "OR (CAST(:rangeStart AS date) IS NULL AND e.eventDate < CAST(:rangeEnd AS date)) " +
            "OR (CAST(:rangeEnd AS date) IS NULL AND e.eventDate > CAST(:rangeStart AS date)) " +
            "AND (e.confirmedRequest < e.participantLimit OR :onlyAvailable = FALSE)" +
            "GROUP BY e.id " +
            "ORDER BY LOWER(:sort) ASC")
    List<Event> findEventsByPublicFromParam(@Param("text") final String text,
                                            @Param("categories") final List<Long> categories,
                                            @Param("paid") final Boolean paid,
                                            @Param("rangeStart") final LocalDateTime startTime,
                                            @Param("rangeEnd") final LocalDateTime endTime,
                                            @Param("onlyAvailable") final Boolean onlyAvailable,
                                            @Param("sort") final String sort,
                                            final Pageable pageable);

    @Query(value = "SELECT e FROM Event AS e " +
            "WHERE (e.state = 'PUBLISHED') " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "OR (CAST(:rangeStart AS date) IS NULL) " +
            "OR (CAST(:rangeStart AS date) IS NULL AND e.eventDate < CAST(:rangeEnd AS date)) " +
            "OR (CAST(:rangeEnd AS date) IS NULL AND e.eventDate > CAST(:rangeStart AS date)) " +
            "AND (e.confirmedRequest < e.participantLimit OR :onlyAvailable = FALSE)" +
            "GROUP BY e.id " +
            "ORDER BY LOWER(:sort) ASC")
    List<Event> findEventsByPublicFromParamWithoutText(
            @Param("categories") final List<Long> categories,
            @Param("paid") final Boolean paid,
            @Param("rangeStart") final LocalDateTime startTime,
            @Param("rangeEnd") final LocalDateTime endTime,
            @Param("onlyAvailable") final Boolean onlyAvailable,
            @Param("sort") final String sort,
            final Pageable pageable);

    default Optional<Event> findPublishedEventById(final Long eventId) {
        return findAllByIdAndState(eventId, State.PUBLISHED);
    }

    List<Event> findAllByInitiatorId(final Long id, final Pageable pageable);

    Optional<Event> findAllByIdAndInitiatorId(final Long id, final Long userId);

    Optional<Event> findAllByIdAndState(final Long eventId, final State state);

    List<Event> findAllByIdIn(final List<Long> ids);

    Boolean existsByCategoryId(final Long categoryId);
}
