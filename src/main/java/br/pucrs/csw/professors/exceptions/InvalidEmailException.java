package br.pucrs.csw.professors.exceptions;

public class InvalidEmailException extends RuntimeException {
    public final String email;
    public final String regexPattern;

    public InvalidEmailException(String email, String regexPattern) {
        this.email = email;
        this.regexPattern = regexPattern;
    }
}
