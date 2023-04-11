package br.pucrs.csw.professors.exceptions;

public class UserNotFoundException extends RuntimeException {

    public final String id;

    public UserNotFoundException(String id) {
        this.id = id;
    }
}
