package br.csw.opensarc.professors.service;

import br.csw.opensarc.professors.controller.dto.Identification;
import br.csw.opensarc.professors.controller.dto.ProfessorCertificationInput;
import br.csw.opensarc.professors.controller.dto.ProfessorInput;
import br.csw.opensarc.professors.model.Professor;
import br.csw.opensarc.professors.model.ProfessorCertification;
import br.csw.opensarc.professors.repository.IdentificationsRepository;
import br.csw.opensarc.professors.repository.ProfessorRepository;
import br.csw.opensarc.professors.repository.entity.IdentificationEntity;
import br.csw.opensarc.professors.repository.entity.ProfessorEntity;
import br.csw.opensarc.professors.service.dto.SearchFilters;
import br.csw.opensarc.professors.service.dto.SearchType;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProfessorService {
    private final ProfessorRepository professorRepository;
    private final IdentificationsRepository identificationsRepository;
    private final ProfessorCertificationService professorCertificationService;

    public ProfessorService(ProfessorRepository professorRepository,
                            IdentificationsRepository identificationsRepository,
                            ProfessorCertificationService professorCertificationService) {
        this.professorRepository = professorRepository;
        this.identificationsRepository = identificationsRepository;
        this.professorCertificationService = professorCertificationService;
    }

    @Transactional
    public Optional<Professor> updateProfessor(String id, ProfessorInput professorData) {
        return professorRepository.getById(id)
                .map(ignored -> {
                    ProfessorEntity entity = professorData.toProfessorEntity();
                    List<IdentificationEntity> identificationEntities = professorData.toIdentificationEntity();
                    List<Identification> updated = identificationsRepository.updateIdentifications(id, identificationEntities)
                            .stream().map(IdentificationEntity::toIdentification).toList();
                    professorCertificationService.deleteAllByProfessorId(id);
                    List<ProfessorCertification> certifications = professorCertificationService.createBatch(id, professorData.certifications());
                    ProfessorEntity updatedProfessor = professorRepository.updateProfessor(id, entity);

                    return updatedProfessor.toProfessor(updated, certifications);
                });
    }

    public List<Professor> getAll(Map<String, String> filters) {
        List<SearchFilters> searchFilters = generateFilters(filters);
        List<ProfessorEntity> entities = professorRepository.getAll(searchFilters);
        return entities.stream()
                .map(it -> {
                    List<Identification> identifications = identificationsRepository.getByProfessorId(it.id())
                            .stream().map(IdentificationEntity::toIdentification).toList();
                    List<ProfessorCertification> certifications = professorCertificationService.getAll(it.id());

                    return it.toProfessor(identifications, certifications);
                }).toList();
    }

    public Optional<Professor> getById(String id) {
        return professorRepository.getById(id)
                .map(it -> {
                    List<Identification> identifications = identificationsRepository.getByProfessorId(it.id())
                            .stream().map(IdentificationEntity::toIdentification).toList();
                    List<ProfessorCertification> certifications = professorCertificationService.getAll(it.id());

                    return it.toProfessor(identifications, certifications);
                });
    }

    @Transactional
    public Professor create(ProfessorInput createProfessor) {
        List<IdentificationEntity> identification = createProfessor.identifications()
                .stream().map(Identification::toEntity).toList();
        ProfessorEntity entity = professorRepository.create(createProfessor.toProfessorEntity());
        List<Identification> insertedIdentifications = identificationsRepository.createBatch(entity.id(), identification)
                .stream().map(IdentificationEntity::toIdentification).toList();
        List<ProfessorCertification> insertedCertifications = professorCertificationService.createBatch(entity.id(), createProfessor.certifications());
        return entity.toProfessor(insertedIdentifications, insertedCertifications);
    }

    @Transactional
    public boolean delete(String id) {
        return professorRepository.getById(id)
                .map(it -> {
                    professorRepository.delete(id);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public Optional<Professor> partialUpdate(String professorId, ProfessorInput professorInput) {
        return getById(professorId).map(professor -> {
            ProfessorInput toUpdate = mergeProfessors(professor, professorInput);
            List<Identification> identificationEntities = new ArrayList<>();
            List<ProfessorCertification> certificationEntities = new ArrayList<>();
            if (!toUpdate.identifications().isEmpty()) {
                identificationsRepository.deleteAllForProfessor(professorId);
                identificationEntities = identificationsRepository
                        .createBatch(professorId, toUpdate.toIdentificationEntity())
                        .stream()
                        .map(IdentificationEntity::toIdentification)
                        .toList();
                ;
            }
            if (!toUpdate.certifications().isEmpty()) {
                professorCertificationService.deleteAllByProfessorId(professorId);
                certificationEntities = professorCertificationService.createBatch(professorId, toUpdate.certifications());
            }
            return professorRepository.updateProfessor(professorId, toUpdate.toProfessorEntity())
                    .toProfessor(identificationEntities, certificationEntities);
        });
    }

    private ProfessorInput mergeProfessors(Professor professor, ProfessorInput professorInput) {
        List<ProfessorCertificationInput> certificationToInput = professorInput.certifications() != null
                ? professorInput.certifications()
                : professor.certifications().stream().map(it -> new ProfessorCertificationInput(it.certificationId(), it.year(), it.semester())).toList();
        return new ProfessorInput(
                professorInput.registration() != null ? professorInput.registration() : professor.registration(),
                professorInput.name() != null ? professorInput.name() : professor.name(),
                professorInput.bornDate() != null ? professorInput.bornDate() : professor.bornDate(),
                professorInput.admissionDate() != null ? professorInput.admissionDate() : professor.admissionDate(),
                professorInput.active() != null ? professorInput.active() : professor.active(),
                certificationToInput,
                professorInput.identifications() != null ? professorInput.identifications() : professor.identifications()
        );
    }

    private List<SearchFilters> generateFilters(Map<String, String> filters) {
        List<String> filterTypesId = SearchType.allIds();
        return filters.entrySet()
                .stream()
                .map(it -> {
                    String valueWithId = it.getValue();
                    SearchType searchType = filterTypesId.stream()
                            .filter(valueWithId::startsWith)
                            .findFirst()
                            .flatMap(SearchType::ofType)
                            .orElse(SearchType.EQUAL);
                    String value = valueWithId.replace(searchType.getId(), "");
                    return new SearchFilters(it.getKey(), value, searchType);
                }).toList();
    }
}
