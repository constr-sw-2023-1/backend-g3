package br.csw.opensarc.professors.repository.mapper;

import br.csw.opensarc.professors.repository.dto.IdentificationEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfessorIdentificationRowMapper implements RowMapper<IdentificationEntity> {
    @Override
    public IdentificationEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        String id = rs.getString("id");
        String type = rs.getString("type");
        String value = rs.getString("value");
        String professorId = rs.getString("professorId");
        return new IdentificationEntity(id, type, value, professorId);
    }
}
