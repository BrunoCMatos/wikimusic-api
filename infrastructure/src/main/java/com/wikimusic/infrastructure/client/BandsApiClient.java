package com.wikimusic.infrastructure.client;

import com.wikimusic.infrastructure.dto.BandApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

/**
 * BandsApiClient — Infrastructure component that retrieves band data
 * from the external third-party Bands API using Spring WebFlux's {@link WebClient}.
 */
@Component
@RequiredArgsConstructor
public class BandsApiClient {

    private final WebClient bandsApiWebClient;

    /**
     * Fetches all bands from the external API.
     *
     * @return a list of {@link BandApiResponse} objects deserialized from the API JSON response
     */
    public List<BandApiResponse> fetchAllBands() {
        return bandsApiWebClient
                .get()
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<BandApiResponse>>() {})
                .block();
    }
}
