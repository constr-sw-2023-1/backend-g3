package br.csw.opensarc.professors.model;

import br.csw.opensarc.professors.controller.dto.Identification;

import java.time.LocalDate;
import java.util.List;

public record Professor(String id, String registration, String name, LocalDate bornDate,
                        LocalDate admissionDate, boolean active,
                        List<Identification> identifications,
                        List<ProfessorCertification> certifications) {
}
