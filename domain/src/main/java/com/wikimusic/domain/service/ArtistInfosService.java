package com.wikimusic.domain.service;

import com.wikimusic.domain.exception.ArtistNotFoundException;
import com.wikimusic.domain.model.ArtistInfo;
import com.wikimusic.domain.repository.ArtistInfosRepository;

import java.util.Comparator;
import java.util.List;

/**
 * ArtistInfosService — Domain service containing business rules for filtering,
 * sorting and paginating artist information.
 *
 * This class has no framework annotations; it is wired as a Spring bean by the
 * infrastructure configuration, preserving the dependency inversion principle.
 */
public class ArtistInfosService {

    private final ArtistInfosRepository artistInfosRepository;

    /**
     * Constructs the service with the given output port.
     *
     * @param artistInfosRepository the repository providing artist data
     */
    public ArtistInfosService(ArtistInfosRepository artistInfosRepository) {
        this.artistInfosRepository = artistInfosRepository;
    }

    public List<ArtistInfo> findAll(int page, int size, boolean sortAsc, String name) {
        List<ArtistInfo> artists = artistInfosRepository.findAll();

        if (name != null && !name.isBlank()) {
            artists = artists.stream()
                    .filter(artist -> artist.getName() != null
                            && artist.getName().toLowerCase().contains(name.toLowerCase()))
                    .toList();

            if (artists.isEmpty()) {
                throw new ArtistNotFoundException("name", name);
            }
        }

        List<ArtistInfo> sorted = sort(artists, sortAsc);

        return paginate(sorted, page, size);
    }

    public ArtistInfo findById(String id) {
        return artistInfosRepository.findAll().stream()
                .filter(artist -> id.equals(artist.getId()))
                .findFirst()
                .orElseThrow(() -> new ArtistNotFoundException(id));
    }

    /**
     * Sorts the artist list by name in ascending or descending order.
     *
     * @param artists the list to sort
     * @param sortAsc {@code true} for ascending, {@code false} for descending
     * @return a new sorted list
     */
    private List<ArtistInfo> sort(List<ArtistInfo> artists, boolean sortAsc) {
        Comparator<ArtistInfo> comparator = Comparator.comparing(
                ArtistInfo::getName, String.CASE_INSENSITIVE_ORDER);
        if (!sortAsc) {
            comparator = comparator.reversed();
        }
        return artists.stream().sorted(comparator).toList();
    }

    /**
     * Paginates the given list based on page index and page size.
     *
     * @param artists the full sorted list
     * @param page    zero-based page index
     * @param size    number of elements per page
     * @return a sublist representing the requested page
     */
    private List<ArtistInfo> paginate(List<ArtistInfo> artists, int page, int size) {
        int fromIndex = page * size;
        if (fromIndex >= artists.size()) {
            return List.of();
        }
        int toIndex = Math.min(fromIndex + size, artists.size());
        return artists.subList(fromIndex, toIndex);
    }
}
