package br.pucrs.csw.professors.keycloak.pojo;

import br.pucrs.csw.professors.pojo.User;

import java.util.List;

public record KeyCloakUser(String id, String username, boolean enabled, boolean emailVerified, String firstName,
                           String lastName, String email, List<KeyCloakCredential> credentials) {

    public User toUser() {
        return new User(id, username, firstName, lastName, credentials.get(0).value());
    }

    public KeyCloakUser withEnabled(boolean enabled) {
        return new KeyCloakUser(id, username, enabled, emailVerified, firstName, lastName, email, credentials);
    }

    public KeyCloakUser withFirstName(String firstName) {
        return new KeyCloakUser(id, username, enabled, emailVerified,
                firstName != null ? firstName : this.firstName, lastName, email, credentials);
    }

    public KeyCloakUser withLastName(String lastName) {
        return new KeyCloakUser(id, username, enabled, emailVerified,
                firstName, lastName != null ? lastName : this.lastName, email, credentials);
    }

    public KeyCloakUser withPassword(String password) {
        return new KeyCloakUser(id, username, enabled, emailVerified,
                firstName, lastName, email, List.of(credentials.get(0).withNewPass(password)));
    }
}
