package ar.edu.unvime.tp1favoritos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${app.dummyjson.base-url}")
    private String baseUrl;

    @Bean
    public RestClient dummyJsonRestClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
