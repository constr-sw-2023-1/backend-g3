@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    public ProfessorEntity updateProfessor(String id, ProfessorEntity professorData) {
        ProfessorEntity professor = professorRepository.updateProfessor(id, professorData);

        if (professor == null) {
            return Optional.empty();
        }

        //return Optional.of(professorRepo.update(id, professorInput).toProfessor() não entendi oq quis dizer
        return Optional.of(professor);
    }
}
