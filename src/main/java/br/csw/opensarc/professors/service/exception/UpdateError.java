package br.csw.opensarc.professors.service.exception;

public class UpdateError extends RuntimeException {
    public UpdateError(String message) {
        super(message);
    }
}
