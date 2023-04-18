package br.pucrs.csw.professors.config;

import br.pucrs.csw.professors.keycloak.KeyCloakUserAPIClientService;
import br.pucrs.csw.professors.keycloak.pojo.KeyCloakConfig;
import br.pucrs.csw.professors.service.UserService;
import br.pucrs.csw.professors.web.UserController;
import br.pucrs.csw.professors.web.error.ControllerExceptionHandler;
import br.pucrs.csw.professors.web.login.LoginController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

@Configuration
@Import({SecurityConfiguration.class, ControllerExceptionHandler.class})
public class AppConfiguration {

    @Value("${keycloak.auth.url}")
    private String keyCloakAuthBaseUrl;
    @Value("${keycloak.user.url}")
    private String keyCloakUserBaseUrl;
    @Value("${keycloak.client.id}")
    private String keyCloakClientId;
    @Value("${keycloak.client.secret}")
    private String keyCloakClientSecret;

    @Bean
    public KeyCloakUserAPIClientService keyCloakUserAPIClientService(RestTemplate restTemplate) {
        KeyCloakConfig keyCloakConfig = new KeyCloakConfig(keyCloakAuthBaseUrl, keyCloakUserBaseUrl,
                keyCloakClientId, keyCloakClientSecret);
        return new KeyCloakUserAPIClientService(restTemplate, keyCloakConfig);
    }

    @Bean
    public LoginController loginController(KeyCloakUserAPIClientService keyCloakUserAPIClientService) {
        return new LoginController(keyCloakUserAPIClientService);
    }

    @Bean
    public UserController userController(UserService userService) {
        return new UserController(userService);
    }

    @Bean
    public UserService userService(KeyCloakUserAPIClientService keyCloakUserAPIClientService) {
        return new UserService(keyCloakUserAPIClientService);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
