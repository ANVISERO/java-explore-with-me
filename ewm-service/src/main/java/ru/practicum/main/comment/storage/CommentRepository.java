package ru.practicum.main.comment.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.comment.model.Comment;
import ru.practicum.main.comment.model.CommentStatus;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByStatus(final CommentStatus status);

    List<Comment> findAllByAuthorId(final Long authorId, final Pageable pageable);

    List<Comment> findAllByEventId(final Long eventId, final Pageable pageable);

    List<Comment> findAllByEventIdAndStatus(final Long eventId, final CommentStatus status, final Pageable pageable);
}