package br.csw.opensarc.professors.controller;

import br.csw.opensarc.professors.controller.dto.CertificationInput;
import br.csw.opensarc.professors.controller.dto.ErrorMessage;
import br.csw.opensarc.professors.controller.dto.ProfessorCertificationId;
import br.csw.opensarc.professors.controller.dto.ProfessorCertificationInput;
import br.csw.opensarc.professors.model.ProfessorCertification;
import br.csw.opensarc.professors.service.ProfessorCertificationService;
import br.csw.opensarc.professors.service.exception.InsertError;
import br.csw.opensarc.professors.service.exception.ProfessorOrCertificationNotFoundException;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Get all certifications for a professor")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProfessorCertification.class)))
    public ResponseEntity<List<ProfessorCertification>> getAll(
            @Parameter(in = ParameterIn.QUERY, name = "id", description = "User ID", schema = @Schema(type = "string"))
            @PathVariable("professorId") String professorId) {
        return ResponseEntity.ok(service.getAll(professorId));
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get a certification by ID for a professor")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProfessorCertification.class)))
    @ApiResponse(responseCode = "404", description = "Certification not found")
    public ResponseEntity<ProfessorCertification> getByIds(
            @PathVariable("professorId") String professorId,
            @PathVariable("id") String certificationId) {
        return ResponseEntity.of(service.getById(new ProfessorCertificationId(professorId, certificationId)));
    }

    @PostMapping("/{id}")
    @Operation(summary = "Create a certification for a professor")
    @ApiResponse(responseCode = "200", description = "Created",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProfessorCertification.class)))
    public ResponseEntity<ProfessorCertification> createCertification(
            @PathVariable("professorId") String professorId,
            @PathVariable("id") String certificationId,
            @Valid @RequestBody ProfessorCertificationInput input) {
        return ResponseEntity.ok(service.createCertification(new ProfessorCertificationId(professorId, certificationId), input));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a certification for a professor")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "404", description = "Certification not found")
    public ResponseEntity<Void> deleteCertification(
            @PathVariable("professorId") String professorId,
            @PathVariable("id") String certificationId) {
        service.delete(new ProfessorCertificationId(professorId, certificationId));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a certification for a professor")
    @ApiResponse(responseCode = "200", description = "Updated Certification",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProfessorCertification.class)))
    public ResponseEntity<ProfessorCertification> editCertification(
            @PathVariable("professorId") String professorId,
            @PathVariable("id") String certificationId,
            @Valid @RequestBody ProfessorCertificationInput certificationInput) {
        return ResponseEntity.ok(service.updateCertification(
                new ProfessorCertificationId(professorId, certificationId), certificationInput)
        );
    }

    public ResponseEntity<ErrorMessage> handelProfessorNotFound(
            ProfessorOrCertificationNotFoundException exception) {
        return new ResponseEntity<>(
                new ErrorMessage("Professor or Certification with ids: " + exception.getId() + " could not be found", "USR-01"),
                HttpStatus.NOT_FOUND
        );
    }
}
