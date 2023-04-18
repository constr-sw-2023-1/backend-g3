package br.pucrs.csw.professors.pojo;


public record User(String id, String username, String firstName, String lastName, String password) {

    public User withId(String id) {
        return new User(id, this.username, this.firstName, this.lastName, this.password);
    }
}
