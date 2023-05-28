import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record ProfessorEntity (String registration, String name, LocalDate bornDate, LocalDate admissionDate, boolean active, List<IdentificationEntity> identification) {
    public ProfessorEntity(String registration, String name, LocalDate bornDate, LocalDate admissionDate, boolean active) {
        this(registration, name, bornDate, admissionDate, active, new ArrayList<>());
    } 
    
    public ProfessorEntity withIdentification(IdentificationEntity identification) {
        List<IdentificationEntity> identifications = new ArrayList<>(this.identification);
        identifications.add(identification);
        return new ProfessorEntity(id, registration, name, bornDate, admissionDate, active, identifications);
    }

    //criar método toProfessor que vai limpar o id da lista de identifications e gerar um professor
}