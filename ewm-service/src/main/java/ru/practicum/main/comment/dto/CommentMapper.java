package ru.practicum.main.comment.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.main.comment.model.Comment;
import ru.practicum.main.comment.model.CommentStatus;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.user.dto.UserMapper;
import ru.practicum.main.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public CommentOutputDto commentToCommentOutputDto(final Comment comment) {
        return CommentOutputDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .author(UserMapper.userToOutputUserDto(comment.getAuthor()))
                .eventId(comment.getEvent().getId())
                .status(comment.getStatus())
                .build();
    }

    public Comment commentInputDtoToComment(final CommentInputDto commentInputDto, final User user, final Event event) {
        return Comment.builder()
                .created(LocalDateTime.now())
                .text(commentInputDto.getText())
                .author(user)
                .event(event)
                .status(CommentStatus.MODERATION)
                .build();
    }
}