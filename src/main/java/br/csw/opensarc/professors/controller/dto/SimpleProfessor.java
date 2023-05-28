package br.csw.opensarc.professors.controller.dto;

import java.time.LocalDate;
import java.util.List;

public record SimpleProfessor(String id, String registration, String name, LocalDate bornDate, LocalDate admissionDate,
                              boolean active, List<Identification> identifications) {
}
