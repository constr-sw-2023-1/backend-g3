@RestController
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
                            schema = @Schema(implementation = ProfessorEntity.class))),

            @ApiResponse(responseCode = "404", description = "Professor not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class)))
    })
    
    public ResponseEntity<ProfessorEntity> updateProfessor(@PathVariable("id") String id, @RequestBody ProfessorInput professorData) {
        try {
            ProfessorEntity updatedProfessor = professorService.updateProfessor(id, professorData);
            return ResponseEntity.ok(updatedProfessor);
        } catch (NotFoundException ex) {
            Error error = new Error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}