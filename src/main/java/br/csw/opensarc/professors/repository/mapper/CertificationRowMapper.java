package br.csw.opensarc.professors.repository.mapper;

import br.csw.opensarc.professors.repository.entity.CertificationEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CertificationRowMapper implements RowMapper<CertificationEntity> {
    @Override
    public CertificationEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        String id = rs.getString("id");
        String name = rs.getString("name");
        String level = rs.getString("level");
        String institution = rs.getString("institution");
        return new CertificationEntity(id, name, level, institution);
    }
}
