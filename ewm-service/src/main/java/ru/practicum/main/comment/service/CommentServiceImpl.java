package ru.practicum.main.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.comment.dto.CommentInputDto;
import ru.practicum.main.comment.dto.CommentMapper;
import ru.practicum.main.comment.dto.CommentOutputDto;
import ru.practicum.main.comment.model.Comment;
import ru.practicum.main.comment.model.CommentStatus;
import ru.practicum.main.comment.storage.CommentRepository;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.State;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.exception.exceptions.EventStateConflictException;
import ru.practicum.main.exception.exceptions.NotFoundException;
import ru.practicum.main.exception.exceptions.UserIdConflictException;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CommentOutputDto approveEventCommentByIdByAdmin(final Long commentId) {
        Comment comment = getCommentByIdOrElseThrow(commentId);
        comment.setStatus(CommentStatus.APPROVED);
        return CommentMapper.commentToCommentOutputDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void rejectEventCommentByIdByAdmin(final Long commentId) {
        getCommentByIdOrElseThrow(commentId);
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentOutputDto> getAllEventComments(final Boolean pending, final Integer from, final Integer size) {
        List<Comment> comments;
        if (pending) {
            comments = commentRepository.findAllByStatus(CommentStatus.MODERATION);
        } else {
            comments = commentRepository.findAll();
        }
        return comments.stream().map(CommentMapper::commentToCommentOutputDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentOutputDto> getAllEventCommentsByAdmin(final Long eventId, final Integer from,
                                                             final Integer size) {
        getEventByIdOrElseThrow(eventId);
        Pageable pageable = PageRequest.of(from / size, size);
        List<Comment> comments = commentRepository.findAllByEventIdAndStatus(eventId,
                CommentStatus.MODERATION, pageable);
        return comments.stream().map(CommentMapper::commentToCommentOutputDto).toList();
    }

    @Override
    @Transactional
    public CommentOutputDto addComment(final Long userId, final Long eventId,
                                       final CommentInputDto commentInputDto) {
        User user = getUserByIdOrElseThrow(userId);
        Event event = getEventByIdOrElseThrow(eventId);
        if (event.getState() != State.PUBLISHED) {
            throw new EventStateConflictException("Event is unpublished");
        }
        Comment comment = CommentMapper.commentInputDtoToComment(commentInputDto, user, event);
        return CommentMapper.commentToCommentOutputDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentOutputDto updateCommentById(final Long userId, final Long commentId,
                                              final CommentInputDto commentInputDto) {
        getUserByIdOrElseThrow(userId);
        Comment comment = getCommentByIdOrElseThrow(commentId);
        checkOwner(comment.getAuthor().getId(), userId);
        comment.setText(commentInputDto.getText());
        return CommentMapper.commentToCommentOutputDto(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentOutputDto> getAllCommentsByUser(final Long userId, final Integer from, final Integer size) {
        getUserByIdOrElseThrow(userId);
        Pageable pageable = PageRequest.of(from / size, size);
        List<Comment> comments = commentRepository.findAllByAuthorId(userId, pageable);
        return comments.stream().map(CommentMapper::commentToCommentOutputDto).toList();
    }

    @Override
    @Transactional
    public void deleteCommentById(final Long userId, final Long commentId) {
        getUserByIdOrElseThrow(userId);
        Comment comment = getCommentByIdOrElseThrow(commentId);
        checkOwner(comment.getAuthor().getId(), userId);
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentOutputDto getCommentById(final Long commentId) {
        return CommentMapper.commentToCommentOutputDto(getCommentByIdOrElseThrow(commentId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentOutputDto> getCommentsForAnEventByUser(final Long eventId, final Integer from, final Integer size) {
        Event event = getEventByIdOrElseThrow(eventId);
        if (event.getState() != State.PUBLISHED) {
            throw new EventStateConflictException("Event is unpublished");
        }
        Pageable pageable = PageRequest.of(from / size, size);
        List<Comment> comments = commentRepository.findAllByEventId(eventId, pageable);
        return comments.stream().map(CommentMapper::commentToCommentOutputDto).toList();
    }

    private Comment getCommentByIdOrElseThrow(final Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException(String.format("Comment with id = %s was not found", commentId)));
    }

    private Event getEventByIdOrElseThrow(final Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id = %s was not found", eventId)));
    }

    private User getUserByIdOrElseThrow(final Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User with id = %s was not found", userId)));
    }

    private void checkOwner(Long authorId, Long userId) {
        if (!Objects.equals(authorId, userId)) {
            throw new UserIdConflictException(String.format("The user with id = %d is not the owner", userId));
        }
    }
}
