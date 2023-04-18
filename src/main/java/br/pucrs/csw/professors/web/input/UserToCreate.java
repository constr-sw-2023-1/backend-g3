package br.pucrs.csw.professors.web.input;

import br.pucrs.csw.professors.exceptions.InvalidEmailException;
import br.pucrs.csw.professors.exceptions.MissingProfessorEssentialPropertiesException;
import br.pucrs.csw.professors.pojo.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserToCreate(
        @NotBlank
        @Pattern(regexp = VALID_EMAIL_REGEX)
        String username,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String password) {

    private static final String VALID_EMAIL_REGEX = "([-!#-'*+/-9=?A-Z^-~]+(\\.[-!#-'*+/-9=?A-Z^-~]+)*|\"([]!#-" +
            "[^-~ \\t]|(\\t -~])+\")@([-!#-'*+/-9=?A-Z^-~]+(\\.[-!#-'*+/-9=?A-Z^-~]+)*|\\([\\t -Z^-~]*])";

    public User toUser() {
        return new User(null, username, firstName, lastName, password);
    }

    public void isValid() {
        isUserNameValid();
        if (firstName == null || firstName.isBlank()
                || lastName == null || lastName.isBlank()
                || password == null || password.isBlank()) {
            throw new MissingProfessorEssentialPropertiesException();
        }

    }

    private void isUserNameValid() {
        if (username == null || !username.matches(VALID_EMAIL_REGEX)) {
            throw new InvalidEmailException(username, VALID_EMAIL_REGEX);
        }
    }
}
