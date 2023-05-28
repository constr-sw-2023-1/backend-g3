package br.csw.opensarc.professors.repository.entity;

import br.csw.opensarc.professors.controller.dto.Identification;

public record IdentificationEntity (String id, String type, String value, String professorId) {

    public IdentificationEntity(String type, String value, String professorId) {
        this(null, type, value, professorId);
    }

    public IdentificationEntity(String type, String value) {
        this(null, type, value, null);
    }

    public Identification toIdentification() {
        return new Identification(type, value);
    }
}
