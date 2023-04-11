package br.pucrs.csw.professors.web.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public record ErrorDetails(@JsonProperty("error_code") String errorCode,
                           @JsonProperty("error_description") String errorDescription) {}
