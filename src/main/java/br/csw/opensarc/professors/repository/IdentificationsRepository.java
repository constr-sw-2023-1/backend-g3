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
}
