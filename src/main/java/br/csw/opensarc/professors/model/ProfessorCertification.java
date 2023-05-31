package br.csw.opensarc.professors.model;

import br.csw.opensarc.professors.repository.entity.ProfessorCertificationEntity;

public record ProfessorCertification(String certificationId, String level, String institution, String name, String year,
                                     Semester semester) {
    public static ProfessorCertification fromEntity(ProfessorCertificationEntity professorCertificationEntity) {
        return new ProfessorCertification(
                professorCertificationEntity.certificationId(),
                professorCertificationEntity.level(),
                professorCertificationEntity.institution(),
                professorCertificationEntity.name(),
                professorCertificationEntity.year(),
                Semester.valueOf(professorCertificationEntity.semester()));

    }
}
