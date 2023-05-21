public class ProfessorRowMapper implements RowMapper<IdentificationEntity> {
    @Override
    public IdentificationEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        String id = rs.getString("id");
        String type = rs.getString("type");
        String value = rs.getString("value");
        String professorId = rs.getString("professorId");
        return new IdentificationEntity(id, type, value, professorId);
    }
}
