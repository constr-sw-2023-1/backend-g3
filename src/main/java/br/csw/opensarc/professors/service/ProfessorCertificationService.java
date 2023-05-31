package br.csw.opensarc.professors.service;

import br.csw.opensarc.professors.controller.dto.ProfessorCertificationId;
import br.csw.opensarc.professors.controller.dto.ProfessorCertificationInput;
import br.csw.opensarc.professors.controller.dto.ProfessorCertificationSimpleInput;
import br.csw.opensarc.professors.model.ProfessorCertification;
import br.csw.opensarc.professors.repository.CertificationRepository;
import br.csw.opensarc.professors.repository.ProfessorCertificationRepository;
import br.csw.opensarc.professors.repository.ProfessorRepository;
import br.csw.opensarc.professors.service.exception.CertificationNotFoundException;
import br.csw.opensarc.professors.service.exception.ProfessorOrCertificationNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class ProfessorCertificationService {

    private final ProfessorCertificationRepository repository;
    private final CertificationRepository certificationRepository;
    private final ProfessorRepository professorRepository;

    public ProfessorCertificationService(ProfessorCertificationRepository repository,
                                         CertificationRepository certificationRepository,
                                         ProfessorRepository professorRepository) {
        this.repository = repository;
        this.certificationRepository = certificationRepository;
        this.professorRepository = professorRepository;
    }

    public List<ProfessorCertification> getAll(String professorId) {
        return repository.getAll(professorId)
                .stream()
                .map(ProfessorCertification::fromEntity)
                .toList();
    }

    public Optional<ProfessorCertification> getById(ProfessorCertificationId professorCertificationId) {
        validateIfExists(professorCertificationId);
        return repository.getById(professorCertificationId)
                .map(ProfessorCertification::fromEntity);
    }

    public void deleteAllByProfessorId(String professorId) {
        repository.deleteAllForProfessor(professorId);
    }

    @Transactional
    public ProfessorCertification createCertification(ProfessorCertificationId professorCertificationId,
                                                      ProfessorCertificationSimpleInput input) {
        validateIfExists(professorCertificationId);
        repository.insert(professorCertificationId, input);
        return getById(professorCertificationId).orElseThrow();
    }

    @Transactional
    public ProfessorCertification updateCertification(ProfessorCertificationId professorCertificationId,
                                                      ProfessorCertificationSimpleInput certificationInput) {
        validateIfExists(professorCertificationId);
        repository.update(professorCertificationId, certificationInput);
        return getById(professorCertificationId).orElseThrow();
    }

    @Transactional
    public void delete(ProfessorCertificationId professorCertificationId) {
        validateIfExists(professorCertificationId);
        repository.delete(professorCertificationId);
    }

    private void validateIfExists(ProfessorCertificationId professorCertificationId) {
        professorRepository.getById(professorCertificationId.professorId())
                .flatMap((ignored) -> certificationRepository.getById(professorCertificationId.certificationId()))
                .orElseThrow(() -> new ProfessorOrCertificationNotFoundException(professorCertificationId));
    }

    public List<ProfessorCertification> createBatch(String professorId, List<ProfessorCertificationInput> certificationsInput) {
        for (ProfessorCertificationInput input: certificationsInput) {
            certificationRepository.getById(input.id())
                    .orElseThrow(() -> new CertificationNotFoundException(input.id()));
            repository.insert(new ProfessorCertificationId(professorId, input.id()), input.toSimple());
        }
        return repository.getAll(professorId).stream().map(ProfessorCertification::fromEntity).toList();
    }
}
