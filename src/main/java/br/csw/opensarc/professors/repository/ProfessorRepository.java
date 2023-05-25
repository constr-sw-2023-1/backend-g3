package br.csw.opensarc.professors.repository;

import org.springframework.transaction.annotation.Transactional;
import br.csw.opensarc.professors.repository.dto.IdentificationEntity;
import br.csw.opensarc.professors.repository.mapper.ProfessorIdentificationRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class ProfessorRepository {
    private static final String TABLE_NAME = "professors.identification";
    private static final Logger log = LoggerFactory.getLogger(IdentificationsRepository.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ProfessorIdentificationRowMapper identificationRowMapper;
    private final ProfessorRowMapper professorRowMapper;

    public ProfessorRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        identificationRowMapper = new ProfessorIdentificationRowMapper();
        professorRowMapper = new ProfessorRowMapper();
    }


    @Transactional
    public Optional<ProfessorEntity> updateProfessor(ProfessorEntity professor) {
        try {
            log.debug("Try to update professor: " + professor.getId());
            String sql = String.format("""
                    UPDATE %s
                    SET registration = :registration,
                        name = :name,
                        born_date = :bornDate,
                        admission_date = :admissionDate,
                        active = :active
                    WHERE id = :id
                    returning *
                    """, TABLE_NAME);
            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                    .addValue("id", professor.getId())
                    .addValue("registration", professor.getRegistration())
                    .addValue("name", professor.getName())
                    .addValue("bornDate", professor.getBornDate())
                    .addValue("admissionDate", professor.getAdmissionDate())
                    .addValue("active", professor.isActive());
    
            ProfessorEntity updatedProfessor = (jdbcTemplate.queryForObject(sql, mapSqlParameterSource), professorRowMapper);
    
            List<IdentificationEntity> updatedIdentifications = updateIdentifications(professor.getId(), professor.getIdentification());
            return Optional.of(professor.withIdentification(updateIdentifications);

        } catch (DataAccessException ex) {
            log.error("Error trying to update professor: " + professor.getId(), ex);
            return Optional.empty();
        }
    
    }
    
    private List<IdentificationEntity> updateIdentifications(String professorId, List<IdentificationEntity> identifications) {
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
                        .addValue("type", identification.getType())
                        .addValue("value", identification.getValue());
                updatedIdentifications.add(jdbcTemplate.queryForObject(String.format(insertSql, TABLE_NAME), insertParams, identificationRowMapper));
            }
            return updatedIdentifications;

        } catch (DataAccessException ex) {
            log.error("Error trying to update identifications of professor: " + professorId, ex);
            return new ArrayList<>();
        }
    }
}