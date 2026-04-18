package com.wikimusic.application.mapper;

import com.wikimusic.application.dto.ArtistInfoResponse;
import com.wikimusic.application.dto.ArtistListResponse;
import com.wikimusic.domain.model.ArtistInfo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * ArtistInfosRestMapper — MapStruct mapper that converts between the domain model
 * ({@link ArtistInfo}) and REST response DTOs.
 *
 * Managed as a Spring component via the {@code componentModel = SPRING} setting.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ArtistInfosRestMapper {

    /**
     * Maps a domain {@link ArtistInfo} to a summary {@link ArtistListResponse} DTO.
     *
     * @param artistInfo the domain entity
     * @return the list response DTO containing only name and numPlays
     */
    ArtistListResponse toListResponse(ArtistInfo artistInfo);

    /**
     * Maps a list of domain {@link ArtistInfo} entities to a list of {@link ArtistListResponse} DTOs.
     *
     * @param artistInfos the domain entities
     * @return the list of list response DTOs
     */
    List<ArtistListResponse> toListResponseList(List<ArtistInfo> artistInfos);

    /**
     * Maps a domain {@link ArtistInfo} to a full {@link ArtistInfoResponse} DTO.
     *
     * @param artistInfo the domain entity
     * @return the full detail response DTO
     */
    ArtistInfoResponse toInfoResponse(ArtistInfo artistInfo);
}
