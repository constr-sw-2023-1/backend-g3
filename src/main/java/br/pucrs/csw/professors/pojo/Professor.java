package br.pucrs.csw.professors.pojo;

import br.pucrs.csw.professors.exceptions.InvalidEmailException;
import br.pucrs.csw.professors.exceptions.MissingProfessorEssentialPropertiesException;

public record Professor(String id, String username, String firstName, String lastName, String password) {

    private static final String VALID_EMAIL_REGEX = "([-!#-'*+/-9=?A-Z^-~]+(\\.[-!#-'*+/-9=?A-Z^-~]+)*|\"([]!#-" +
            "[^-~ \\t]|(\\t -~])+\")@([-!#-'*+/-9=?A-Z^-~]+(\\.[-!#-'*+/-9=?A-Z^-~]+)*|\\([\\t -Z^-~]*])";

    public Professor withId(String id) {
        return new Professor(id, this.username, this.firstName, this.lastName, this.password);
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
