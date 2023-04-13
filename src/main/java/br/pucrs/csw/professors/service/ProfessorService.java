package br.pucrs.csw.professors.service;

import br.pucrs.csw.professors.exceptions.UserNotFoundException;
import br.pucrs.csw.professors.keycloak.KeyCloakUserAPIClientService;
import br.pucrs.csw.professors.keycloak.pojo.KeyCloakUser;
import br.pucrs.csw.professors.pojo.PasswordUpdate;
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
                .map(KeyCloakUser::toProfessor)
                .toList();
    }

    public Professor getUserById(String id) {
        return this.getById(id)
                .map(KeyCloakUser::toProfessor)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public void delete(String id) {
        KeyCloakUser user = this.getById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        keyCloakUserAPIClientService.update(id, user.withEnabled(false));
    }

    public Professor updateUserById(String id, ProfessorInput newUserData) {
        KeyCloakUser user = this.getById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user = user.withFirstName(newUserData.firstName())
                .withLastName(newUserData.lastName());
        keyCloakUserAPIClientService.update(id, user);
        return user.toProfessor();
    }

    public Professor updatePassword(String id, PasswordUpdate passwordUpdate) {
        KeyCloakUser user = this.getById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user = user.withPassword(passwordUpdate.newPassword());
        keyCloakUserAPIClientService.update(id, user);
        return user.toProfessor();
    }

    private Optional<KeyCloakUser> getById(String id) {
        return keyCloakUserAPIClientService.getAll()
                .stream()
                .filter(it -> it.id().equals(id))
                .findFirst();
    }
}


