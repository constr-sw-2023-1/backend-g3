package br.pucrs.csw.professors.config;

import br.pucrs.csw.professors.web.error.ControllerExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

@Configuration
@Import({SecurityConfiguration.class, ControllerExceptionHandler.class})
public class AppConfiguration {


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
