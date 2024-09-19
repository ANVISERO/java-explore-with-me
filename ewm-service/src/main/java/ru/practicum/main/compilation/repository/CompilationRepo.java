package ru.practicum.main.compilation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.main.compilation.model.Compilation;

public interface CompilationRepo extends JpaRepository<Compilation, Long>, JpaSpecificationExecutor<Compilation> {
}
