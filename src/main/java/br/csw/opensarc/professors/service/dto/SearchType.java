package br.csw.opensarc.professors.service.dto;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public enum SearchType {
    EQUAL("", "="), NOT_EQUAL("{neq}", "<>"), GREATER_THEN("{gt}", ">"), GREATER_THEN_EQUAL("{gteq}", ">="),
    LESS_THEN("{lt}", "<"), LESS_THEN_EQUAL("{lteq}", "<="), LIKE("{like}", "LIKE");

    private String id;
    private String operator;

    SearchType(String id, String operator) {
        this.id = id;
        this.operator = operator;
    }

    public static Optional<SearchType> ofType(String id) {
        return Stream.of(values())
                .filter(it -> id.equals(it.id))
                .findFirst();
    }

    public static List<String> allIds() {
        return Arrays.stream(values()).filter(it -> !it.id.isBlank()).map(it -> it.id).toList();
    }

    public String getOperator() {
        return operator;
    }

    public String getId() {
        return id;
    }
}
