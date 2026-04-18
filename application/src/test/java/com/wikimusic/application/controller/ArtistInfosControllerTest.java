package com.wikimusic.application.controller;

import com.wikimusic.application.dto.ArtistInfoResponse;
import com.wikimusic.application.dto.ArtistListResponse;
import com.wikimusic.application.handler.GlobalExceptionHandler;
import com.wikimusic.application.mapper.ArtistInfosRestMapper;
import com.wikimusic.domain.exception.ArtistNotFoundException;
import com.wikimusic.domain.model.ArtistInfo;
import com.wikimusic.domain.service.ArtistInfosService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * ArtistInfosControllerTest — WebMvcTest verifying the REST response structure
 * and exception handling of the {@link ArtistInfosController}.
 */
@WebMvcTest(ArtistInfosController.class)
@Import(GlobalExceptionHandler.class)
class ArtistInfosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArtistInfosService artistInfosService;

    @MockBean
    private ArtistInfosRestMapper artistInfosRestMapper;

    private static ArtistInfo buildArtistInfo(String id, String name, long numPlays) {
        return ArtistInfo.builder()
                .id(id)
                .name(name)
                .image("https://example.com/" + name + ".png")
                .genre("rock")
                .biography("Bio of " + name)
                .numPlays(numPlays)
                .albums(List.of("album-1", "album-2"))
                .build();
    }

    @Test
    @DisplayName("GET /v1/artists - should return paginated list of artists with name and numPlays")
    void findAll_shouldReturnPaginatedList() throws Exception {
        var artist1 = buildArtistInfo("id-1", "Alpha Band", 5000);
        var artist2 = buildArtistInfo("id-2", "Beta Band", 3000);

        when(artistInfosService.findAll(0, 10, true, null))
                .thenReturn(List.of(artist1, artist2));

        when(artistInfosRestMapper.toListResponseList(List.of(artist1, artist2)))
                .thenReturn(List.of(
                        new ArtistListResponse("id-1", "Alpha Band", 5000),
                        new ArtistListResponse("id-2", "Beta Band", 3000)
                ));

        mockMvc.perform(get("/v1/artists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Alpha Band")))
                .andExpect(jsonPath("$[0].numPlays", is(5000)))
                .andExpect(jsonPath("$[1].name", is("Beta Band")))
                .andExpect(jsonPath("$[1].numPlays", is(3000)));
    }

    @Test
    @DisplayName("GET /v1/artists - should accept pagination and sort parameters")
    void findAll_shouldAcceptParams() throws Exception {
        var artist = buildArtistInfo("id-1", "Zeta Band", 1000);

        when(artistInfosService.findAll(1, 5, false, null))
                .thenReturn(List.of(artist));

        when(artistInfosRestMapper.toListResponseList(List.of(artist)))
                .thenReturn(List.of(
                        new ArtistListResponse("id-1", "Zeta Band", 1000)
                ));

        mockMvc.perform(get("/v1/artists")
                        .param("page", "1")
                        .param("size", "5")
                        .param("sortAsc", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Zeta Band")));
    }

    @Test
    @DisplayName("GET /v1/artists?name=nickel - should return artists matching the name filter")
    void findAll_shouldFilterByName() throws Exception {
        var artist = buildArtistInfo("id-1", "Nickelback", 17605491);

        when(artistInfosService.findAll(0, 10, true, "nickel"))
                .thenReturn(List.of(artist));

        when(artistInfosRestMapper.toListResponseList(List.of(artist)))
                .thenReturn(List.of(
                        new ArtistListResponse("id-1", "Nickelback", 17605491)
                ));

        mockMvc.perform(get("/v1/artists")
                        .param("name", "nickel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Nickelback")))
                .andExpect(jsonPath("$[0].numPlays", is(17605491)));
    }

    @Test
    @DisplayName("GET /v1/artists?name=nonexistent - should return 404 when no artist matches")
    void findAll_shouldReturn404WhenNameNotFound() throws Exception {
        when(artistInfosService.findAll(0, 10, true, "nonexistent"))
                .thenThrow(new ArtistNotFoundException("name", "nonexistent"));

        mockMvc.perform(get("/v1/artists")
                        .param("name", "nonexistent"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Artist not found with name: nonexistent")));
    }

    @Test
    @DisplayName("GET /v1/artists/{id} - should return full artist details")
    void findById_shouldReturnArtistDetails() throws Exception {
        var artist = buildArtistInfo("bc710bcf-8815-42cf-bad2-3f1d12246aeb", "Nickelback", 17605491);

        when(artistInfosService.findById("bc710bcf-8815-42cf-bad2-3f1d12246aeb"))
                .thenReturn(artist);

        when(artistInfosRestMapper.toInfoResponse(artist))
                .thenReturn(new ArtistInfoResponse(
                        "bc710bcf-8815-42cf-bad2-3f1d12246aeb",
                        "Nickelback",
                        "https://example.com/Nickelback.png",
                        "rock",
                        "Bio of Nickelback",
                        17605491,
                        List.of("album-1", "album-2")
                ));

        mockMvc.perform(get("/v1/artists/{id}", "bc710bcf-8815-42cf-bad2-3f1d12246aeb"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("bc710bcf-8815-42cf-bad2-3f1d12246aeb")))
                .andExpect(jsonPath("$.name", is("Nickelback")))
                .andExpect(jsonPath("$.image", is("https://example.com/Nickelback.png")))
                .andExpect(jsonPath("$.genre", is("rock")))
                .andExpect(jsonPath("$.biography", is("Bio of Nickelback")))
                .andExpect(jsonPath("$.numPlays", is(17605491)))
                .andExpect(jsonPath("$.albums", hasSize(2)));
    }

    @Test
    @DisplayName("GET /v1/artists/{id} - should return 404 when artist not found")
    void findById_shouldReturn404WhenNotFound() throws Exception {
        when(artistInfosService.findById("unknown-id"))
                .thenThrow(new ArtistNotFoundException("unknown-id"));

        mockMvc.perform(get("/v1/artists/{id}", "unknown-id"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Artist not found with id: unknown-id")));
    }
}
