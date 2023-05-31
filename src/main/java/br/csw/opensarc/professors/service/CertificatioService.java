package br.csw.opensarc.professors.service;

import br.csw.opensarc.professors.controller.dto.CertificationInput;
import br.csw.opensarc.professors.model.Certification;
import br.csw.opensarc.professors.repository.CertificationRepository;
import br.csw.opensarc.professors.repository.entity.CertificationEntity;

import java.util.List;
import java.util.Optional;

public class CertificatioService {

    private final CertificationRepository certificationRepository;

    public CertificatioService(CertificationRepository certificationRepository) {
        this.certificationRepository = certificationRepository;
    }

    public Optional<Certification> getById(String id) {
        return certificationRepository.getById(id)
                .map(CertificationEntity::toCertification);
    }

    public Optional<Certification> createCertification(CertificationInput input) {
        return certificationRepository.create(input.toEntity())
                .map(CertificationEntity::toCertification);
    }

    public Optional<Certification> updateCertification(String id, CertificationInput input) {
        return getById(id)
                .flatMap(it -> certificationRepository.update(id, input.toEntity()))
                .map(CertificationEntity::toCertification);
    }

    public boolean delete(String id) {
        return getById(id)
                .map(it -> certificationRepository.delete(id))
                .orElse(false);
    }

    public List<Certification> getAll() {
        return certificationRepository.getAll()
                .stream()
                .map(CertificationEntity::toCertification)
                .toList();
    }

    public Optional<Certification> updatePartialCertification(String id, CertificationInput input) {
        return getById(id)
                .flatMap(it -> {
                    CertificationEntity toUpdate = new CertificationEntity(
                            input.name() != null ? input.name() : it.name(),
                            input.level() != null ? input.level() : it.level(),
                            input.institution() != null ? input.institution() : it.institution()
                    );
                    return certificationRepository.update(id, toUpdate);
                })
                .map(CertificationEntity::toCertification);

    }
}
