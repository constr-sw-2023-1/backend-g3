package br.pucrs.csw.professors.service;

import br.pucrs.csw.professors.keycloak.KeyCloakUserAPIClientService;
import br.pucrs.csw.professors.pojo.Professor;

public class ProfessorService {

    private KeyCloakUserAPIClientService keyCloakUserAPIClientService;

    public ProfessorService(KeyCloakUserAPIClientService keyCloakUserAPIClientService) {
        this.keyCloakUserAPIClientService = keyCloakUserAPIClientService;
    }

    public Professor create(Professor toCreate){
        return keyCloakUserAPIClientService.create(toCreate);
    }
}
