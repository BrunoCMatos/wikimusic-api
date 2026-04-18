package com.wikimusic.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClientConfiguration — Configures the {@link WebClient} bean used by the
 * {@code BandsApiClient} to call the external Bands API.
 */
@Configuration
public class WebClientConfiguration {

    /**
     * Creates a {@link WebClient} configured with the Bands API base URL.
     *
     * @param bandsApiBaseUrl the base URL of the external Bands API
     * @return a configured {@link WebClient} instance
     */
    @Bean
    public WebClient bandsApiWebClient(@Value("${bands-api.base-url}") String bandsApiBaseUrl) {
        return WebClient.builder()
                .baseUrl(bandsApiBaseUrl)
                .build();
    }
}
