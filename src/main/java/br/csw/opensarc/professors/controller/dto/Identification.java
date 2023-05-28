package br.csw.opensarc.professors.controller.dto;

import br.csw.opensarc.professors.repository.entity.IdentificationEntity;

public record Identification (String type, String value) {

    public IdentificationEntity toEntity() {
        return new IdentificationEntity(type, value);
    }
}
