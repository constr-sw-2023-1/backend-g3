package br.csw.opensarc.professors.repository;

import br.csw.opensarc.professors.repository.entity.ProfessorEntity;
import br.csw.opensarc.professors.repository.mapper.ProfessorRowMapper;
import org.springframework.transaction.annotation.Transactional;
import br.csw.opensarc.professors.repository.mapper.ProfessorIdentificationRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProfessorRepository {
    private static final String TABLE_NAME = "professors.identification";
    private static final Logger log = LoggerFactory.getLogger(IdentificationsRepository.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ProfessorRowMapper professorRowMapper;

    public ProfessorRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        professorRowMapper = new ProfessorRowMapper();
    }

    public Optional<ProfessorEntity> updateProfessor(String professorId, ProfessorEntity professor) {
        try {
            log.debug("Try to update professor: " + professorId);
            String sql = String.format("""
                    UPDATE %s
                    SET registration = :registration,
                        name = :name,
                        born_date = :bornDate,
                        admission_date = :admissionDate,
                        active = :active
                    WHERE id = :id
                    returning id, registration, name, born_date, admission_date, active
                    """, TABLE_NAME);
            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                    .addValue("id", professorId)
                    .addValue("registration", professor.registration())
                    .addValue("name", professor.name())
                    .addValue("bornDate", professor.bornDate())
                    .addValue("admissionDate", professor.admissionDate())
                    .addValue("active", professor.active());

            ProfessorEntity updatedProfessor = jdbcTemplate.queryForObject(String.format(sql, TABLE_NAME),
                    mapSqlParameterSource, professorRowMapper);

            return Optional.ofNullable(updatedProfessor);
        } catch (DataAccessException ex) {
            log.error("Error trying to update professor: " + professorId, ex);
            return Optional.empty();
        }
    }
        public Optional<ProfessorEntity> getProfessorById(String professorId) {
            try {
                log.debug("Try to get professor by ID: " + professorId);
                String sql = String.format("""
                    SELECT id, registration, name, born_date, admission_date, active
                    FROM %s
                    WHERE id = :id
                    """, TABLE_NAME);
                MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                        .addValue("id", professorId);

                return jdbcTemplate.query(sql, mapSqlParameterSource, professorRowMapper).stream().findFirst();
            } catch (DataAccessException ex) {
                log.error("Error trying to get professor by ID: " + professorId, ex);
                return Optional.empty();
            }
    }
}