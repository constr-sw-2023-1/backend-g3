package br.csw.opensarc.professors.controller.errors;

import br.csw.opensarc.professors.controller.dto.ErrorMessage;
import br.csw.opensarc.professors.service.exception.CertificationNotFoundException;
import br.csw.opensarc.professors.service.exception.InsertError;
import br.csw.opensarc.professors.service.exception.UpdateError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GenericErrors {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        if (ex.getFieldError() != null) {
            return new ResponseEntity<>(new ErrorMessage(ex.getFieldError().getDefaultMessage(), "USR-02"), HttpStatus.BAD_REQUEST);
        }
        return  new ResponseEntity<>(new ErrorMessage(ex.getMessage(), "USR-03"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UpdateError.class)
    public ResponseEntity<ErrorMessage> handelUpdateError(UpdateError exception) {
        return new ResponseEntity<>(
                new ErrorMessage(exception.getMessage(), "POST-02"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(CertificationNotFoundException.class)
    public ResponseEntity<ErrorMessage> handelCertificationNotFound(CertificationNotFoundException exception) {
        return new ResponseEntity<>(
                new ErrorMessage(exception.getMessage(), "USR-04"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(InsertError.class)
    public ResponseEntity<ErrorMessage> handelInsertError(InsertError exception) {
        return new ResponseEntity<>(
                new ErrorMessage(exception.getMessage(), "POST-01"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}
