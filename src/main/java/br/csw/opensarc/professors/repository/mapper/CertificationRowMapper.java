package br.csw.opensarc.professors.repository.mapper;

import br.csw.opensarc.professors.repository.entity.CertificationEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CertificationRowMapper implements RowMapper<CertificationEntity> {
    @Override
    public CertificationEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        String id = rs.getString("id");
        int year = rs.getInt("year");
        String level = rs.getString("level");
        String description = rs.getString("description");
        String professorId = rs.getString("professor_id");

        return new CertificationEntity(id, professorId, year, level, description);
    }
}
