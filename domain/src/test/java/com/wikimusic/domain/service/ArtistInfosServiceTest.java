package com.wikimusic.domain.service;

import com.wikimusic.domain.exception.ArtistNotFoundException;
import com.wikimusic.domain.model.ArtistInfo;
import com.wikimusic.domain.repository.ArtistInfosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * ArtistInfosServiceTest — Unit tests for the {@link ArtistInfosService} domain service.
 * Verifies filtering, sorting, pagination logic and exception scenarios using Mockito.
 */
@ExtendWith(MockitoExtension.class)
class ArtistInfosServiceTest {

    @Mock
    private ArtistInfosRepository artistInfosRepository;

    @InjectMocks
    private ArtistInfosService artistInfosService;

    private List<ArtistInfo> sampleArtists;

    @BeforeEach
    void setUp() {
        sampleArtists = List.of(
                ArtistInfo.builder().id("id-1").name("Nickelback").numPlays(17605491).build(),
                ArtistInfo.builder().id("id-2").name("Arctic Monkeys").numPlays(12000000).build(),
                ArtistInfo.builder().id("id-3").name("Imagine Dragons").numPlays(20000000).build(),
                ArtistInfo.builder().id("id-4").name("Coldplay").numPlays(25000000).build(),
                ArtistInfo.builder().id("id-5").name("Arcade Fire").numPlays(8000000).build()
        );
    }

    @Nested
    @DisplayName("findAll")
    class FindAllTests {

        @Test
        @DisplayName("should return artists sorted ascending by name")
        void shouldSortAscending() {
            when(artistInfosRepository.findAll()).thenReturn(sampleArtists);

            List<ArtistInfo> result = artistInfosService.findAll(0, 10, true, null);

            assertEquals(5, result.size());
            assertEquals("Arcade Fire", result.get(0).getName());
            assertEquals("Arctic Monkeys", result.get(1).getName());
            assertEquals("Coldplay", result.get(2).getName());
            assertEquals("Imagine Dragons", result.get(3).getName());
            assertEquals("Nickelback", result.get(4).getName());
        }

        @Test
        @DisplayName("should return artists sorted descending by name")
        void shouldSortDescending() {
            when(artistInfosRepository.findAll()).thenReturn(sampleArtists);

            List<ArtistInfo> result = artistInfosService.findAll(0, 10, false, null);

            assertEquals(5, result.size());
            assertEquals("Nickelback", result.get(0).getName());
            assertEquals("Imagine Dragons", result.get(1).getName());
            assertEquals("Coldplay", result.get(2).getName());
            assertEquals("Arctic Monkeys", result.get(3).getName());
            assertEquals("Arcade Fire", result.get(4).getName());
        }

        @Test
        @DisplayName("should paginate results with default page size")
        void shouldPaginateResults() {
            when(artistInfosRepository.findAll()).thenReturn(sampleArtists);

            List<ArtistInfo> page0 = artistInfosService.findAll(0, 2, true, null);
            List<ArtistInfo> page1 = artistInfosService.findAll(1, 2, true, null);
            List<ArtistInfo> page2 = artistInfosService.findAll(2, 2, true, null);

            assertEquals(2, page0.size());
            assertEquals("Arcade Fire", page0.get(0).getName());
            assertEquals("Arctic Monkeys", page0.get(1).getName());

            assertEquals(2, page1.size());
            assertEquals("Coldplay", page1.get(0).getName());
            assertEquals("Imagine Dragons", page1.get(1).getName());

            assertEquals(1, page2.size());
            assertEquals("Nickelback", page2.get(0).getName());
        }

        @Test
        @DisplayName("should return empty list when page is beyond available data")
        void shouldReturnEmptyForOutOfBoundsPage() {
            when(artistInfosRepository.findAll()).thenReturn(sampleArtists);

            List<ArtistInfo> result = artistInfosService.findAll(10, 10, true, null);

            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("should filter artists by name (case-insensitive)")
        void shouldFilterByNameCaseInsensitive() {
            when(artistInfosRepository.findAll()).thenReturn(sampleArtists);

            List<ArtistInfo> result = artistInfosService.findAll(0, 10, true, "arc");

            assertEquals(2, result.size());
            assertEquals("Arcade Fire", result.get(0).getName());
            assertEquals("Arctic Monkeys", result.get(1).getName());
        }

        @Test
        @DisplayName("should sort filtered results descending")
        void shouldSortFilteredDescending() {
            when(artistInfosRepository.findAll()).thenReturn(sampleArtists);

            List<ArtistInfo> result = artistInfosService.findAll(0, 10, false, "arc");

            assertEquals(2, result.size());
            assertEquals("Arctic Monkeys", result.get(0).getName());
            assertEquals("Arcade Fire", result.get(1).getName());
        }

        @Test
        @DisplayName("should paginate filtered results")
        void shouldPaginateFilteredResults() {
            when(artistInfosRepository.findAll()).thenReturn(sampleArtists);

            List<ArtistInfo> result = artistInfosService.findAll(0, 1, true, "arc");

            assertEquals(1, result.size());
            assertEquals("Arcade Fire", result.get(0).getName());
        }

        @Test
        @DisplayName("should throw ArtistNotFoundException when no artist matches the name")
        void shouldThrowExceptionWhenNameNotFound() {
            when(artistInfosRepository.findAll()).thenReturn(sampleArtists);

            ArtistNotFoundException exception = assertThrows(
                    ArtistNotFoundException.class,
                    () -> artistInfosService.findAll(0, 10, true, "nonexistent")
            );

            assertTrue(exception.getMessage().contains("nonexistent"));
        }
    }

    @Nested
    @DisplayName("findById")
    class FindByIdTests {

        @Test
        @DisplayName("should return the artist matching the given ID")
        void shouldReturnArtistById() {
            when(artistInfosRepository.findAll()).thenReturn(sampleArtists);

            ArtistInfo result = artistInfosService.findById("id-3");

            assertEquals("id-3", result.getId());
            assertEquals("Imagine Dragons", result.getName());
        }

        @Test
        @DisplayName("should throw ArtistNotFoundException when ID does not exist")
        void shouldThrowExceptionWhenIdNotFound() {
            when(artistInfosRepository.findAll()).thenReturn(sampleArtists);

            ArtistNotFoundException exception = assertThrows(
                    ArtistNotFoundException.class,
                    () -> artistInfosService.findById("unknown-id")
            );

            assertTrue(exception.getMessage().contains("unknown-id"));
        }
    }
}
