public class ProfessorRowMapper implements RowMapper<IdentificationEntity> {
    @Override
    public IdentificationEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        String id = rs.getString("id");
        String registration = rs.getString("registration");
        String name = rs.getString("name");
        LocalDate bornDate = rs.getDate("bornDate").toLocalDate();
        LocalDate admissionDate = rs.getDate("admissionDate").toLocalDate();
        boolean active = rs.getBoolean("active");
        return new IdentificationEntity(id, type, value, professorId);
    }
}
