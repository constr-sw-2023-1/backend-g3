package br.pucrs.csw.professors.keycloak.pojo;

public record KeyCloakConfig(String authBaseUrl, String userBaseUrl, String clientId, String clientSecret) {
}
