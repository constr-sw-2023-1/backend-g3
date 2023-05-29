package br.csw.opensarc.professors.controller.dto;

import java.util.UUID;

public record ProfessorCertificationId(String professorId, String certificationId) {

    public UUID getProfessorId() {
        return UUID.fromString(professorId);
    }

    public UUID getCertificationId() {
        return UUID.fromString(certificationId);
    }
}
