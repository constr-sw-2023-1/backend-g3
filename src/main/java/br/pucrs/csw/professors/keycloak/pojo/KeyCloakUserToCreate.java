package br.pucrs.csw.professors.keycloak.pojo;

import br.pucrs.csw.professors.pojo.Professor;

import java.util.Map;

public record KeyCloakUserToCreate(String username, boolean enabled, boolean emailVerified, String firstName, String lastName, String email, Map<String, String> attributes){
    public KeyCloakUserToCreate(Professor professor) {
        this(professor.username(), true, true, professor.firstName(), professor.lastName(),
                professor.username(), Map.of("password", professor.password()));
    }
}
