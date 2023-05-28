package br.csw.opensarc.professors.service.exception;

import br.csw.opensarc.professors.controller.dto.ProfessorCertificationId;

public class ProfessorOrCertificationNotFoundException extends RuntimeException{
    private final ProfessorCertificationId id;

    public ProfessorOrCertificationNotFoundException(ProfessorCertificationId id) {
        this.id = id;
    }

    public ProfessorCertificationId getId() {
        return id;
    }
}
