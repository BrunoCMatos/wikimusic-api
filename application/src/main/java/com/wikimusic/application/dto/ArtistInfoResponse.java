package com.wikimusic.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * ArtistInfoResponse — Output DTO for the detail endpoint returning all information about an artist.
 * Maps to the full artist data retrieved from the external Bands API.
 */
@Schema(description = "Full artist data returned by the detail endpoint")
public record ArtistInfoResponse(

        @Schema(description = "Unique artist identifier (UUID)", example = "bc710bcf-8815-42cf-bad2-3f1d12246aeb")
        String id,

        @Schema(description = "Name of the artist or band", example = "Nickelback")
        String name,

        @Schema(description = "URL of the artist's image")
        String image,

        @Schema(description = "Primary music genre", example = "rock")
        String genre,

        @Schema(description = "Biography text of the artist")
        String biography,

        @Schema(description = "Total number of plays across platforms", example = "17605491")
        long numPlays,

        @Schema(description = "List of album identifiers associated with the artist")
        List<String> albums
) {}
