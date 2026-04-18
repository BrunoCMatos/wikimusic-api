package com.wikimusic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * WikimusicApiApplication — Spring Boot entry point.
 *
 * Placed in the root package "com.wikimusic" so that Spring's component scan
 * automatically covers all sub-packages across every module:
 *   - com.wikimusic.infrastructure  (REST adapters, JPA adapters, config)
 *
 * Domain and application modules contain no Spring beans; they are wired
 * explicitly in infrastructure's BeanConfiguration.
 */
@SpringBootApplication
public class WikimusicApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WikimusicApiApplication.class, args);
    }
}
