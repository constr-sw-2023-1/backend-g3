package br.csw.opensarc.professors.controller;

import br.csw.opensarc.professors.controller.dto.CertificationInput;
import br.csw.opensarc.professors.controller.dto.ErrorMessage;
import br.csw.opensarc.professors.controller.dto.ProfessorCertificationId;
import br.csw.opensarc.professors.controller.dto.ProfessorCertificationInput;
import br.csw.opensarc.professors.model.ProfessorCertification;
import br.csw.opensarc.professors.service.ProfessorCertificationService;
import br.csw.opensarc.professors.service.exception.InsertError;
import br.csw.opensarc.professors.service.exception.ProfessorOrCertificationNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ControllerAdvice
@RequestMapping("/professors/{professorId}/certifications")
public class ProfessorCertificationController {

    private final ProfessorCertificationService service;

    public ProfessorCertificationController(ProfessorCertificationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProfessorCertification>> getAll(@PathVariable("professorId") String professorId) {
        return ResponseEntity.ok(service.getAll(professorId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorCertification> getByIds(@PathVariable("professorId") String professorId,
                                                           @PathVariable("id") String certificationId) {
        return ResponseEntity.of(service.getById(new ProfessorCertificationId(professorId, certificationId)));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ProfessorCertification> createCertification(@PathVariable("professorId") String professorId,
                                                                      @PathVariable("id") String certificationId,
                                                                      @Valid @RequestBody ProfessorCertificationInput input) {
        return ResponseEntity.ok(service.createCertification(new ProfessorCertificationId(professorId, certificationId), input));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCertification(@PathVariable("professorId") String professorId,
                                                    @PathVariable("id") String certificationId) {
        service.delete(new ProfessorCertificationId(professorId, certificationId));
        return ResponseEntity.noContent().build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorCertification> editCertification(@PathVariable("professorId") String professorId,
                                                                    @PathVariable("id") String certificationId,
                                                                    @Valid @RequestBody ProfessorCertificationInput certificationInput) {
        return ResponseEntity.ok(service.updateCertification(
                new ProfessorCertificationId(professorId, certificationId), certificationInput)
        );
    }

    @ExceptionHandler(ProfessorOrCertificationNotFoundException.class)
    public ResponseEntity<ErrorMessage> handelProfessorNotFound(ProfessorOrCertificationNotFoundException exception) {
        return new ResponseEntity<>(
                new ErrorMessage("Professor or Certification with ids: " + exception.getId() + " could not be found", "USR-01"),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(InsertError.class)
    public ResponseEntity<ErrorMessage> handelInsertError(InsertError exception) {
        return new ResponseEntity<>(
                new ErrorMessage(exception.getMessage(), "POST-01"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
