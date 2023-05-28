package br.csw.opensarc.professors.controller.dto;

import br.csw.opensarc.professors.repository.entity.IdentificationEntity;
import br.csw.opensarc.professors.repository.entity.ProfessorEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record ProfessorInput(
        @NotBlank(message = "Registration is required") String registration,
        @NotBlank(message = "Name is required") String name,
        @NotNull(message = "Born date is required") LocalDate bornDate,
        @NotNull(message = "Admission date is required") LocalDate admissionDate,
        @NotNull(message = "Active is required") Boolean active,
        @NotNull(message = "Identifications are required") List<Identification> identifications) {


    public ProfessorEntity toProfessorEntity() {
        return new ProfessorEntity(registration, name, bornDate, admissionDate, active);
    }

    public List<IdentificationEntity> getIdentificationEntity() {
        return identifications.stream().map(Identification::toEntity).toList();
    }
}