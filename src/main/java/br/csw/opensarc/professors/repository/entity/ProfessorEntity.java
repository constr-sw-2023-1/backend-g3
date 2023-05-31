package br.csw.opensarc.professors.repository.entity;

import br.csw.opensarc.professors.controller.dto.Identification;
import br.csw.opensarc.professors.model.Professor;
import br.csw.opensarc.professors.model.ProfessorCertification;

import java.time.LocalDate;
import java.util.List;

public record ProfessorEntity(String id, String registration, String name, LocalDate bornDate, LocalDate admissionDate,
                              boolean active) {
    public ProfessorEntity(String registration, String name, LocalDate bornDate, LocalDate admissionDate, boolean active) {
        this(null, registration, name, bornDate, admissionDate, active);
    }

    public Professor toProfessor(List<Identification> identifications, List<ProfessorCertification> certifications) {
        return new Professor(id, registration, name, bornDate, admissionDate,
                active,
                identifications,
                certifications
        );
    }
}