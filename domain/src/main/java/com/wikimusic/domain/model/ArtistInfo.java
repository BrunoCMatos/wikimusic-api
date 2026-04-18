package com.wikimusic.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * ArtistInfo — Domain entity representing a musical artist retrieved from the external Bands API.
 * Contains all information about an artist including metadata, biography and album references.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistInfo {

    /** Unique identifier (UUID) of the artist. */
    private String id;

    /** Name of the artist or band. */
    private String name;

    /** URL of the artist's image. */
    private String image;

    /** Primary music genre. */
    private String genre;

    /** Biography text of the artist. */
    private String biography;

    /** Total number of plays across platforms. */
    private long numPlays;

    /** List of album identifiers associated with this artist. */
    private List<String> albums;
}
