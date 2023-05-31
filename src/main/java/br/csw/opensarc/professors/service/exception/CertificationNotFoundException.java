package br.csw.opensarc.professors.service.exception;

import jakarta.validation.constraints.NotBlank;

public class CertificationNotFoundException extends RuntimeException {
    public CertificationNotFoundException(String id) {
        super("Certification with id: " + id + " does not exists.");
    }
}
