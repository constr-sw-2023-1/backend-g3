package br.csw.opensarc.professors.controller;

import br.csw.opensarc.professors.controller.dto.CertificationInput;
import br.csw.opensarc.professors.controller.dto.ErrorMessage;
import br.csw.opensarc.professors.model.Certification;
import br.csw.opensarc.professors.service.CertificatioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ControllerAdvice
@RequestMapping("/certifications")
public class CertificationController {

    private final CertificatioService certificatioService;

    public CertificationController(CertificatioService certificatioService) {
        this.certificatioService = certificatioService;
    }

    @GetMapping
    @Operation(operationId = "getAllCertifications", description = "Retrive all certifications",
            responses = {
                    @ApiResponse(responseCode = "200", description = "All Users",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Certification.class)))
                    )
            })
    public ResponseEntity<List<Certification>> getAll() {
        return ResponseEntity.ok(certificatioService.getAll());
    }


    @PostMapping
    @Operation(operationId = "createCertification", description = "Create certification",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Created certification",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Certification.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Invalid body on request",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Certification.class))
                    )
            }
    )
    public ResponseEntity<Certification> createCertification(@Valid @RequestBody CertificationInput certificationInput) {
        return ResponseEntity.of(certificatioService.createCertification(certificationInput));
    }

    @Operation(operationId = "getById", description = "Search CertificationById",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Certification of id",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Certification.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Invalid Id")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Certification> getById(@PathVariable("id") String id) {
        return ResponseEntity.of(certificatioService.getById(id));
    }

    @Operation(operationId = "getById", description = "Delete certification by Id",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Certification Deleted"),
                    @ApiResponse(responseCode = "404", description = "Invalid Id")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCertification(@PathVariable("id") String id) {
        boolean deleted = certificatioService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @Operation(operationId = "Update by Id", description = "Update certification by Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Certification Updated",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Certification.class)
                            )),
                    @ApiResponse(responseCode = "404", description = "Invalid Id")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Certification> editCertification(@PathVariable("id") String id,
                                                           @Valid @RequestBody CertificationInput certificationInput) {
        return ResponseEntity.of(certificatioService.updateCertification(id, certificationInput));
    }

    //Fazer um PATCH
}
