package br.pucrs.csw.professors.web;

import br.pucrs.csw.professors.pojo.Professor;
import br.pucrs.csw.professors.service.ProfessorService;
import br.pucrs.csw.professors.web.input.ProfessorInput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/professors")
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

    @PutMapping("/{id}")
    //@TOdo terminar
    public ResponseEntity<Professor> deleteUser(@PathVariable("id") String id, @RequestBody ProfessorInput input) {
        Optional<Professor> professorUpdated = professorService.update(id, input);
        return ResponseEntity.noContent().build();
    }
    /*
    @Todo Fazer PUT /{id} (Executar put no keycloak)
    @Todo Fazer PATCH /{id} (Buscar pelo id, modificar o que veio na request, fazer put no keycloak)
    @Todo Fazer GET /{id} (Buscar pelo id (caso flag enable fase, devolver 404)
    Usar estrutura acima como um "exemplo"
     */

//
//    @GetMapping("/users")
//    public ResponseEntity<List<User>> getUsers(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(required = false) Boolean enabled) {
//        try {
//            String accessToken = getTokenFromAuthorizationHeader(authorizationHeader);
//            List<User> users = keycloakClient.getUsers(accessToken, enabled);
//            return ResponseEntity.ok(users);
//        } catch (KeycloakClientException e) {
//            return ResponseEntity.status(e.getStatus()).build();
//        }
//    }
//
//    @GetMapping("/users/{id}")
//    public ResponseEntity<User> getUserById(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String id) {
//        try {
//            String accessToken = getTokenFromAuthorizationHeader(authorizationHeader);
//            User user = keycloakClient.getUserById(accessToken, id);
//            if (user == null) {
//                return ResponseEntity.notFound().build();
//            } else {
//                return ResponseEntity.ok(user);
//            }
//        } catch (KeycloakClientException e) {
//            return ResponseEntity.status(e.getStatus()).build();
//        }
//    }
//
//    @PutMapping("/users/{id}")
//    public ResponseEntity<Void> updateUser(@RequestBody User user, @RequestHeader("Authorization") String authorizationHeader, @PathVariable String id) {
//        try {
//            String accessToken = getTokenFromAuthorizationHeader(authorizationHeader);
//            keycloakClient.updateUser(accessToken, id, user.getUsername(), user.getFirstName(), user.getLastName(), user.isEnabled());
//            return ResponseEntity.ok().build();
//        } catch (KeycloakClientException e) {
//            return ResponseEntity.status(e.getStatus()).build();
//        }
//    }
//
//    @PatchMapping("/users/{id}")
//    public ResponseEntity<Void> updatePassword(@RequestBody PasswordUpdate passwordUpdate, @RequestHeader("Authorization") String authorizationHeader, @PathVariable String id) {
//        try {
//            String accessToken = getTokenFromAuthorizationHeader(authorizationHeader);
//            keycloakClient.updatePassword(accessToken, id, passwordUpdate.getPassword());
//            return ResponseEntity
//        } catch (KeycloakClientException e) {
//            return ResponseEntity.status(e.getStatus()).build();
//        }
//    }
//
//    @DeleteMapping("/users/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long id, @RequestHeader("Authorization") String token) {
//        // Verificar se o usuário com o ID especificado existe no sistema
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
//
//        // Enviar uma requisição DELETE para o Keycloak para desabilitar o usuário
//        try {
//            Keycloak.getInstance()
//                    .realm(keycloakRealm)
//                    .users()
//                    .delete(user.getUsername());
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete user", e);
//        }
//
//        // Retornar o código de status 200 se a requisição foi bem sucedida
//        return ResponseEntity.ok().build();
//    }
}