package br.pucrs.csw.professors.service;

import br.pucrs.csw.professors.exceptions.UserNotFoundException;
import br.pucrs.csw.professors.keycloak.KeyCloakUserAPIClientService;
import br.pucrs.csw.professors.keycloak.pojo.KeyCloakUser;
import br.pucrs.csw.professors.pojo.PasswordUpdate;
import br.pucrs.csw.professors.pojo.User;
import br.pucrs.csw.professors.web.input.UserUpdate;

import java.util.List;
import java.util.Optional;

public class UserService {

    private KeyCloakUserAPIClientService keyCloakUserAPIClientService;

    public UserService(KeyCloakUserAPIClientService keyCloakUserAPIClientService) {
        this.keyCloakUserAPIClientService = keyCloakUserAPIClientService;
    }

    public User create(User toCreate) {
        return keyCloakUserAPIClientService.create(toCreate);
    }

    public List<User> getAll() {
        List<KeyCloakUser> users = keyCloakUserAPIClientService.getAll();
        return users.stream()
                .map(KeyCloakUser::toUser)
                .toList();
    }

    public User getUserById(String id) {
        return this.getById(id)
                .map(KeyCloakUser::toUser)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public void delete(String id) {
        KeyCloakUser user = this.getById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        keyCloakUserAPIClientService.update(id, user.withEnabled(false));
    }

    public User updateUserById(String id, UserUpdate newUserData) {
        KeyCloakUser user = this.getById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user = user.withFirstName(newUserData.firstName())
                .withLastName(newUserData.lastName());
        keyCloakUserAPIClientService.update(id, user);
        return user.toUser();
    }

    public User updatePassword(String id, PasswordUpdate passwordUpdate) {
        KeyCloakUser user = this.getById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user = user.withPassword(passwordUpdate.newPassword());
        keyCloakUserAPIClientService.update(id, user);
        return user.toUser();
    }

    private Optional<KeyCloakUser> getById(String id) {
        return keyCloakUserAPIClientService.getAll()
                .stream()
                .filter(it -> it.id().equals(id))
                .findFirst();
    }
}


