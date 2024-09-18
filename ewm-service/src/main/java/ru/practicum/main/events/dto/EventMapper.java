package ru.practicum.main.events.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.main.category.dto.CategoryMapper;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.events.model.Location;
import ru.practicum.main.events.model.State;
import ru.practicum.main.user.dto.UserMapper;
import ru.practicum.main.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {

    public Event inputEventDtoToEvent(EventInputDto eventInputDto, User user, Category category) {
        return Event.builder()
                .annotation(eventInputDto.getAnnotation())
                .category(category)
                .createdOn(LocalDateTime.now())
                .description(eventInputDto.getDescription())
                .eventDate(eventInputDto.getEventDate())
                .initiator(user)
                .paid(eventInputDto.getPaid())
                .participantLimit(eventInputDto.getParticipantLimit())
                .requestModeration(eventInputDto.getRequestModeration())
                .title(eventInputDto.getTitle())
                .lat(eventInputDto.getLocation().getLat())
                .lon(eventInputDto.getLocation().getLon())
                .build();
    }

    public EventFullOutputDto eventToFullOutputEventDto(Event event) {
        return EventFullOutputDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToCategoryOutputDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequest())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.UserToOutputUserDto(event.getInitiator()))
                .location(Location.builder()
                        .lon(event.getLon())
                        .lat(event.getLat())
                        .build())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public ShortOutputEventDto eventToShortOutputDto(Event event) {
        return ShortOutputEventDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToCategoryOutputDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequest())
                .id(event.getId())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.UserToOutputUserDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }

    public Event updateEvent(UpdateEventDto updateEventDto, Event event, Category category, State state) {
        return Event.builder()
                .annotation(updateEventDto.getAnnotation() != null ?
                        updateEventDto.getAnnotation() : event.getAnnotation())
                .category(category)
                .description(updateEventDto.getDescription() != null ?
                        updateEventDto.getDescription() : event.getDescription())
                .eventDate(updateEventDto.getEventDate() != null ?
                        updateEventDto.getEventDate() : event.getEventDate())
                .lon(updateEventDto.getLocation() != null ?
                        updateEventDto.getLocation().getLon() : event.getLon())
                .lat(updateEventDto.getLocation() != null ?
                        updateEventDto.getLocation().getLat() : event.getLat())
                .paid(updateEventDto.getPaid() != null ?
                        updateEventDto.getPaid() : event.getPaid())
                .participantLimit(updateEventDto.getParticipantLimit() != null ?
                        updateEventDto.getParticipantLimit() : event.getParticipantLimit())
                .requestModeration(updateEventDto.getRequestModeration() != null ?
                        updateEventDto.getRequestModeration() : event.getRequestModeration())
                .state(state)
                .title(updateEventDto.getTitle() != null ?
                        updateEventDto.getTitle() : event.getTitle())
                .confirmedRequest(event.getConfirmedRequest())
                .createdOn(event.getCreatedOn())
                .id(event.getId())
                .initiator(event.getInitiator())
                .publishedOn(event.getPublishedOn())
                .views(event.getViews())
                .build();
    }
}