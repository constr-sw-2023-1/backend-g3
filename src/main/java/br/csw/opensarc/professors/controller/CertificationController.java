package br.csw.opensarc.professors.controller;

import br.csw.opensarc.professors.controller.dto.CertificationInput;
import br.csw.opensarc.professors.model.Certification;
import br.csw.opensarc.professors.service.CertificatioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certifications")
public class CertificationController {

    private final CertificatioService certificatioService;

    public CertificationController(CertificatioService certificatioService) {
        this.certificatioService = certificatioService;
    }

    @GetMapping
    public ResponseEntity<List<Certification>> getAll() {
        return ResponseEntity.ok(certificatioService.getAll());
    }

    @PostMapping
    public ResponseEntity<Certification> createCertification(@Valid @RequestBody CertificationInput certificationInput) {
        return ResponseEntity.of(certificatioService.createCertification(certificationInput));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Certification> getById(@PathVariable("id") String id) {
        return ResponseEntity.of(certificatioService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCertification(@PathVariable("id") String id) {
        boolean deleted = certificatioService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Certification> editCertification(@PathVariable("id") String id,
                                                           @Valid @RequestBody CertificationInput certificationInput) {
        return ResponseEntity.of(certificatioService.updateCertification(id, certificationInput));
    }
}
