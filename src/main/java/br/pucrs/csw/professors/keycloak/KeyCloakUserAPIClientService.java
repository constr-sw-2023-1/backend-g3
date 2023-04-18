package br.pucrs.csw.professors.keycloak;

import br.pucrs.csw.professors.exceptions.InvalidUserOrPasswordLoginException;
import br.pucrs.csw.professors.exceptions.UnauthorizedOperation;
import br.pucrs.csw.professors.exceptions.UserNameAlreadyExists;
import br.pucrs.csw.professors.keycloak.pojo.*;
import br.pucrs.csw.professors.pojo.User;
import br.pucrs.csw.professors.security.RequestContext;
import br.pucrs.csw.professors.web.login.LoginRequest;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;

public class KeyCloakUserAPIClientService {

    private final RestTemplate restTemplate;
    private final KeyCloakConfig keyCloakConfig;

    public KeyCloakUserAPIClientService(RestTemplate restTemplate, KeyCloakConfig keyCloakConfig) {
        this.restTemplate = restTemplate;
        this.keyCloakConfig = keyCloakConfig;
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
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, httpHeaders);
        try {
            ResponseEntity<LoginResponse> response = restTemplate.postForEntity(keyCloakConfig.authBaseUrl(),
                    entity, LoginResponse.class);
            return response.getBody();
        } catch (HttpStatusCodeException ex) {
            throw ex.getStatusCode().value() == 401
                    ? new InvalidUserOrPasswordLoginException()
                    : ex;
        }
    }

    public User create(User userToCreate) {
        KeyCloakUserToCreate keyCloakUser = new KeyCloakUserToCreate(userToCreate);
        HttpHeaders httpHeaders = buildAuthHeader();
        HttpEntity<KeyCloakUserToCreate> entity = new HttpEntity<>(keyCloakUser, httpHeaders);
        try {
            URI location = restTemplate.postForLocation(keyCloakConfig.userBaseUrl(), entity);
            String[] locationParts = location.getPath().split("/");
            String createdId = locationParts[locationParts.length - 1];
            return userToCreate.withId(createdId);
        } catch (HttpClientErrorException ex) {
            switch (ex.getStatusCode().value()) {
                case 403 -> throw new UnauthorizedOperation();
                case 409 -> throw new UserNameAlreadyExists(userToCreate.username());
                default -> throw ex;
            }
        }
    }

    public List<KeyCloakUser> getAll() {
        HttpHeaders httpHeaders = buildAuthHeader();
        HttpEntity<Void> entity = new HttpEntity<>(httpHeaders);
        try {
            ResponseEntity<KeyCloakUsersResponse[]> response = restTemplate.exchange(keyCloakConfig.userBaseUrl(),
                    HttpMethod.GET, entity, KeyCloakUsersResponse[].class);
            if (response.getBody() == null) return new ArrayList<>();
            return Arrays.stream(response.getBody())
                    .map(KeyCloakUsersResponse::toKeyCloakUser)
                    .filter(KeyCloakUser::enabled)
                    .toList();
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode().value() == 403) {
                throw new UnauthorizedOperation();
            }
            throw ex;
        }
    }

    public void update(String id, KeyCloakUser keyCloakUser) {
        HttpHeaders httpHeaders = buildAuthHeader();
        HttpEntity<KeyCloakUser> entity = new HttpEntity<>(keyCloakUser, httpHeaders);
        try {
            restTemplate.exchange(keyCloakConfig.userBaseUrl() + "/" + id,
                    HttpMethod.PUT, entity, KeyCloakUser.class);
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode().value() == 403) {
                throw new UnauthorizedOperation();
            }
            throw ex;
        }
    }

    private HttpHeaders buildAuthHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(RequestContext.getAccessToken());
        return httpHeaders;
    }
}
