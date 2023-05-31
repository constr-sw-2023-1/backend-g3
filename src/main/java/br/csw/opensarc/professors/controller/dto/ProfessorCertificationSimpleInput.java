package br.csw.opensarc.professors.controller.dto;

import br.csw.opensarc.professors.model.Semester;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ProfessorCertificationSimpleInput(
        @Pattern(
                regexp = "(19|20)\\d{2}",
                message = "Year should be in [1900;2099]") String year,
        @NotNull Semester semester) {
}
