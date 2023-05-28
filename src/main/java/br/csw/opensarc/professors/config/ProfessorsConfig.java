package br.csw.opensarc.professors.config;

import br.csw.opensarc.professors.controller.CertificationController;
import br.csw.opensarc.professors.controller.ProfessorController;
import br.csw.opensarc.professors.repository.CertificationRepository;
import br.csw.opensarc.professors.repository.IdentificationsRepository;
import br.csw.opensarc.professors.repository.ProfessorRepository;
import br.csw.opensarc.professors.service.CertificationProfessorService;
import br.csw.opensarc.professors.service.ProfessorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class ProfessorsConfig {

    @Bean
    public CertificationController certificationController(CertificationProfessorService certificationProfessorService) {
        return new CertificationController(certificationProfessorService);
    }

    @Bean
    public ProfessorController professorController(ProfessorService professorService) {
        return new ProfessorController(professorService);
    }

    @Bean
    public ProfessorService professorService(ProfessorRepository professorRepository, IdentificationsRepository identificationsRepository) {
        return new ProfessorService(professorRepository, identificationsRepository);
    }

    @Bean
    public CertificationProfessorService certificationService(CertificationRepository certificationRepository) {
        return new CertificationProfessorService(certificationRepository);
    }

    @Bean
    public ProfessorRepository professorRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new ProfessorRepository(jdbcTemplate);
    }

    @Bean
    public IdentificationsRepository identificationsRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new IdentificationsRepository(jdbcTemplate);
    }

    @Bean
    public CertificationRepository certificationRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new CertificationRepository(jdbcTemplate);
    }
}