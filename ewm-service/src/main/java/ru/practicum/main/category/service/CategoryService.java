package ru.practicum.main.category.service;

import ru.practicum.main.category.dto.CategoryInputDto;
import ru.practicum.main.category.dto.CategoryOutputDto;

import java.util.List;

public interface CategoryService {
    CategoryOutputDto createCategory(final CategoryInputDto categoryInputDto);

    void deleteCategory(final Long catId);

    CategoryOutputDto updateCategory(final Long catId, final CategoryInputDto categoryInputDto);

    List<CategoryOutputDto> getAllCategories(final Integer from, final Integer size);

    CategoryOutputDto getCategoryById(final Long catId);
}
