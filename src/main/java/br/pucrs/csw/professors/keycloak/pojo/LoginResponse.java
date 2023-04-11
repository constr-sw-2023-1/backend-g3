package br.pucrs.csw.professors.keycloak.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LoginResponse(@JsonProperty("access_token") String accessToken,
                            @JsonProperty("token_type") String tokenType,
                            @JsonProperty("refresh_token") String refreshToken,
                            @JsonProperty("refresh_expires_in") Integer refreshExpiresIn,
                            @JsonProperty("expires_in") Integer expiresIn) {
}
