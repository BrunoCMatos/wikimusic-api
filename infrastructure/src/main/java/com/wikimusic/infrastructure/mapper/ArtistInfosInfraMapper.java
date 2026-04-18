package com.wikimusic.infrastructure.mapper;

import com.wikimusic.domain.model.ArtistInfo;
import com.wikimusic.infrastructure.dto.BandApiResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * ArtistInfosInfraMapper — MapStruct mapper that converts between the external API DTO
 * ({@link BandApiResponse}) and the domain model ({@link ArtistInfo}).
 *
 * Managed as a Spring component via the {@code componentModel = SPRING} setting.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ArtistInfosInfraMapper {

    /**
     * Maps a single {@link BandApiResponse} to an {@link ArtistInfo} domain entity.
     *
     * @param response the external API response DTO
     * @return the corresponding domain entity
     */
    ArtistInfo toDomain(BandApiResponse response);

    /**
     * Maps a list of {@link BandApiResponse} to a list of {@link ArtistInfo} domain entities.
     *
     * @param responses the list of external API response DTOs
     * @return the corresponding list of domain entities
     */
    List<ArtistInfo> toDomainList(List<BandApiResponse> responses);
}
