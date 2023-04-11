package br.pucrs.csw.professors.keycloak;

import br.pucrs.csw.professors.exceptions.UnauthorizedOperation;
import br.pucrs.csw.professors.exceptions.UserNameAlreadyExists;
import br.pucrs.csw.professors.keycloak.pojo.KeyCloakConfig;
import br.pucrs.csw.professors.keycloak.pojo.KeyCloakUser;
import br.pucrs.csw.professors.keycloak.pojo.LoginResponse;
import br.pucrs.csw.professors.pojo.Professor;
import br.pucrs.csw.professors.security.RequestContext;
import br.pucrs.csw.professors.web.login.LoginRequest;
import org.apache.juli.logging.Log;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;

public class KeyCloakUserAPIClientService {

    private RestTemplate restTemplate;
    private KeyCloakConfig keyCloakConfig;

    public KeyCloakUserAPIClientService(RestTemplate restTemplate, KeyCloakConfig keyCloakConfig) {
        this.restTemplate = restTemplate;
        this.keyCloakConfig = keyCloakConfig;
    }


    public Professor create(Professor professorToCreate) {
        KeyCloakUser keyCloakUser = new KeyCloakUser(professorToCreate);
        HttpHeaders httpHeaders = buildAuthHeader();
        HttpEntity<KeyCloakUser> entity = new HttpEntity<>(keyCloakUser, httpHeaders);
        try {
            URI location = restTemplate.postForLocation(keyCloakConfig.userBaseUrl(), entity);
            String[] locationParts = location.getPath().split("/");
            String createdId = locationParts[locationParts.length - 1];
            return professorToCreate.withId(createdId);
        } catch (HttpClientErrorException ex) {
            switch (ex.getStatusCode().value()) {
                case 403 -> throw new UnauthorizedOperation();
                case 409 -> throw new UserNameAlreadyExists(professorToCreate.username());
                default -> throw ex;
            }
        }
    }

    public LoginResponse login(LoginRequest request) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.put("client_id", Collections.singletonList(keyCloakConfig.clientId()));
        body.put("client_secret", Collections.singletonList(keyCloakConfig.clientSecret()));
        body.put("username", Collections.singletonList(request.username()));
        body.put("password", Collections.singletonList(request.password()));
        body.put("grant_type", Collections.singletonList("password"));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body,httpHeaders);
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(keyCloakConfig.authBaseUrl(),
                entity, LoginResponse.class);
        return response.getBody();
    }

    private HttpHeaders buildAuthHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(RequestContext.getAccessToken());
        return httpHeaders;
    }
}
