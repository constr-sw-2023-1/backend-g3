package br.csw.opensarc.professors.repository;

import br.csw.opensarc.professors.repository.entity.IdentificationEntity;
import br.csw.opensarc.professors.repository.mapper.ProfessorIdentificationRowMapper;
import br.csw.opensarc.professors.service.exception.InsertError;
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

    public List<IdentificationEntity> getByProfessorId(String professorId) {
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

    public List<IdentificationEntity> createBatch(String professorId, List<IdentificationEntity> entities) {
        try {
            String sql = "INSERT INTO %s (professor_id, type, value) VALUES (:professor_id, :type, :value) returning *";

            List<IdentificationEntity> returnEntities = new ArrayList<>();
            for (IdentificationEntity entity : entities) {
                MapSqlParameterSource parameterSource = new MapSqlParameterSource("professor_id", professorId)
                        .addValue("type", entity.type())
                        .addValue("value", entity.value());
                IdentificationEntity returned = jdbcTemplate.queryForObject(String.format(sql, TABLE_NAME), parameterSource, rowMapper);
                returnEntities.add(returned);
            }
            return returnEntities;
        } catch (DataAccessException ex) {
            log.error("Error trying to insert indentifications for professors: " + professorId);
            throw new InsertError(ex.getMessage());
        }
    }

    public List<IdentificationEntity> updateIdentifications(String professorId, List<IdentificationEntity> identifications) {
        try {
            log.debug("Try to update identifications of professor: " + professorId);
            String deleteSql = "DELETE FROM %s WHERE professor_id = :professor_id";
            String insertSql = "INSERT INTO %s (professor_id, type, value) VALUES (:professor_id, :type, :value) returning *";

            MapSqlParameterSource deleteParams = new MapSqlParameterSource("professor_id", professorId);
            jdbcTemplate.update(String.format(deleteSql, TABLE_NAME), deleteParams);


            List<IdentificationEntity> updatedIdentifications = new ArrayList<>();

            for (IdentificationEntity identification : identifications) {
                MapSqlParameterSource insertParams = new MapSqlParameterSource()
                        .addValue("professor_id", professorId)
                        .addValue("type", identification.type())
                        .addValue("value", identification.value());
                updatedIdentifications.add(jdbcTemplate.queryForObject(String.format(insertSql, TABLE_NAME),
                        insertParams, rowMapper));
            }
            return updatedIdentifications;
        } catch (DataAccessException ex) {
            log.error("Error trying to update identifications of professor: " + professorId, ex);
            return new ArrayList<>();
        }
    }

    public void deleteAllForProfessor(String id) {
        String deleteSql = "DELETE FROM %s WHERE professor_id = :professor_id";
        MapSqlParameterSource deleteParams = new MapSqlParameterSource("professor_id", id);
        jdbcTemplate.update(String.format(deleteSql, TABLE_NAME), deleteParams);
    }
}
    
