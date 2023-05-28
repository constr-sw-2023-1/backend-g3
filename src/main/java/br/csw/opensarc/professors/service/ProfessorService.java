package br.csw.opensarc.professors.service;

import br.csw.opensarc.professors.controller.dto.Identification;
import br.csw.opensarc.professors.controller.dto.ProfessorInput;
import br.csw.opensarc.professors.controller.dto.SimpleProfessor;
import br.csw.opensarc.professors.model.Professor;
import br.csw.opensarc.professors.repository.IdentificationsRepository;
import br.csw.opensarc.professors.repository.ProfessorCertificationRepository;
import br.csw.opensarc.professors.repository.ProfessorRepository;
import br.csw.opensarc.professors.repository.entity.IdentificationEntity;
import br.csw.opensarc.professors.repository.entity.ProfessorCertificationEntity;
import br.csw.opensarc.professors.repository.entity.ProfessorEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class ProfessorService {
    private final ProfessorRepository professorRepository;
    private final IdentificationsRepository identificationsRepository;
    private final ProfessorCertificationRepository professorCertificationRepository;

    public ProfessorService(ProfessorRepository professorRepository,
                            IdentificationsRepository identificationsRepository,
                            ProfessorCertificationRepository professorCertificationRepository) {
        this.professorRepository = professorRepository;
        this.identificationsRepository = identificationsRepository;
        this.professorCertificationRepository = professorCertificationRepository;
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

    public List<Professor> getAll() {
        List<ProfessorEntity> entities = professorRepository.getAll();
        return entities.stream()
                .map(it -> {
                    List<IdentificationEntity> identificationEntities = identificationsRepository.getByProfessorId(it.id());
                    List<ProfessorCertificationEntity> certificationEntities = professorCertificationRepository.getAll(it.id());

                    return it.toProfessor(identificationEntities, certificationEntities);
                }).toList();
    }

    public Optional<Professor> getById(String id) {
        return professorRepository.getById(id)
                .map(it -> {
                    List<IdentificationEntity> identificationEntities = identificationsRepository.getByProfessorId(it.id());
                    List<ProfessorCertificationEntity> certificationEntities = professorCertificationRepository.getAll(it.id());

                    return it.toProfessor(identificationEntities, certificationEntities);
                });
    }

    @Transactional
    public SimpleProfessor create(ProfessorInput createProfessor) {
        List<IdentificationEntity> identification = createProfessor.identifications()
                .stream().map(Identification::toEntity).toList();
        ProfessorEntity entity = professorRepository.create(createProfessor.toProfessorEntity());
        List<IdentificationEntity> insertedIdentifications = identificationsRepository.createBatch(entity.id(), identification);
        return entity.toSimpleProfessor(insertedIdentifications.stream().map(IdentificationEntity::toIdentification).toList());
    }
}
