@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {                
        return new Docket(DocumentationType.SWAGGER_2)          
          .select()
          .apis(RequestHandlerSelectors.basePackage("src.main.web.ProfessorController"))
          //caminho para ProfessorController.java
          .paths(PathSelectors.any())
          .build()
          .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Swagger Backend Grupo 3")
            .description("Documentação da API de acesso aos endpoints do Backend Grupo 3")
            .version("1.0")
            .build();
    }
}
