package br.pucrs.csw.professors.pojo;


public record Professor(String id, String username, String firstName, String lastName, String password) {

    public Professor withId(String id) {
        return new Professor(id, this.username, this.firstName, this.lastName, this.password);
    }
}
