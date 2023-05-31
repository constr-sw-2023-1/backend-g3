package br.csw.opensarc.professors.controller;

import br.csw.opensarc.professors.controller.dto.ErrorMessage;
import br.csw.opensarc.professors.controller.dto.ProfessorCertificationId;
import br.csw.opensarc.professors.controller.dto.ProfessorCertificationSimpleInput;
import br.csw.opensarc.professors.model.ProfessorCertification;
import br.csw.opensarc.professors.service.ProfessorCertificationService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ResourceBundle;

@RestController
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
                    array = @ArraySchema(schema = @Schema(implementation = ProfessorCertification.class))))
    public ResponseEntity<List<ProfessorCertification>> getAll(
            @PathVariable("professorId") String professorId) {
        return ResponseEntity.ok(service.getAll(professorId));
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get a certification by ID for a professor")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProfessorCertification.class)))
    @ApiResponse(responseCode = "404", description = "Certification or professor not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class))
    )
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
    @ApiResponse(responseCode = "404", description = "Certification or professor not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class))
    )
    public ResponseEntity<ProfessorCertification> createCertification(
            @PathVariable("professorId") String professorId,
            @PathVariable("id") String certificationId,
            @Valid @RequestBody ProfessorCertificationSimpleInput input) {
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
    @ApiResponse(responseCode = "404", description = "Certification or professor not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class))
    )
    public ResponseEntity<ProfessorCertification> editCertification(
            @PathVariable("professorId") String professorId,
            @PathVariable("id") String certificationId,
            @Valid @RequestBody ProfessorCertificationSimpleInput certificationInput) {
        return ResponseEntity.ok(service.updateCertification(
                new ProfessorCertificationId(professorId, certificationId), certificationInput)
        );
    }
}
