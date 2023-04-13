package br.pucrs.csw.professors.web;

import br.pucrs.csw.professors.pojo.PasswordUpdate;
import br.pucrs.csw.professors.pojo.Professor;
import br.pucrs.csw.professors.service.ProfessorService;
import br.pucrs.csw.professors.web.input.ProfessorInput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @PostMapping
    public ResponseEntity<Professor> create(@RequestBody ProfessorInput professorToCreate) {
        professorToCreate.isValid();
        Professor createdProfessor = professorService.create(professorToCreate.toProfessor());
        return ResponseEntity.ok(createdProfessor);
    }

    @GetMapping
    public ResponseEntity<List<Professor>> getAll() {
        List<Professor> professors = professorService.getAll();
        return ResponseEntity.ok(professors);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        professorService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Professor> getUserById(@PathVariable String id) {
        Professor professor = professorService.getUserById(id);
        return ResponseEntity.ok(professor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Professor> updateUser(@RequestBody ProfessorInput professorData, @PathVariable String id) {
        Professor updatedProfessor = professorService.updateUserById(id, professorData);
        return ResponseEntity.ok(updatedProfessor);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Professor> updatePassword(@RequestBody PasswordUpdate passwordUpdate, @PathVariable String id) {
        Professor updatedProfessor = professorService.updatePassword(id, passwordUpdate);
        return ResponseEntity.ok(updatedProfessor);
    }

}
