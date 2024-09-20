package ru.practicum.main.comment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.comment.dto.CommentInputDto;
import ru.practicum.main.comment.dto.CommentOutputDto;
import ru.practicum.main.comment.service.CommentService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
public class UserCommentsController {

    private final CommentService commentService;

    @PostMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentOutputDto addComment(@PathVariable final Long userId,
                                       @PathVariable final Long eventId,
                                       @Valid @RequestBody final CommentInputDto commentInputDto) {
        log.info("POST request received to create user comment with id = {}", userId);
        return commentService.addComment(userId, eventId, commentInputDto);
    }

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentOutputDto updateCommentById(@PathVariable final Long userId,
                                              @PathVariable final Long commentId,
                                              @Valid @RequestBody final CommentInputDto commentInputDto) {
        log.info("PATCH request received to change a comment with id = {}", commentId);
        return commentService.updateCommentById(userId, commentId, commentInputDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentOutputDto> getAllCommentsByUser(
            @PathVariable final Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero final Integer from,
            @RequestParam(defaultValue = "10") @Positive final Integer size) {
        log.info("GET request received to get all comments with userId = {}", userId);
        return commentService.getAllCommentsByUser(userId, from, size);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable final Long userId,
                                  @PathVariable final Long commentId) {
        log.info("DELETE request received to delete a comment with id = {}", commentId);
        commentService.deleteCommentById(userId, commentId);
    }
}