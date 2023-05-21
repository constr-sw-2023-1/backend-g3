package br.csw.opensarc.professors.repository;

import br.csw.opensarc.professors.repository.dto.IdentificationEntity;
import br.csw.opensarc.professors.repository.mapper.ProfessorIdentificationRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class IdentificationsRepository {
    private static final String TABLE_NAME = "professors.identification";
    private static final Logger log = LoggerFactory.getLogger(IdentificationsRepository.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ProfessorIdentificationRowMapper rowMapper;

    public IdentificationsRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        rowMapper = new ProfessorIdentificationRowMapper();
    }

    List<IdentificationEntity> getByProfessorId(String professorId) {
        try {
            log.debug("Try to get identifications of professor: " + professorId);
            String sql = """
                    SELECT id, professor_id, type, "value"
                    from %s
                    where professor_id = :professor_id
                    """;
            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource("professor_id", professorId);
            return jdbcTemplate.query(sql, mapSqlParameterSource, rowMapper);
        } catch (DataAccessException ex) {
            log.error("Error trying to get identifications of professors: " + professorId, ex);
            return new ArrayList<>();
        }
    }

    public void updateProfessor(ProfessorEntity professor) {
        try {
            log.debug("Try to update professor: " + professor.getId());
            String sql = """
                    UPDATE %s
                    SET registration = :registration,
                        name = :name,
                        born_date = :bornDate,
                        admission_date = :admissionDate,
                        active = :active
                    WHERE id = :id
                    """;
            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                    .addValue("id", professor.getId())
                    .addValue("registration", professor.getRegistration())
                    .addValue("name", professor.getName())
                    .addValue("bornDate", professor.getBornDate())
                    .addValue("admissionDate", professor.getAdmissionDate())
                    .addValue("active", professor.isActive());
    
            jdbcTemplate.update(sql, mapSqlParameterSource);
    
            // Atualizar as identificações
            updateIdentifications(professor.getId(), professor.getIdentification());
    
        } catch (DataAccessException ex) {
            log.error("Error trying to update professor: " + professor.getId(), ex);
            throw new RuntimeException("Failed to update professor");
        }
    }
    
    private void updateIdentifications(UUID professorId, List<IdentificationEntity> identifications) {
        try {
            log.debug("Try to update identifications of professor: " + professorId);
            String deleteSql = "DELETE FROM %s WHERE professor_id = :professor_id";
            String insertSql = "INSERT INTO %s (professor_id, type, value) VALUES (:professor_id, :type, :value)";
    
            MapSqlParameterSource deleteParams = new MapSqlParameterSource("professor_id", professorId);
            jdbcTemplate.update(String.format(deleteSql, TABLE_NAME), deleteParams);
    
            for (IdentificationEntity identification : identifications) {
                MapSqlParameterSource insertParams = new MapSqlParameterSource()
                        .addValue("professor_id", professorId)
                        .addValue("type", identification.getType())
                        .addValue("value", identification.getValue());
                jdbcTemplate.update(String.format(insertSql, TABLE_NAME), insertParams);
            }
        } catch (DataAccessException ex) {
            log.error("Error trying to update identifications of professor: " + professorId, ex);
            throw new RuntimeException("Failed to update identifications");
        }
    }
    
