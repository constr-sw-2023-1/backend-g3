package br.csw.opensarc.professors.controller;

import br.csw.opensarc.professors.controller.dto.ErrorMessage;
import br.csw.opensarc.professors.controller.dto.ProfessorInput;
import br.csw.opensarc.professors.model.Professor;
import br.csw.opensarc.professors.service.ProfessorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
    @Operation(operationId = "Update by Id", description = "Update professor by Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Professor Updated",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Professor.class)
                            )),
                    @ApiResponse(responseCode = "400", description = "Invalid fields",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Invalid Id")
            }
    )
    public ResponseEntity<Professor> updateProfessor(
            @PathVariable("id") String id,
            @Valid @RequestBody ProfessorInput professorData) {
        Optional<Professor> updatedProfessor = professorService.updateProfessor(id, professorData);
        return updatedProfessor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    @Operation(operationId = "Patch by id", description = "Partial update professor by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Updated professor",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Professor.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Id not found")
            }
    )
    public ResponseEntity<Professor> partialUpdate(@PathVariable("id") String professorId,
                                                   @RequestBody ProfessorInput professorInput) {
        return ResponseEntity.of(professorService.partialUpdate(professorId, professorInput));
    }

    @GetMapping
    @Operation(summary = "Get all professors",
            description = "Used to filter the professor can be passed expanded passing queryParameters with the following pattern\n" +
                    "Condição\tOperador " +
                    "{op}\tExemplo\n" +
                    "Equals\t\tstatus=Liberado\n" +
                    "Not equals\tneq\tstatus={neq}Reservado\n" +
                    "Greater than\tgt\tvalue={gt}20\n" +
                    "Greater than equals\tgteq\tinitial_date={gteq}2022-11-08\n" +
                    "Less than\tlt\tfinal_date={lt}2022-11-30\n" +
                    "Less than equals\tlteq\tquantity={lteq}2.5\n" +
                    "Like\tlike\tdescription={like}%database%"
    )
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = Professor.class))
            )
    )
    @Parameter(name = "id", description = "Query filters to Filter the id", in = ParameterIn.QUERY)
    @Parameter(name = "registration", description = "Query filters to Filter the registration", in = ParameterIn.QUERY)
    @Parameter(name = "name", description = "Query filters to Filter the name", in = ParameterIn.QUERY)
    @Parameter(name = "bornDate", description = "Query filters to Filter the bornDate", in = ParameterIn.QUERY)
    @Parameter(name = "admissionDate", description = "Query filters to Filter the admissionDate", in = ParameterIn.QUERY)
    @Parameter(name = "active", description = "Query filters to Filter the active", in = ParameterIn.QUERY)
    public ResponseEntity<List<Professor>> getAll(
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
                    schema = @Schema(implementation = Professor.class)))
    @ApiResponse(responseCode = "400", description = "Invalid fields",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class))
    )
    public ResponseEntity<Professor> createProfessor(
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
