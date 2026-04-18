package com.wikimusic.infrastructure.adapter;

import com.wikimusic.domain.model.ArtistInfo;
import com.wikimusic.domain.repository.ArtistInfosRepository;
import com.wikimusic.infrastructure.client.BandsApiClient;
import com.wikimusic.infrastructure.dto.BandApiResponse;
import com.wikimusic.infrastructure.mapper.ArtistInfosInfraMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ArtistInfosAdapter — Infrastructure output adapter implementing the domain's
 * {@link ArtistInfosRepository} port.
 *
 * Delegates to the {@link BandsApiClient} for HTTP retrieval and uses MapStruct
 * for mapping external DTOs to domain entities.
 */
@Component
@RequiredArgsConstructor
public class ArtistInfosAdapter implements ArtistInfosRepository {

    private final BandsApiClient bandsApiClient;
    private final ArtistInfosInfraMapper artistInfosInfraMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ArtistInfo> findAll() {
        List<BandApiResponse> responses = bandsApiClient.fetchAllBands();
        return artistInfosInfraMapper.toDomainList(responses);
    }
}
