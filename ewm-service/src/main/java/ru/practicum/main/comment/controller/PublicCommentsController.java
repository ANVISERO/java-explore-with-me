package ru.practicum.main.comment.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/comments")
public class PublicCommentsController {

    private final CommentService commentService;

    @GetMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentOutputDto getCommentById(@PathVariable final Long commentId) {
        log.debug("GET request received to get comment with id = {}", commentId);
        return commentService.getCommentById(commentId);
    }

    @GetMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentOutputDto> getCommentsForAnEventByUser(
            @PathVariable final Long eventId,
            @RequestParam(defaultValue = "0") @PositiveOrZero final Integer from,
            @RequestParam(defaultValue = "10") @Positive final Integer size) {
        log.debug("GET request received to get all comments for an event with id = {}", eventId);
        return commentService.getCommentsForAnEventByUser(eventId, from, size);
    }
}