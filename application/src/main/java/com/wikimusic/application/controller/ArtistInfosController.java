package com.wikimusic.application.controller;

import com.wikimusic.application.dto.ArtistInfoResponse;
import com.wikimusic.application.dto.ArtistListResponse;
import com.wikimusic.application.mapper.ArtistInfosRestMapper;
import com.wikimusic.domain.model.ArtistInfo;
import com.wikimusic.domain.service.ArtistInfosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ArtistInfosController — REST Input Adapter for querying artist information
 * retrieved from the external Bands API.
 *
 * Exposes read-only endpoints at {@code /v1/artists} for listing, searching
 * and fetching detailed artist data.
 */
@RestController
@RequestMapping("/v1/artists")
@RequiredArgsConstructor
@Tag(name = "Artist Infos", description = "Read-only endpoints for artist information from external Bands API")
public class ArtistInfosController {

    private final ArtistInfosService artistInfosService;
    private final ArtistInfosRestMapper artistInfosRestMapper;

    /**
     * Returns a paginated list of all artists, sorted by name.
     * Optionally filters by a name fragment (case-insensitive).
     *
     * @param page    zero-based page index (default 0)
     * @param size    number of elements per page (default 10)
     * @param sortAsc whether to sort by name ascending (default true)
     * @param name    optional name fragment to filter by
     * @return a list of {@link ArtistListResponse} containing name and numPlays
     */
    @GetMapping
    @Operation(summary = "List all artists (paginated)", description = "Returns a paginated list of artists sorted by name. Optionally filters by a name fragment. Defaults to page 0 with 10 items per page.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved artist list",
                    content = @Content(schema = @Schema(implementation = ArtistListResponse.class))),
            @ApiResponse(responseCode = "404", description = "No artists found matching the given name",
                    content = @Content)
    })
    public ResponseEntity<List<ArtistListResponse>> findAll(
            @Parameter(description = "Zero-based page index") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of elements per page") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by name ascending if true") @RequestParam(defaultValue = "true") boolean sortAsc,
            @Parameter(description = "Optional name fragment to filter by (case-insensitive)") @RequestParam(required = false) String name) {

        List<ArtistInfo> artists = artistInfosService.findAll(page, size, sortAsc, name);
        return ResponseEntity.ok(artistInfosRestMapper.toListResponseList(artists));
    }

    /**
     * Returns full details for a single artist by ID.
     *
     * @param id the artist UUID
     * @return the {@link ArtistInfoResponse} with all artist details
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get artist by ID", description = "Returns all information about a specific artist. Throws 404 if not found.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved artist details",
                    content = @Content(schema = @Schema(implementation = ArtistInfoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Artist not found with the given ID",
                    content = @Content)
    })
    public ResponseEntity<ArtistInfoResponse> findById(
            @Parameter(description = "Artist UUID", required = true) @PathVariable String id) {

        ArtistInfo artist = artistInfosService.findById(id);
        return ResponseEntity.ok(artistInfosRestMapper.toInfoResponse(artist));
    }
}
