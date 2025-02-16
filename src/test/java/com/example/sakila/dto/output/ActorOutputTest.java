package com.example.sakila.dto.output;

import com.example.sakila.entities.Film;
import com.example.sakila.entities.Language;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.ArrayList;

public class ActorOutputTest {

    @Test
    public void testFilmOutputFromFilm(){
        Film film = new Film(
                (short)1,
                "a",
                "b",
                Year.of(2003),
                new Language((byte)2),
                (short)2,
                "PG",
                new ArrayList<>(),
                new ArrayList<>(),
                2.4F,
                4.4F,
                (byte)2
                );

        FilmOutput filmOutput = FilmOutput.from(film);

        {
            Assertions.assertNotNull(filmOutput, "FilmOutput should not be null");

            Assertions.assertNotNull(filmOutput.getId(), "ID should not be null");
            Assertions.assertEquals(film.getId(), filmOutput.getId(), "ID mismatch");

            Assertions.assertNotNull(filmOutput.getTitle(), "Title should not be null");
            Assertions.assertEquals(film.getTitle(), filmOutput.getTitle(), "Title mismatch");

            Assertions.assertNotNull(filmOutput.getDescription(), "Description should not be null");
            Assertions.assertEquals(film.getDescription(), filmOutput.getDescription(), "Description mismatch");

            Assertions.assertNotNull(filmOutput.getReleaseYear(), "Release Year should not be null");
            Assertions.assertEquals(film.getReleaseYear(), filmOutput.getReleaseYear(), "Release Year mismatch");

            Assertions.assertNotNull(filmOutput.getLanguage(), "Language should not be null");
            Assertions.assertEquals(film.getLanguage(), filmOutput.getLanguage(), "Language mismatch");

            Assertions.assertNotNull(filmOutput.getMovieLength(), "Movie Length should not be null");
            Assertions.assertEquals(film.getMovieLength(), filmOutput.getMovieLength(), "Movie Length mismatch");

            Assertions.assertNotNull(filmOutput.getRating(), "Rating should not be null");
            Assertions.assertEquals(film.getRating(), filmOutput.getRating(), "Rating mismatch");

            Assertions.assertNotNull(filmOutput.getScore(), "Score should not be null");
            Assertions.assertEquals(film.getScore(), filmOutput.getScore(), "Score mismatch");

            Assertions.assertNotNull(filmOutput.getRentalRate(), "Rental Rate should not be null");
            Assertions.assertEquals(film.getRentalRate(), filmOutput.getRentalRate(), "Rental Rate mismatch");

            Assertions.assertNotNull(filmOutput.getRentalDuration(), "Rental Duration should not be null");
            Assertions.assertEquals(film.getRentalDuration(), filmOutput.getRentalDuration(), "Rental Duration mismatch");

            Assertions.assertNotNull(filmOutput.getRental(), "Rental should not be null");
            Assertions.assertEquals(film.getRental(), filmOutput.getRental(), "Rental mismatch");

            Assertions.assertNotNull(filmOutput.getCast(), "Cast list should not be null");
            Assertions.assertTrue(filmOutput.getCast().isEmpty(), "Cast should be empty");

            Assertions.assertNotNull(filmOutput.getGenre(), "Genre list should not be null");
            Assertions.assertTrue(filmOutput.getGenre().isEmpty(), "Genre should be empty");
        }
    }



}
