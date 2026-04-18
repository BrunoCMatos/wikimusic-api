package com.wikimusic.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for {@code ArtistInfosController} using WireMock to simulate
 * the external Bands API. Boots the full Spring context.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ArtistInfosControllerIntegrationTest {

    static WireMockServer wireMockServer;

    @Autowired
    private MockMvc mockMvc;

    private static final String BANDS_API_RESPONSE = """
            [
              {
                "id": "id-1",
                "name": "Arctic Monkeys",
                "image": "https://example.com/arctic.png",
                "genre": "indie rock",
                "biography": "Bio of Arctic Monkeys",
                "numPlays": 12000000,
                "albums": ["album-a1", "album-a2"]
              },
              {
                "id": "id-2",
                "name": "Coldplay",
                "image": "https://example.com/coldplay.png",
                "genre": "alternative rock",
                "biography": "Bio of Coldplay",
                "numPlays": 25000000,
                "albums": ["album-c1"]
              },
              {
                "id": "id-3",
                "name": "Nickelback",
                "image": "https://example.com/nickelback.png",
                "genre": "rock",
                "biography": "Bio of Nickelback",
                "numPlays": 17605491,
                "albums": ["album-n1", "album-n2", "album-n3"]
              }
            ]
            """;

    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMockServer.start();
    }

    @AfterAll
    static void stopWireMock() {
        wireMockServer.stop();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("bands-api.base-url", () -> wireMockServer.baseUrl() + "/api/bands");
    }

    @BeforeEach
    void resetWireMock() {
        wireMockServer.resetAll();
    }

    private void stubBandsApiSuccess() {
        wireMockServer.stubFor(WireMock.get(urlEqualTo("/api/bands"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(BANDS_API_RESPONSE)));
    }

    private void stubBandsApiEmpty() {
        wireMockServer.stubFor(WireMock.get(urlEqualTo("/api/bands"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("[]")));
    }

    private void stubBandsApiServerError() {
        wireMockServer.stubFor(WireMock.get(urlEqualTo("/api/bands"))
                .willReturn(aResponse().withStatus(500)));
    }

    // ── GET /v1/artists (findAll) ──────────────────────────────────────

    @Nested
    @DisplayName("GET /v1/artists")
    class FindAllTests {

        @Test
        @DisplayName("should return paginated list with default parameters")
        void shouldReturnPaginatedList() throws Exception {
            stubBandsApiSuccess();

            mockMvc.perform(get("/v1/artists"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)))
                    // sorted ascending by name: Arctic Monkeys, Coldplay, Nickelback
                    .andExpect(jsonPath("$[0].name", is("Arctic Monkeys")))
                    .andExpect(jsonPath("$[1].name", is("Coldplay")))
                    .andExpect(jsonPath("$[2].name", is("Nickelback")))
                    .andExpect(jsonPath("$[0].id", is("id-1")))
                    .andExpect(jsonPath("$[0].numPlays", is(12000000)));
        }

        @Test
        @DisplayName("should respect pagination parameters")
        void shouldRespectPagination() throws Exception {
            stubBandsApiSuccess();

            mockMvc.perform(get("/v1/artists")
                            .param("page", "0")
                            .param("size", "2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].name", is("Arctic Monkeys")))
                    .andExpect(jsonPath("$[1].name", is("Coldplay")));
        }

        @Test
        @DisplayName("should return second page")
        void shouldReturnSecondPage() throws Exception {
            stubBandsApiSuccess();

            mockMvc.perform(get("/v1/artists")
                            .param("page", "1")
                            .param("size", "2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].name", is("Nickelback")));
        }

        @Test
        @DisplayName("should return empty list when page is beyond available data")
        void shouldReturnEmptyWhenPageBeyondData() throws Exception {
            stubBandsApiSuccess();

            mockMvc.perform(get("/v1/artists")
                            .param("page", "10")
                            .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        @DisplayName("should sort descending when sortAsc is false")
        void shouldSortDescending() throws Exception {
            stubBandsApiSuccess();

            mockMvc.perform(get("/v1/artists")
                            .param("sortAsc", "false"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name", is("Nickelback")))
                    .andExpect(jsonPath("$[1].name", is("Coldplay")))
                    .andExpect(jsonPath("$[2].name", is("Arctic Monkeys")));
        }

        @Test
        @DisplayName("should filter by name fragment (case-insensitive)")
        void shouldFilterByName() throws Exception {
            stubBandsApiSuccess();

            mockMvc.perform(get("/v1/artists")
                            .param("name", "cold"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].name", is("Coldplay")));
        }

        @Test
        @DisplayName("should return 404 when name filter matches no artist")
        void shouldReturn404WhenNameNotFound() throws Exception {
            stubBandsApiSuccess();

            mockMvc.perform(get("/v1/artists")
                            .param("name", "nonexistent"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status", is(404)))
                    .andExpect(jsonPath("$.message", is("Artist not found with name: nonexistent")));
        }

        @Test
        @DisplayName("should return empty list when external API returns empty array")
        void shouldReturnEmptyWhenApiReturnsEmpty() throws Exception {
            stubBandsApiEmpty();

            mockMvc.perform(get("/v1/artists"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        @DisplayName("should return 500 when external API is down")
        void shouldReturn500WhenApiIsDown() throws Exception {
            stubBandsApiServerError();

            mockMvc.perform(get("/v1/artists"))
                    .andExpect(status().isInternalServerError());
        }
    }

    // ── GET /v1/artists/{id} (findById) ────────────────────────────────

    @Nested
    @DisplayName("GET /v1/artists/{id}")
    class FindByIdTests {

        @Test
        @DisplayName("should return full artist details for a valid ID")
        void shouldReturnArtistDetails() throws Exception {
            stubBandsApiSuccess();

            mockMvc.perform(get("/v1/artists/{id}", "id-2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is("id-2")))
                    .andExpect(jsonPath("$.name", is("Coldplay")))
                    .andExpect(jsonPath("$.image", is("https://example.com/coldplay.png")))
                    .andExpect(jsonPath("$.genre", is("alternative rock")))
                    .andExpect(jsonPath("$.biography", is("Bio of Coldplay")))
                    .andExpect(jsonPath("$.numPlays", is(25000000)))
                    .andExpect(jsonPath("$.albums", hasSize(1)))
                    .andExpect(jsonPath("$.albums[0]", is("album-c1")));
        }

        @Test
        @DisplayName("should return 404 when artist ID does not exist")
        void shouldReturn404WhenIdNotFound() throws Exception {
            stubBandsApiSuccess();

            mockMvc.perform(get("/v1/artists/{id}", "unknown-id"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status", is(404)))
                    .andExpect(jsonPath("$.message", is("Artist not found with id: unknown-id")));
        }

        @Test
        @DisplayName("should return 500 when external API is down")
        void shouldReturn500WhenApiIsDown() throws Exception {
            stubBandsApiServerError();

            mockMvc.perform(get("/v1/artists/{id}", "id-1"))
                    .andExpect(status().isInternalServerError());
        }
    }
}

