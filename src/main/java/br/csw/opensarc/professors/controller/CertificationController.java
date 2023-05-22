package br.csw.opensarc.professors.controller;

import br.csw.opensarc.professors.controller.dto.CertificationInput;
import br.csw.opensarc.professors.model.Certification;
import br.csw.opensarc.professors.service.CertificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/professors/{professorId}/certifications")
public class CertificationController {

    private CertificationService certificationService;

    public CertificationController(CertificationService certificationService) {
        this.certificationService = certificationService;
    }

    @PostMapping
    public ResponseEntity<Certification> createCertification(@PathVariable("professorId") String professorId,
                                                             @Valid @RequestBody CertificationInput certificationInput) {
        return ResponseEntity.of(Optional.empty());
    }

    @GetMapping
    public ResponseEntity<List<Certification>> getAllOfProfessor(@PathVariable("professorId") String professorId) {
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Certification> getById(@PathVariable("professorId") String professorId,
                                                 @PathVariable("id") String id) {
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Certification> deleteCertification(@PathVariable("professorId") String professorId,
                                                             @PathVariable("id") String id) {
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Certification> editCertification(@PathVariable("professorId") String professorId,
                                                           @PathVariable("id") String id,
                                                           @Valid @RequestBody CertificationInput certificationInput) {
        return ResponseEntity.notFound().build();
    }
}
