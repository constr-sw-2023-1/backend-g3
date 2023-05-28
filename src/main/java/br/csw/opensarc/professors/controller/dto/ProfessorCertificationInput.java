package br.csw.opensarc.professors.controller.dto;

import br.csw.opensarc.professors.model.Semester;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ProfessorCertificationInput(
        @Pattern(
                regexp = "\\d{4}-(0\\d|1[012])-(0\\d|[12]\\d|3[01])",
                message = "Date should be on pattern yyyy-MM-dd") String date,
        @NotNull Semester semester) {
}
