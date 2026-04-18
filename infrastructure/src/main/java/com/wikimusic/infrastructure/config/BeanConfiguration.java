package com.wikimusic.infrastructure.config;

import com.wikimusic.domain.repository.ArtistInfosRepository;
import com.wikimusic.domain.service.ArtistInfosService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * BeanConfiguration — Wires domain-layer services as Spring beans.
 *
 * Domain and application modules contain no Spring annotations.
 * This configuration class bridges them into the Spring context,
 * preserving the hexagonal architecture's dependency inversion.
 */
@Configuration
public class BeanConfiguration {

    /**
     * Creates the {@link ArtistInfosService} domain service bean,
     * injecting the output port implementation.
     *
     * @param artistInfosRepository the infrastructure adapter implementing the output port
     * @return the wired domain service
     */
    @Bean
    public ArtistInfosService artistInfosService(ArtistInfosRepository artistInfosRepository) {
        return new ArtistInfosService(artistInfosRepository);
    }
}
