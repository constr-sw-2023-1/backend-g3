package br.csw.opensarc.professors.controller;

import br.csw.opensarc.professors.controller.dto.ErrorMessage;
import br.csw.opensarc.professors.controller.dto.ProfessorInput;
import br.csw.opensarc.professors.controller.dto.SimpleProfessor;
import br.csw.opensarc.professors.model.Professor;
import br.csw.opensarc.professors.service.ProfessorService;
import br.csw.opensarc.professors.service.exception.InsertError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@ControllerAdvice
@RequestMapping("/professors")
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update professor by ID")
    @ApiResponse(responseCode = "200", description = "Updated Professor",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = SimpleProfessor.class)))
    @ApiResponse(responseCode = "404", description = "Professor not found")
    public ResponseEntity<SimpleProfessor> updateProfessor(
            @Parameter(description = "ID of the professor to update", required = true)
            @PathVariable("id") String id,
            @Valid @RequestBody ProfessorInput professorData) {
        Optional<SimpleProfessor> updatedProfessor = professorService.updateProfessor(id, professorData);
        return updatedProfessor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all professors")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<List<Professor>> getAll(
            @Parameter(description = "Query filters to apply", example = "name=Arthur&department=Math")
            @RequestParam(required = false) Map<String, String> filters) {
        return ResponseEntity.ok(professorService.getAll(filters));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get professor by ID")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Professor.class)))
    @ApiResponse(responseCode = "404", description = "Professor not found")
    public ResponseEntity<Professor> getById(
            @Parameter(description = "ID of the professor to retrieve", required = true)
            @PathVariable("id") String id) {
        return ResponseEntity.of(professorService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new professor")
    @ApiResponse(responseCode = "201", description = "Created",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = SimpleProfessor.class)))
    public ResponseEntity<SimpleProfessor> createProfessor(
            @Parameter(description = "Professor data", required = true)
            @Valid @RequestBody ProfessorInput createProfessor) {
        return new ResponseEntity<>(professorService.create(createProfessor), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete professor by ID")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "404", description = "Professor not found")
    public ResponseEntity<Void> deleteProfessor(
            @Parameter(description = "ID of the professor to delete", required = true)
            @PathVariable("id") String id) {
        boolean deleted = professorService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
