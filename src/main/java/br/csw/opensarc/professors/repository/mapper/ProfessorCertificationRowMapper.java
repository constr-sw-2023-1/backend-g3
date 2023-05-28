package br.csw.opensarc.professors.repository.mapper;

import br.csw.opensarc.professors.repository.entity.ProfessorCertificationEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfessorCertificationRowMapper implements RowMapper<ProfessorCertificationEntity> {
    @Override
    public ProfessorCertificationEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return null;
    }
}
