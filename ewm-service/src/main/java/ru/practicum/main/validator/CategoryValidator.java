package ru.practicum.main.validator;

import lombok.experimental.UtilityClass;
import ru.practicum.main.category.repository.CategoryRepository;
import ru.practicum.main.exception.exceptions.CategoryUniqueNameException;
import ru.practicum.main.exception.exceptions.NotFoundException;

@UtilityClass
public class CategoryValidator {
    public void checkCategoryExist(final CategoryRepository categoryRepository, final Long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundException(String.format("Category with id = %d does not exist", catId));
        }
    }

    public void checkAnotherCategoryUseName(final CategoryRepository categoryRepository,
                                            final Long catId, final String name) {
        if (categoryRepository.existsByNameAndIdNot(name, catId)) {
            throw new CategoryUniqueNameException("Name already exist");
        }
    }

    public void checkNameAlreadyExist(final CategoryRepository categoryRepository, final String name) {
        if (categoryRepository.existsByName(name)) {
            throw new CategoryUniqueNameException("Name already exist");
        }
    }
}
