package br.csw.opensarc.professors.repository.entity;

import br.csw.opensarc.professors.controller.dto.Identification;
import br.csw.opensarc.professors.controller.dto.SimpleProfessor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record ProfessorEntity(String id, String registration, String name, LocalDate bornDate, LocalDate admissionDate,
                              boolean active) {
    public ProfessorEntity(String registration, String name, LocalDate bornDate, LocalDate admissionDate, boolean active) {
        this(null, registration, name, bornDate, admissionDate, active);
    }

    public SimpleProfessor toSimpleProfessor(List<Identification> identifications) {
        return new SimpleProfessor(id, registration, name, bornDate, admissionDate, active, identifications);
    }
}