package br.csw.opensarc.professors.repository;

import br.csw.opensarc.professors.controller.dto.ProfessorCertificationId;
import br.csw.opensarc.professors.controller.dto.ProfessorCertificationInput;
import br.csw.opensarc.professors.model.ProfessorCertification;
import br.csw.opensarc.professors.repository.entity.ProfessorCertificationEntity;
import br.csw.opensarc.professors.repository.mapper.ProfessorCertificationRowMapper;
import br.csw.opensarc.professors.service.exception.InsertError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProfessorCertificationRepository {
    private static final Logger log = LoggerFactory.getLogger(ProfessorCertificationRepository.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ProfessorCertificationRowMapper rowMapper;

    public ProfessorCertificationRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new ProfessorCertificationRowMapper();
    }

    public List<ProfessorCertificationEntity> getAll(String professorId) {
        String sql = """
                    SELECT id, professor_id, certification_id, level, name, institution, year, semester
                    FROM professors.professors_certifications pc
                        JOIN professors.certification c on pc.certification_id = c.id
                    where pc.professor_id = :professor_id;
                """;
        try {
            return jdbcTemplate.query(sql, new MapSqlParameterSource("professor_id", UUID.fromString(professorId)), rowMapper);
        } catch (DataAccessException exception) {
            return new ArrayList<>();
        }
    }

    public void insert(ProfessorCertificationId professorCertificationId,
                       ProfessorCertificationInput input) {

        String sql = """
                    insert into professors.professors_certifications (professor_id, certification_id, year, semester)
                    values (:professor_id, :certification_id, :year, :semester)
                """;
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("professor_id", professorCertificationId.getProfessorId())
                .addValue("certification_id", professorCertificationId.getCertificationId())
                .addValue("year", input.date())
                .addValue("semester", input.semester().name());
        try {
            jdbcTemplate.update(sql, mapSqlParameterSource);
        } catch (DataAccessException ex) {
            throw new InsertError(ex.getMessage());
        }
    }

    public Optional<ProfessorCertificationEntity> getById(ProfessorCertificationId professorCertificationId) {
        String sql = """
                    SELECT pc.id, pc.professor_id, pc.certification_id, c.level, c.name, c.institution, pc.year, pc.semester
                    FROM professors.professors_certifications pc
                        JOIN professors.certification c on pc.certification_id = c.id
                    where pc.professor_id = :professor_id
                    AND pc.certification_id = :certification_id
                """;
        try {
            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource("professor_id", professorCertificationId.getProfessorId())
                    .addValue("certification_id", professorCertificationId.getCertificationId());
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, mapSqlParameterSource, rowMapper));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    public void delete(ProfessorCertificationId professorCertificationId) {
        String sql = """
                    DELETE FROM professors.professors_certifications
                    where professor_id = :professor_id
                    AND certification_id = :certification_id
                """;

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource("professor_id", professorCertificationId.getProfessorId())
                .addValue("certification_id", professorCertificationId.getCertificationId());
        jdbcTemplate.update(sql, mapSqlParameterSource);
    }

    public void update(ProfessorCertificationId professorCertificationId, ProfessorCertificationInput input) {
        String sql = """
                    UPDATE professors.professors_certifications
                        set year = :year,
                            semester = :semester
                    where professor_id = :professor_id
                    AND certification_id = :certification_id
                """;
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("professor_id", professorCertificationId.getProfessorId())
                .addValue("certification_id", professorCertificationId.getCertificationId())
                .addValue("year", input.date())
                .addValue("semester", input.semester().name());
        try {
            jdbcTemplate.update(sql, mapSqlParameterSource);
        } catch (DataAccessException ex) {
            throw new InsertError(ex.getMessage());
        }
    }

    public void deleteAllForProfessor(String id) {
        String sql = """
                    DELETE FROM professors.professors_certifications
                    where professor_id = :professor_id
                """;

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource("professor_id", UUID.fromString(id));
        jdbcTemplate.update(sql, mapSqlParameterSource);
    }
}
