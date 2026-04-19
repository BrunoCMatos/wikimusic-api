package com.wikimusic.infrastructure.client;

import com.wikimusic.infrastructure.dto.BandApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static com.wikimusic.infrastructure.config.CacheConfiguration.BANDS_CACHE;

/**
 * BandsApiClient — Infrastructure component that retrieves band data
 * from the external third-party Bands API using Spring WebFlux's {@link WebClient}.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BandsApiClient {

    private final WebClient bandsApiWebClient;

    /**
     * Fetches all bands from the external API.
     *
     * @return a list of {@link BandApiResponse} objects deserialized from the API JSON response
     */
    @Cacheable(BANDS_CACHE)
    public List<BandApiResponse> fetchAllBands() {
        log.info("Starting fetchAllBands request to external Bands API");
        return bandsApiWebClient
                .get()
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<BandApiResponse>>() {})
                .block();
    }
}
