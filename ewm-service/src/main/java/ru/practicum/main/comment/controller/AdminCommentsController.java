package ru.practicum.main.comment.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.comment.dto.CommentOutputDto;
import ru.practicum.main.comment.service.CommentService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
public class AdminCommentsController {
    private final CommentService commentService;

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentOutputDto approveEventCommentByIdByAdmin(@PathVariable final Long commentId) {
        log.debug("PATCH request received to approve comment with id = {}", commentId);
        return commentService.approveEventCommentByIdByAdmin(commentId);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void rejectEventCommentByIdByAdmin(@PathVariable final Long commentId) {
        log.debug("DELETE request received to reject comment with id = {}", commentId);
        commentService.rejectEventCommentByIdByAdmin(commentId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentOutputDto> getAllEventComments(
            @RequestParam(defaultValue = "false") final Boolean pending,
            @RequestParam(defaultValue = "0") @PositiveOrZero final Integer from,
            @RequestParam(defaultValue = "10") @Positive final Integer size) {
        log.debug("GET request received to get all comments with pending = {}, from = {}, size = {}",
                pending, from, size);
        return commentService.getAllEventComments(pending, from, size);
    }

    @GetMapping("events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentOutputDto> getAllEventCommentsByAdmin(
            @PathVariable final Long eventId,
            @RequestParam(defaultValue = "0") @PositiveOrZero final Integer from,
            @RequestParam(defaultValue = "10") @Positive final Integer size) {
        log.debug("GET request received to get all event comments with eventId = {}, from = {}, size = {}",
                eventId, from, size);
        return commentService.getAllEventCommentsByAdmin(eventId, from, size);
    }
}