package br.csw.opensarc.professors.controller.dto;

import br.csw.opensarc.professors.repository.entity.CertificationEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CertificationInput(
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "Level is required") String level,
        @NotBlank(message = "Institution is required") String institution) {

    public CertificationEntity toEntity() {
        return new CertificationEntity(name, level, institution);
    }
}
