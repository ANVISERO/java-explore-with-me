package ru.practicum.main.events.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.events.model.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepo extends JpaRepository<Event, Long> {

    @Query(value = "SELECT e FROM Event AS e " +
            "WHERE (:users IS NULL OR e.initiator.id IN :users) " +
            "AND (:states IS NULL OR e.state IN :states) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "OR (CAST(:rangeStart AS date) IS NULL) " +
            "OR (CAST(:rangeStart AS date) IS NULL AND e.eventDate < CAST(:rangeEnd AS date)) " +
            "OR (CAST(:rangeEnd AS date) IS NULL AND e.eventDate > CAST(:rangeStart AS date)) " +
            "GROUP BY e.id " +
            "ORDER BY e.id ASC")
    List<Event> findEventsByAdminFromParam(@Param("users") List<Long> users,
                                           @Param("states") List<State> states,
                                           @Param("categories") List<Long> categories,
                                           @Param("rangeStart") LocalDateTime rangeStart,
                                           @Param("rangeEnd") LocalDateTime rangeEnd,
                                           Pageable pageable);


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
    List<Event> findEventsByPublicFromParam(@Param("text") String text,
                                            @Param("categories") List<Long> categories,
                                            @Param("paid") Boolean paid,
                                            @Param("rangeStart") LocalDateTime startTime,
                                            @Param("rangeEnd") LocalDateTime endTime,
                                            @Param("onlyAvailable") Boolean onlyAvailable,
                                            @Param("sort") String sort,
                                            Pageable pageable);

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
            @Param("categories") List<Long> categories,
            @Param("paid") Boolean paid,
            @Param("rangeStart") LocalDateTime startTime,
            @Param("rangeEnd") LocalDateTime endTime,
            @Param("onlyAvailable") Boolean onlyAvailable,
            @Param("sort") String sort,
            Pageable pageable);

    default Optional<Event> findPublishedEventById(Long eventId) {
        return findAllByIdAndState(eventId, State.PUBLISHED);
    }

    List<Event> findAllByInitiator_Id(Long id, Pageable pageable);

    Optional<Event> findAllByIdAndInitiator_Id(Long id, Long userId);

    Optional<Event> findAllByIdAndState(Long eventId, State state);

    List<Event> findAllByIdIn(List<Long> ids);

    Boolean existsByCategory_Id(Long categoryId);
}
