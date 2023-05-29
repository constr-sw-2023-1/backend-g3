package br.csw.opensarc.professors.controller.errors;

import br.csw.opensarc.professors.controller.dto.ErrorMessage;
import br.csw.opensarc.professors.service.exception.InsertError;
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

    @ExceptionHandler(InsertError.class)
    public ResponseEntity<ErrorMessage> handelInsertError(InsertError exception) {
        return new ResponseEntity<>(
                new ErrorMessage(exception.getMessage(), "POST-01"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}
