package br.csw.opensarc.professors.repository.entity;

public record ProfessorCertificationEntity(String id, String professorId, String certificationId, String level,
                                           String name, String institution, String year, String semester) {
}
