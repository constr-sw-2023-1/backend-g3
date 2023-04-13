package br.pucrs.csw.professors.web.login;

import br.pucrs.csw.professors.keycloak.KeyCloakUserAPIClientService;
import br.pucrs.csw.professors.keycloak.pojo.LoginResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    private KeyCloakUserAPIClientService keyCloakUserAPIClientService;

    public LoginController(KeyCloakUserAPIClientService keyCloakUserAPIClientService) {
        this.keyCloakUserAPIClientService = keyCloakUserAPIClientService;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        LoginResponse response = keyCloakUserAPIClientService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
