package br.csw.opensarc.professors.controller.dto;

import br.csw.opensarc.professors.repository.entity.CertificationEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CertificationInput(
        @Min(value = 1920, message = "Year should be at least 1920") int year,
        @NotBlank(message = "Level is required") String level,
        @NotBlank(message = "Description is required") String description) {

    public CertificationEntity toEntity(String professorId) {
        return new CertificationEntity(professorId, year, level, description);
    }
}
