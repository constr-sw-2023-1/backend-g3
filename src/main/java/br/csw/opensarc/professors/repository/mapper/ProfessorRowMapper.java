package br.csw.opensarc.professors.repository.mapper;

import br.csw.opensarc.professors.repository.entity.ProfessorEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ProfessorRowMapper implements RowMapper<ProfessorEntity> {
    @Override
    public ProfessorEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        String id = rs.getString("id");
        String registration = rs.getString("registration");
        String name = rs.getString("name");
        LocalDate bornDate = rs.getDate("bornDate").toLocalDate();
        LocalDate admissionDate = rs.getDate("admissionDate").toLocalDate();
        boolean active = rs.getBoolean("active");
        return new ProfessorEntity(id, registration, name, bornDate, admissionDate, active);
    }
}
