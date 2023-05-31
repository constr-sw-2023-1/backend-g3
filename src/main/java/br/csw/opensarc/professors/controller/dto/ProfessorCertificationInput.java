package br.csw.opensarc.professors.controller.dto;

import br.csw.opensarc.professors.model.Semester;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ProfessorCertificationInput(
        @NotBlank String id,
        @Pattern(regexp = "(19|20)\\d{2}",
                message = "Date should be on pattern yyyy-MM-dd") String year,
        @NotNull Semester semester
) {

    public ProfessorCertificationSimpleInput toSimple() {
        return new ProfessorCertificationSimpleInput(year, semester);
    }
}
