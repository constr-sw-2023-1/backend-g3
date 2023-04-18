package br.pucrs.csw.professors;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "btoken", scheme = "bearer", bearerFormat = "jwt", in = SecuritySchemeIn.HEADER)
@OpenAPIDefinition(info = @Info(title = "Sistema de criação de usuario grupo 3", version = "1.0.0"),
        security = { @SecurityRequirement(name = "btoken") })
public class ProfessorsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProfessorsApplication.class, args);
    }

}
