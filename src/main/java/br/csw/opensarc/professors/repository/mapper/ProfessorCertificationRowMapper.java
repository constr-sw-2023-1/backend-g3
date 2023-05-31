package br.csw.opensarc.professors.repository.mapper;

import br.csw.opensarc.professors.repository.entity.ProfessorCertificationEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfessorCertificationRowMapper implements RowMapper<ProfessorCertificationEntity> {
    @Override
    public ProfessorCertificationEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        String id = rs.getString("id");
        String professor_id = rs.getString("professor_id");
        String certification_id = rs.getString("certification_id");
        String level = rs.getString("level");
        String name = rs.getString("name");
        String institution = rs.getString("institution");
        String year = rs.getString("year");
        String semester = rs.getString("semester");
        return new ProfessorCertificationEntity(id, professor_id, certification_id, level, name, institution, year, semester);
    }
}
