@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    public Optional<ProfessorEntity> updateProfessor(String id, ProfessorEntity professorData) {
        return Optional.ofNullable(professorRepository.updateProfessor(id, professorData));
    }
}