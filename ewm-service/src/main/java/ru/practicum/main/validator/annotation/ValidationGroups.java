package ru.practicum.main.validator.annotation;


import jakarta.validation.groups.Default;

public interface ValidationGroups {
    interface Create extends Default {
    }

    interface Update extends Default {
    }
}
