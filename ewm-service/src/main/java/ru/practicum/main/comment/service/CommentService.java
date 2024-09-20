package ru.practicum.main.comment.service;

import ru.practicum.main.comment.dto.CommentInputDto;
import ru.practicum.main.comment.dto.CommentOutputDto;

import java.util.List;

public interface CommentService {
    CommentOutputDto approveEventCommentByIdByAdmin(final Long commentId);

    void rejectEventCommentByIdByAdmin(final Long commentId);

    List<CommentOutputDto> getAllEventComments(final Boolean pending, final Integer from, final Integer size);

    List<CommentOutputDto> getAllEventCommentsByAdmin(final Long eventId, final Integer from, final Integer size);

    CommentOutputDto addComment(final Long userId, final Long eventId, final CommentInputDto commentInputDto);

    CommentOutputDto updateCommentById(final Long userId, final Long commentId, final CommentInputDto commentInputDto);

    List<CommentOutputDto> getAllCommentsByUser(final Long userId, final Integer from, final Integer size);

    void deleteCommentById(final Long userId, final Long commentId);

    CommentOutputDto getCommentById(final Long commentId);

    List<CommentOutputDto> getCommentsForAnEventByUser(final Long eventId, final Integer from, final Integer size);
}
