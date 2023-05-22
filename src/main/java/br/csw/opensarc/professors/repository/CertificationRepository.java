package br.csw.opensarc.professors.repository;

import br.csw.opensarc.professors.repository.entity.CertificationEntity;
import br.csw.opensarc.professors.repository.mapper.CertificationRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CertificationRepository {

    private static final Logger log = LoggerFactory.getLogger(CertificationRepository.class);
    private static final String TABLE = "professors.professor";
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final CertificationRowMapper rowMapper;

    public CertificationRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new CertificationRowMapper();
    }

    public List<CertificationEntity> getByProfessorId(String professorId) {
        String sql = """
                    SELECT id, professor_id, year, level, description
                    from %s
                    where professor_id = :professor_id
                """;
        try {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource("professor_id", professorId);
            return jdbcTemplate.query(String.format(sql, TABLE), parameterSource, rowMapper);
        } catch (DataAccessException ex) {
            return new ArrayList<>();
        }
    }

    public Optional<CertificationEntity> create(CertificationEntity entity) {
        String sql = """
                    INSERT INTO %s (professor_id, year, level, description)
                    values (:professor_id, :year, :level, :description)
                    returning id
                """;
        try {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource(Map.of(
                    "professor_id", entity.professorId(),
                    "year", entity.year(),
                    "level", entity.level(),
                    "description", entity.description()
            ));
            String id = jdbcTemplate.queryForObject(String.format(sql, TABLE), parameterSource, String.class);
            return Optional.of(entity.withId(id));
        } catch (DataAccessException ex) {
            log.error("Error trying to create certification for professor: " + entity.professorId() +
                    " [" + entity + "]", ex);
            return Optional.empty();
        }
    }

    public Optional<CertificationEntity> getById(String professorId, String id) {
        String sql = """
                    SELECT id, professor_id, year, leve, description
                    from %s
                    where id = :id AND professor_id = :professor_id
                """;
        try {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
            parameterSource.addValue("professor_id", professorId);
            return Optional.ofNullable(jdbcTemplate.queryForObject(String.format(sql, TABLE), parameterSource, rowMapper));
        } catch (DataAccessException ex) {
            return Optional.empty();
        }
    }

    public Optional<CertificationEntity> update(String id, CertificationEntity entityToUpdate) {
        String sql = """
                    UPDATE %s
                        set year = :year,
                        level = :level,
                        description = :description
                    where id = :id AND professor_id = :professor_id
                    returning id, professor_id, year, level, description;
                """;
        try {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource(Map.of(
                    "id", id,
                    "professor_id", entityToUpdate.professorId(),
                    "year", entityToUpdate.year(),
                    "level", entityToUpdate.level(),
                    "description", entityToUpdate.description()
            ));
            return Optional.ofNullable(jdbcTemplate.queryForObject(String.format(sql, TABLE), parameterSource, rowMapper));
        } catch (DataAccessException ex) {
            return Optional.empty();
        }
    }

    public boolean delete(String professorId, String id) {
        String sql = """
                    DELETE FROM %s
                    where id = :id AND professor_id = :professor_id
                    returning id
                """;
        try {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
            parameterSource.addValue("professor_id", professorId);
            String deletedId = jdbcTemplate.queryForObject(String.format(sql, TABLE), parameterSource, String.class);
            return deletedId != null && !deletedId.isEmpty();
        } catch (DataAccessException ex) {
            log.error("Error trying to delete certification for professor: " + professorId +
                    " [id: " + id + "]", ex);
            return false;
        }
    }
}
