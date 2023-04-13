package br.pucrs.csw.professors.config;

import br.pucrs.csw.professors.security.TokenValidatorInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class SecurityConfiguration implements WebMvcConfigurer {

    @Value("#{'${required.path.token.interceptor}'.split(',\\s*')}")
    private String[] interceptorPaths;
    @Value("${keycloak.auth.validator.url}")
    private String keyCloakBaseUrl;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenValidatorInterceptor(restTemplate, keyCloakBaseUrl))
                .addPathPatterns(interceptorPaths);
    }
}
