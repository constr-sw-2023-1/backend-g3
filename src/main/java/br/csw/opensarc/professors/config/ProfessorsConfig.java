package br.csw.opensarc.professors.config;

import br.csw.opensarc.professors.controller.CertificationController;
import br.csw.opensarc.professors.repository.CertificationRepository;
import br.csw.opensarc.professors.service.CertificationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class ProfessorsConfig {

    @Bean
    public CertificationController certificationController(CertificationService certificationService) {
        return new CertificationController(certificationService);
    }

    @Bean
    public CertificationService certificationService(CertificationRepository certificationRepository) {
        return new CertificationService(certificationRepository);
    }

    @Bean
    public CertificationRepository certificationRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new CertificationRepository(jdbcTemplate);
    }
}
