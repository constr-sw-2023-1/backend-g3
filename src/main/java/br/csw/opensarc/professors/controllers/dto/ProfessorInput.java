package br.csw.opensarc.professors.controllers.dto;

import br.csw.opensarc.professors.repository.entity.CertificationEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProfessorInput(
        @NotBlank(message = "Registration is required") String registration,
        @NotBlank(message = "Name is required") String name,
        @NotNull(message = "Born date is required") LocalDate bornDate,
        @NotNull(message = "Admission date is required") LocalDate admissionDate,
        @NotNull(message = "Active is required") boolean active),
        @NotNull(message = "Identifications are required") List<IdentificationInput> identifications {


    public ProfessorEntity toEntity(String professorId) {
        return new ProfessorEntity(professorId, registration, name, bornDate, admissionDate, active, identifications);
    }
}