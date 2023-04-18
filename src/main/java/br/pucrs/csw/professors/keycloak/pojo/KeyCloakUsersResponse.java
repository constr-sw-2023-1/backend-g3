package br.pucrs.csw.professors.keycloak.pojo;

import br.pucrs.csw.professors.pojo.User;

import java.util.List;

public record KeyCloakUsersResponse(String id, String username, boolean enabled, boolean emailVerified,
                                    String firstName, String lastName, String email) {

    public KeyCloakUser toKeyCloakUser() {
        return new KeyCloakUser(id, username, enabled, emailVerified, firstName, lastName, email,
                List.of(new KeyCloakCredential("***", "***")));
    }
}
