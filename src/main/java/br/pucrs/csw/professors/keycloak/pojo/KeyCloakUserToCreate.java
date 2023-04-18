package br.pucrs.csw.professors.keycloak.pojo;

import br.pucrs.csw.professors.pojo.User;

import java.util.List;

public record KeyCloakUserToCreate(String username, boolean enabled, boolean emailVerified, String firstName,
                                   String lastName, String email, List<KeyCloakCredential> credentials) {
    public KeyCloakUserToCreate(User user) {
        this(user.username(), true, true, user.firstName(), user.lastName(),
                user.username(), List.of(new KeyCloakCredential("password", user.password())));
    }
}
