package br.pucrs.csw.professors.keycloak.pojo;

import br.pucrs.csw.professors.pojo.Professor;

import java.util.HashMap;
import java.util.Map;

public record KeyCloakUser(String id, String username, boolean enabled, boolean emailVerified, String firstName,
                           String lastName, String email, Map<String, String> attributes) {

    public Professor toProfessor() {
        Map<String, String> attr = attributes == null ? new HashMap<>() : attributes;
        return new Professor(id, username, firstName, lastName, attr.getOrDefault("password", "NOT_SETTED"));
    }

    public KeyCloakUser withEnabled(boolean enabled) {
        return new KeyCloakUser(id, username, enabled, emailVerified, firstName, lastName, email, attributes);
    }

    public KeyCloakUser withFirstName(String firstName) {
        return new KeyCloakUser(id, username, enabled, emailVerified,
                firstName != null ? firstName: this.firstName, lastName, email, attributes);
    }

    public KeyCloakUser withLastName(String lastName) {
        return new KeyCloakUser(id, username, enabled, emailVerified,
                firstName, lastName != null ? lastName : this.lastName, email, attributes);
    }

    public KeyCloakUser withEmail(String email) {
        return new KeyCloakUser(id, username, enabled, emailVerified,
                firstName, lastName, email != null ? email : this.email, attributes);
    }

    public KeyCloakUser withPassword(String password) {
        if (password != null) {
            attributes.put("password", password);
        }
        return this;
    }
}
