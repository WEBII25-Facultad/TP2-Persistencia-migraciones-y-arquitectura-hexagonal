package ar.edu.unvime.tp1favoritos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Favoritos API")
                        .description("Catálogo de productos (DummyJSON) + favoritos y listas persistidos en PostgreSQL")
                        .version("1.0"));
    }
}
