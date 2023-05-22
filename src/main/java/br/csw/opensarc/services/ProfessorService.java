@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    public ProfessorEntity updateProfessor(String id, ProfessorEntity professorData) {
        ProfessorEntity professor = professorRepository.findById(id);

        if (professor == null) {
            optional.empty();
        }

        professorRepository.update(id, professorData);

        return Optional.of(o update())
    }
}
