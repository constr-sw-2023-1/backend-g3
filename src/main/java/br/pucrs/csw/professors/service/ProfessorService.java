package br.pucrs.csw.professors.service;

import br.pucrs.csw.professors.exceptions.UserNotFoundException;
import br.pucrs.csw.professors.keycloak.KeyCloakUserAPIClientService;
import br.pucrs.csw.professors.keycloak.pojo.KeyCloakUser;
import br.pucrs.csw.professors.pojo.Professor;
import br.pucrs.csw.professors.web.input.ProfessorInput;

import java.util.List;
import java.util.Optional;

public class ProfessorService {

    private KeyCloakUserAPIClientService keyCloakUserAPIClientService;

    public ProfessorService(KeyCloakUserAPIClientService keyCloakUserAPIClientService) {
        this.keyCloakUserAPIClientService = keyCloakUserAPIClientService;
    }

    public Professor create(Professor toCreate) {
        return keyCloakUserAPIClientService.create(toCreate);
    }

    public List<Professor> getAll() {
        List<KeyCloakUser> users = keyCloakUserAPIClientService.getAll();
        return users.stream()
                .filter(KeyCloakUser::enabled)
                .map(KeyCloakUser::toProfessor)
                .toList();
    }

    public void delete(String id) {
        KeyCloakUser user = keyCloakUserAPIClientService.getUser(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        keyCloakUserAPIClientService.update(id, user.withEnabled(false));
    }

    public Optional<Professor> update(String id, ProfessorInput input) {
        return Optional.empty();
    }
}
