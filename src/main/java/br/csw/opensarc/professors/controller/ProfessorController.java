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

@RestController
@ControllerAdvice
@RequestMapping("/professors")
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @PutMapping("/{id}")
    @Operation(operationId = "updateByd", description = "Update professor by its id", responses = {
            @ApiResponse(responseCode = "200", description = "Updated Professor",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SimpleProfessor.class))),

            @ApiResponse(responseCode = "404", description = "Professor not found")
    })
    public ResponseEntity<SimpleProfessor> updateProfessor(@PathVariable("id") String id, @Valid @RequestBody ProfessorInput professorData) {
        Optional<SimpleProfessor> updatedProfessor = professorService.updateProfessor(id, professorData);
        return updatedProfessor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping
    public ResponseEntity<List<Professor>> getAll(@RequestParam(required = false) Map<String, String> filters) {
        return ResponseEntity.ok(professorService.getAll(filters));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Professor> getAll(@PathVariable("id") String id) {
        return ResponseEntity.of(professorService.getById(id));
    }

    @PostMapping
    public ResponseEntity<SimpleProfessor> createProfessor(@Valid @RequestBody ProfessorInput createProfessor) {
        return new ResponseEntity<>(professorService.create(createProfessor), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable("id") String id) {
        boolean deleted = professorService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}