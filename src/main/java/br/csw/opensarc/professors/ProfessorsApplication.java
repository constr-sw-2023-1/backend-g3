package br.csw.opensarc.professors;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Sistema de gerenciamento de professores", version = "1.0.0"))
public class ProfessorsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProfessorsApplication.class, args);
	}

}
