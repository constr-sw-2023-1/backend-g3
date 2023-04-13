package br.pucrs.csw.professors.keycloak.pojo;

public record KeyCloakCredential(String type, String value) {

    public KeyCloakCredential withNewPass(String newPass) {
        return new KeyCloakCredential(type, newPass);
    }
}
