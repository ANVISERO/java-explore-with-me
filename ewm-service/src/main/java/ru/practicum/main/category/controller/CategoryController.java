package ru.practicum.main.category.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.category.dto.CategoryInputDto;
import ru.practicum.main.category.dto.CategoryOutputDto;
import ru.practicum.main.category.service.CategoryService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryOutputDto createCategory(@RequestBody @Valid final CategoryInputDto categoryInputDto) {
        log.debug("POST request received to create category: {}", categoryInputDto);
        return categoryService.createCategory(categoryInputDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCategory(@PathVariable final Long catId) {
        log.debug("DELETE request received to delete category with id: {}", catId);
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryOutputDto updateCategory(@PathVariable final Long catId,
                                            @RequestBody @Valid final CategoryInputDto categoryInputDto) {
        log.debug("PATCH request received to update category with id = {}: {}", catId, categoryInputDto);
        return categoryService.updateCategory(catId, categoryInputDto);
    }

    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryOutputDto> getAllCategories(
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero final Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive final Integer size) {
        log.debug("GET request received to get all categories");
        return categoryService.getAllCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryOutputDto getCategoryById(@PathVariable final Long catId) {
        log.debug("GET request received to find category with id = {}", catId);
        return categoryService.getCategoryById(catId);
    }
}
