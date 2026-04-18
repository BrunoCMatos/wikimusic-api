package com.wikimusic.domain.repository;

import com.wikimusic.domain.model.ArtistInfo;

import java.util.List;

/**
 * ArtistInfosRepository — Output port for retrieving artist information from an external data source.
 * Implemented by the infrastructure layer adapter that calls the third-party Bands API.
 */
public interface ArtistInfosRepository {

    /**
     * Retrieves all artists from the external data source.
     *
     * @return a list of all available {@link ArtistInfo} entities
     */
    List<ArtistInfo> findAll();
}
