package br.csw.opensarc.professors.service;

import br.csw.opensarc.professors.controller.dto.ProfessorInput;
import br.csw.opensarc.professors.controller.dto.SimpleProfessor;
import br.csw.opensarc.professors.repository.IdentificationsRepository;
import br.csw.opensarc.professors.repository.ProfessorRepository;
import br.csw.opensarc.professors.repository.entity.IdentificationEntity;
import br.csw.opensarc.professors.repository.entity.ProfessorEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class ProfessorService {
    private final ProfessorRepository professorRepository;
    private final IdentificationsRepository identificationsRepository;

    public ProfessorService(ProfessorRepository professorRepository, IdentificationsRepository identificationsRepository) {
        this.professorRepository = professorRepository;
        this.identificationsRepository = identificationsRepository;
    }

    @Transactional
    public Optional<SimpleProfessor> updateProfessor(String id, ProfessorInput professorData) {
        ProfessorEntity entity = professorData.toProfessorEntity();
        List<IdentificationEntity> identificationEntities = professorData.getIdentificationEntity();
        List<IdentificationEntity> updated = identificationsRepository.updateIdentifications(id, identificationEntities);

        Optional<ProfessorEntity> updatedProfessor = professorRepository.updateProfessor(id, entity);

        return updatedProfessor.map(it ->
                it.toSimpleProfessor(updated.stream()
                        .map(IdentificationEntity::toIdentification)
                        .toList())
        );
    }
}
