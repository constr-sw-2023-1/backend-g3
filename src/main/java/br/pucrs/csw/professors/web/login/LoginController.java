package br.pucrs.csw.professors.web.login;

import br.pucrs.csw.professors.keycloak.KeyCloakUserAPIClientService;
import br.pucrs.csw.professors.keycloak.pojo.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/professors/login")
public class LoginController {

    private KeyCloakUserAPIClientService keyCloakUserAPIClientService;

    public LoginController(KeyCloakUserAPIClientService keyCloakUserAPIClientService) {
        this.keyCloakUserAPIClientService = keyCloakUserAPIClientService;
    }

    //@TODO tratamento de erro.
    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = keyCloakUserAPIClientService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
