package br.csw.opensarc.professors.repository;

import br.csw.opensarc.professors.repository.entity.CertificationEntity;
import br.csw.opensarc.professors.repository.mapper.CertificationRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.*;

public class CertificationRepository {

    private static final Logger log = LoggerFactory.getLogger(CertificationRepository.class);
    private static final String TABLE = "professors.certification";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final CertificationRowMapper rowMapper;

    public CertificationRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new CertificationRowMapper();
    }

    public Optional<CertificationEntity> create(CertificationEntity entity) {
        String sql = """
                    INSERT INTO %s (level, name, institution)
                    values (:level, :name, :institution)
                    returning id
                """;
        try {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource(Map.of(
                    "name", entity.name(),
                    "level", entity.level(),
                    "institution", entity.institution()
            ));
            String id = jdbcTemplate.queryForObject(String.format(sql, TABLE), parameterSource, String.class);
            return Optional.of(entity.withId(id));
        } catch (DataAccessException ex) {
            log.error("Error trying to create certification " +
                    " [" + entity + "]", ex);
            return Optional.empty();
        }
    }

    public Optional<CertificationEntity> getById(String id) {
        String sql = """
                    SELECT id, name, level, institution
                    from %s
                    where id = :id
                """;
        try {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource("id", UUID.fromString(id));
            return Optional.ofNullable(jdbcTemplate.queryForObject(String.format(sql, TABLE), parameterSource, rowMapper));
        } catch (DataAccessException ex) {
            return Optional.empty();
        }
    }

    public Optional<CertificationEntity> update(String id, CertificationEntity entityToUpdate) {
        String sql = """
                    UPDATE %s
                        set name = :name,
                        level = :level,
                        institution = :institution
                    where id = :id
                    returning id, name, level, institution;
                """;
        try {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource(Map.of(
                    "id", UUID.fromString(id),
                    "name", entityToUpdate.name(),
                    "level", entityToUpdate.level(),
                    "institution", entityToUpdate.institution()
            ));
            return Optional.ofNullable(jdbcTemplate.queryForObject(String.format(sql, TABLE), parameterSource, rowMapper));
        } catch (DataAccessException ex) {
            return Optional.empty();
        }
    }

    public boolean delete(String id) {
        String sql = """
                    DELETE FROM %s
                    where id = :id
                    returning id
                """;
        try {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource("id", UUID.fromString(id));
            String deletedId = jdbcTemplate.queryForObject(String.format(sql, TABLE), parameterSource, String.class);
            return deletedId != null && !deletedId.isEmpty();
        } catch (DataAccessException ex) {
            log.error("Error trying to delete certification" +
                    " [id: " + id + "]", ex);
            return false;
        }
    }

    public List<CertificationEntity> getAll() {
        String sql = """
                    SELECT id, name, level, institution
                    FROM %s
                """;
        try {
            return jdbcTemplate.query(String.format(sql, TABLE), rowMapper);
        } catch (DataAccessException ex) {
            log.error("Error trying to get all certifications");
            return Collections.emptyList();
        }
    }
}
