package br.pucrs.csw.professors.keycloak.pojo;

import br.pucrs.csw.professors.pojo.Professor;

import java.util.List;
import java.util.Map;

public record KeyCloakUserToCreate(String username, boolean enabled, boolean emailVerified, String firstName,
                                   String lastName, String email, List<KeyCloakCredential> credentials) {
    public KeyCloakUserToCreate(Professor professor) {
        this(professor.username(), true, true, professor.firstName(), professor.lastName(),
                professor.username(), List.of(new KeyCloakCredential("password", professor.password())));
    }
}
