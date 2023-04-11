package br.pucrs.csw.professors.web.error;

import br.pucrs.csw.professors.exceptions.*;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(TokenAuthException.class)
    public ResponseEntity<ErrorDetails> errorValidatingToken(TokenAuthException ex) {
        ErrorDetails errorDetails = new ErrorDetails("OA-000", "Error when validating token");
        return new ResponseEntity<>(errorDetails, HttpStatusCode.valueOf(401));
    }

    @ExceptionHandler(MissingTokenAuthException.class)
    public ResponseEntity<ErrorDetails> missingAuthHeader(MissingTokenAuthException ex) {
        ErrorDetails errorDetails = new ErrorDetails("OA-000", "Missing Authorization header");
        return new ResponseEntity<>(errorDetails, HttpStatusCode.valueOf(401));
    }

    @ExceptionHandler(UnauthorizedOperation.class)
    public ResponseEntity<ErrorDetails> unauthorizedOperation(UnauthorizedOperation ex) {
        String message = "User does not have permission to do this operation";
        ErrorDetails errorDetails = new ErrorDetails("OA-001", message);
        return new ResponseEntity<>(errorDetails, HttpStatusCode.valueOf(403));
    }

    @ExceptionHandler(InvalidUserOrPasswordLoginException.class)
    public ResponseEntity<ErrorDetails> invalidUserNameOrPasswordException(InvalidUserOrPasswordLoginException ex) {
        String message = "Invalid username or password";
        ErrorDetails errorDetails = new ErrorDetails("OA-002", message);
        return new ResponseEntity<>(errorDetails, HttpStatusCode.valueOf(401));
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ErrorDetails> invalidEmailOnCreation(InvalidEmailException ex) {
        String message = String.format("Email: %s should follow pattern %s", ex.email, ex.regexPattern);
        ErrorDetails errorDetails = new ErrorDetails("PRF-000", message);
        return new ResponseEntity<>(errorDetails, HttpStatusCode.valueOf(400));
    }

    @ExceptionHandler(MissingProfessorEssentialPropertiesException.class)
    public ResponseEntity<ErrorDetails> missingProfessorProperties(MissingProfessorEssentialPropertiesException ex) {
        ErrorDetails errorDetails = new ErrorDetails("PRF-001", "Some properties are missing");
        return new ResponseEntity<>(errorDetails, HttpStatusCode.valueOf(400));
    }

    @ExceptionHandler(UserNameAlreadyExists.class)
    public ResponseEntity<ErrorDetails> userNameAlreadyExists(UserNameAlreadyExists ex) {
        String message = String.format("UserName: %s already exists", ex.username);
        ErrorDetails errorDetails = new ErrorDetails("PRF-000", message);
        return new ResponseEntity<>(errorDetails, HttpStatusCode.valueOf(409));
    }
}
