package br.pucrs.csw.professors.exceptions;

public class UserNameAlreadyExists extends RuntimeException {

    public final String username;
    public UserNameAlreadyExists(String username) {
        this.username = username;
    }
}
