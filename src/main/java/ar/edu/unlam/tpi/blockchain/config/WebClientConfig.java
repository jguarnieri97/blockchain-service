package ar.edu.unlam.tpi.blockchain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient buildWebClient() {
        return WebClient.builder().build();
    }
}
