package br.pucrs.csw.professors.config;

import br.pucrs.csw.professors.security.TokenValidatorInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class SecurityConfiguration implements WebMvcConfigurer {

    @Value("${keycloak.auth.validator.url}")
    private String keyCloakBaseUrl;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenValidatorInterceptor(restTemplate, keyCloakBaseUrl))
                .addPathPatterns("/professors/**")
                .excludePathPatterns("/professors/login/**");
    }
}
