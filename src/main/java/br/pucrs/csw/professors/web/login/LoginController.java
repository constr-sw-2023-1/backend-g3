package br.pucrs.csw.professors.web.login;

import br.pucrs.csw.professors.keycloak.KeyCloakUserAPIClientService;
import br.pucrs.csw.professors.keycloak.pojo.LoginResponse;
import br.pucrs.csw.professors.web.error.ErrorDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@RestController
@RequestMapping("/login")
public class LoginController {

    private KeyCloakUserAPIClientService keyCloakUserAPIClientService;

    public LoginController(KeyCloakUserAPIClientService keyCloakUserAPIClientService) {
        this.keyCloakUserAPIClientService = keyCloakUserAPIClientService;
    }

    @PostMapping(consumes = APPLICATION_FORM_URLENCODED_VALUE,
            produces = {"application/json"})
    @Operation(operationId = "login", description = "Authenticate The User",
            requestBody = @RequestBody(content = @Content(mediaType = APPLICATION_FORM_URLENCODED_VALUE)),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Generates Bearer token for specified login",
                            content = @Content(schema = @Schema(implementation = LoginResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid User or Login",
                            content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
            }
    )
    @SecurityRequirements
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        LoginResponse response = keyCloakUserAPIClientService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
