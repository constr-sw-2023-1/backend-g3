package br.csw.opensarc.professors.repository.entity;

import br.csw.opensarc.professors.model.ProfessorCertification;
import br.csw.opensarc.professors.model.Semester;

import java.util.List;

public record ProfessorCertificationEntity(String id, String professorId, String certificationId, String level,
                                           String name, String institution, String year, String semester) {
}
