package com.wikimusic.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * BandApiResponse — DTO representing the JSON response from the external Bands API.
 * Used exclusively by the {@code BandsApiClient} for JSON deserialization.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BandApiResponse {

    /** Unique identifier (UUID) of the band. */
    private String id;

    /** Name of the band or artist. */
    private String name;

    /** URL of the band's image. */
    private String image;

    /** Primary music genre. */
    private String genre;

    /** Biography text of the band. */
    private String biography;

    /** Total number of plays across platforms. */
    private long numPlays;

    /** List of album identifiers associated with this band. */
    private List<String> albums;
}
