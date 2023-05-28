package br.csw.opensarc.professors.repository.entity;

import br.csw.opensarc.professors.model.Certification;

public record CertificationEntity(String id, String name, String level, String institution) {

    public CertificationEntity(String name, String level, String institution) {
        this(null, name, level, institution);
    }

    public CertificationEntity withId(String id) {
        return new CertificationEntity(id, name, level, institution);
    }

    public Certification toCertification() {
        return new Certification(id, name, level, institution);
    }
}
