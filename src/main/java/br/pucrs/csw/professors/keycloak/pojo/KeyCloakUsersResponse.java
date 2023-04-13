package br.pucrs.csw.professors.keycloak.pojo;

import br.pucrs.csw.professors.pojo.Professor;

import java.util.ArrayList;
import java.util.List;

public record KeyCloakUsersResponse(String id, String username, boolean enabled, boolean emailVerified,
                                    String firstName, String lastName, String email) {

    public Professor toProfessor() {
        return new Professor(id, username, firstName, lastName, null);
    }

    public KeyCloakUser toKeyCloakUser() {
        return new KeyCloakUser(id, username, enabled, emailVerified, firstName, lastName, email, List.of(new KeyCloakCredential("***", "***")));
    }
}
