package br.csw.opensarc.professors.service.exception;

public class InsertError extends RuntimeException {
    public InsertError(String message) {
        super(message);
    }
}
