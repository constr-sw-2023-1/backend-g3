package br.pucrs.csw.professors.security;

import br.pucrs.csw.professors.exceptions.MissingTokenAuthException;
import br.pucrs.csw.professors.exceptions.TokenAuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

public class TokenValidatorInterceptor implements HandlerInterceptor {

    private static final String HEADER_TOKEN = "Authorization";
    private final RestTemplate restTemplate;
    private final String keyCloakAuthUrl;

    public TokenValidatorInterceptor(RestTemplate restTemplate, String keyCloakAuthUrl) {
        this.restTemplate = restTemplate;
        this.keyCloakAuthUrl = keyCloakAuthUrl;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader(HEADER_TOKEN);
        if (header == null || header.isBlank()) {
            throw new MissingTokenAuthException();
        }
        String token = getToken(header);
        isTokenValid(token);
        RequestContext.setAccessToken(token);
        return true;
    }

    private String getToken(String header) {
        if (header.toLowerCase().startsWith("bearer"))
            return header.substring(7);
        throw new TokenAuthException();
    }

    private boolean isTokenValid(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        HttpEntity entity = new HttpEntity(httpHeaders);

        try {
            restTemplate.exchange(keyCloakAuthUrl, HttpMethod.GET, entity, String.class);
            return true;
        } catch (HttpClientErrorException ex) {
            throw new TokenAuthException();
        }
    }
}
