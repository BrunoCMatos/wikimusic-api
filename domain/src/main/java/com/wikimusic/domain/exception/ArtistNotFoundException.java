package com.wikimusic.domain.exception;

/**
 * ArtistNotFoundException — Domain exception thrown when an Artist cannot be found.
 * Mapped to HTTP 404 by the infrastructure exception handler.
 */
public class ArtistNotFoundException extends RuntimeException {

    /**
     * Creates an exception for an artist not found by id.
     *
     * @param id the artist identifier that was not found
     */
    public ArtistNotFoundException(String id) {
        super("Artist not found with id: " + id);
    }

    /**
     * Creates an exception for an artist not found by a specific field.
     *
     * @param field the field name used in the search (e.g. "name")
     * @param value the value that was searched for
     */
    public ArtistNotFoundException(String field, String value) {
        super(String.format("Artist not found with %s: %s", field, value));
    }
}
