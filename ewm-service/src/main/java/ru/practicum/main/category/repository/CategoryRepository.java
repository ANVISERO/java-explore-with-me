package ru.practicum.main.category.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Boolean existsByName(final String name);

    Boolean existsByNameAndIdNot(final String name, final Long catId);
}
