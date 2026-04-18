package com.wikimusic.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * ArtistListResponse — Output DTO for list endpoints returning a summary of artist information.
 * Contains only the artist name and number of plays.
 */
@Schema(description = "Summary artist data returned by list endpoints")
public record ArtistListResponse(

        @Schema(description = "Unique identifier of the artist", example = "bc710bcf-8815-42cf-bad2-3f1d12246aeb")
        String id,

        @Schema(description = "Name of the artist or band", example = "Nickelback")
        String name,

        @Schema(description = "Total number of plays across platforms", example = "17605491")
        long numPlays
) {}
