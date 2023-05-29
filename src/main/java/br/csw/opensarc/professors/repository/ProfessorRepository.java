package br.csw.opensarc.professors.repository;

import br.csw.opensarc.professors.repository.entity.ProfessorEntity;
import br.csw.opensarc.professors.repository.mapper.ProfessorRowMapper;
import br.csw.opensarc.professors.service.dto.SearchFilters;
import br.csw.opensarc.professors.service.exception.InsertError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.*;

public class ProfessorRepository {
    private static final Map<String, String> typesOfFields = Map.of(
            "id", "text",
            "registration", "text",
            "name", "text",
            "bornDate", "date",
            "admissionDate", "date",
            "active", "boolean"
    );
    private static final String TABLE_NAME = "professors.professor";
    private static final Logger log = LoggerFactory.getLogger(IdentificationsRepository.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ProfessorRowMapper professorRowMapper;

    public ProfessorRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        professorRowMapper = new ProfessorRowMapper();
    }

    public Optional<ProfessorEntity> getById(String professorId) {
        try {
            String sql = """
                        select id, registration, name, born_date, admission_date, active
                        from %s
                        where id = :id
                    """;
            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                    .addValue("id", UUID.fromString(professorId));

            return Optional.ofNullable(jdbcTemplate.queryForObject(String.format(sql, TABLE_NAME), mapSqlParameterSource, professorRowMapper));
        } catch (DataAccessException ex) {
            return Optional.empty();
        }
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
                    .addValue("id", UUID.fromString(professorId))
                    .addValue("registration", professor.registration())
                    .addValue("name", professor.name())
                    .addValue("bornDate", professor.bornDate())
                    .addValue("admissionDate", professor.admissionDate())
                    .addValue("active", professor.active());

            ProfessorEntity updatedProfessor = jdbcTemplate.queryForObject(sql,
                    mapSqlParameterSource, professorRowMapper);

            return Optional.ofNullable(updatedProfessor);
        } catch (DataAccessException ex) {
            log.error("Error trying to update professor: " + professorId, ex);
            return Optional.empty();
        }
    }

    public List<ProfessorEntity> getAll(List<SearchFilters> searchFilters) {
        try {
            StringBuilder sql = new StringBuilder("""
                        select id, registration, name, born_date, admission_date, active
                        from %s
                        WHERE 1 = 1
                    """);
            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
            for (SearchFilters it : searchFilters) {
                sql.append(String.format(" AND %s %s %s", toSnake(it.field()),
                        it.type().getOperator(),
                        "(:" + it.field() + ")::" + typesOfFields.getOrDefault(it.field(), "text")
                ));
                mapSqlParameterSource.addValue(it.field(), it.value());
            }

            return jdbcTemplate.query(String.format(sql.toString(), TABLE_NAME), mapSqlParameterSource, professorRowMapper);
        } catch (DataAccessException ex) {
            log.error("Error trying to get all users", ex);
            return new ArrayList<>();
        }
    }

    private String toSnake(String field) {
        return field.replaceAll("([A-Z])", "_$1").toLowerCase();
    }

    public ProfessorEntity create(ProfessorEntity professorEntity) {
        String sql = """
                    insert into %s (registration, name, born_date, admission_date, active)
                    values (:registration, :name, :born_date, :admission_date, :active)
                    returning id, registration, name, born_date, admission_date, active;
                """;
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("registration", professorEntity.registration())
                .addValue("name", professorEntity.name())
                .addValue("born_date", professorEntity.bornDate())
                .addValue("admission_date", professorEntity.admissionDate())
                .addValue("active", professorEntity.active());
        try {
            return jdbcTemplate.queryForObject(String.format(sql, TABLE_NAME), parameterSource, professorRowMapper);
        } catch (DataAccessException ex) {
            log.error("Error trying to insert into professor: ", ex);
            throw new InsertError(ex.getMessage());
        }
    }

    public void delete(String id) {
        String sql = """
                    UPDATE %s
                    SET active = false
                    WHERE id = :id
                """;
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource("id", UUID.fromString(id));
        jdbcTemplate.update(String.format(sql, TABLE_NAME), mapSqlParameterSource);
    }
}