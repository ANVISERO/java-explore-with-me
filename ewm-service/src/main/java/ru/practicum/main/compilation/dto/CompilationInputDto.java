package ru.practicum.main.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.validator.annotation.ValidationGroups;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class CompilationInputDto {
    @Builder.Default
    private List<Long> events = List.of();

    private Boolean pinned;

    @NotBlank(groups = ValidationGroups.Create.class)
    @Size(max = 50)
    private String title;
}
