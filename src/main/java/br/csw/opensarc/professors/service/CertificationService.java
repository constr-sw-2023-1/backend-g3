package br.csw.opensarc.professors.service;

import br.csw.opensarc.professors.controller.dto.CertificationInput;
import br.csw.opensarc.professors.model.Certification;
import br.csw.opensarc.professors.repository.CertificationRepository;
import br.csw.opensarc.professors.repository.entity.CertificationEntity;

import java.util.List;
import java.util.Optional;

public class CertificationService {

    private final CertificationRepository certificationRepository;

    public CertificationService(CertificationRepository certificationRepository) {
        this.certificationRepository = certificationRepository;
    }

    public List<Certification> getAllByProfessor(String professorId) {
        return certificationRepository.getByProfessorId(professorId)
                .stream()
                .map(CertificationEntity::toCertification)
                .toList();
    }

    public Optional<Certification> getById(String professorId, String id) {
        return certificationRepository.getById(professorId, id)
                .map(CertificationEntity::toCertification);
    }

    public Optional<Certification> createCertification(String professorId, CertificationInput input) {
        return certificationRepository.create(input.toEntity(professorId))
                .map(CertificationEntity::toCertification);
    }

    public Optional<Certification> updateCertification(String id, String professorId, CertificationInput input) {
        return getById(professorId, id)
                .flatMap(it -> {
                    CertificationEntity toUpdate = new CertificationEntity(
                            professorId, input.year(), input.level(), input.description()
                    );
                    return certificationRepository.update(id, toUpdate);
                }).map(CertificationEntity::toCertification);
    }

    public boolean delete(String id, String professorId) {
        return getById(professorId, id)
                .map(it -> certificationRepository.delete(professorId, id))
                .orElse(false);
    }
}
