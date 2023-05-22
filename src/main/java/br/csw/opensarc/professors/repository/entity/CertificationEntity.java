package br.csw.opensarc.professors.repository.entity;

public record CertificationEntity(String id, String professorId, int year, String level, String description) {

    public CertificationEntity(String professorId, int year, String level, String description) {
        this(null, professorId, year, level, description);
    }
    public CertificationEntity withId(String id) {
        return new CertificationEntity(id, this.professorId, this.year, this.level, this.description);
    }
}
