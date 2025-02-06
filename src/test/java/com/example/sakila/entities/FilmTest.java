package com.example.sakila.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Year;

public class FilmTest {

    @Test
    public void testDefaultConstruct(){
        Film newFilm = new Film();

        {
            Assertions.assertNotNull(newFilm, "New film should not be null");
            Assertions.assertNull(newFilm.getTitle(), "Title should be null");
            Assertions.assertNull(newFilm.getDescription(), "Description should be null");
            Assertions.assertNull(newFilm.getReleaseYear(), "Release year should be null");
            Assertions.assertNull(newFilm.getMovieLength(), "Movie length year should be null");
            Assertions.assertNull(newFilm.getRating(), "Rating should be null");
            Assertions.assertNull(newFilm.getScore(), "Score should be null");
            Assertions.assertEquals("null for null weeks", newFilm.getRental(), "Default actor should have 2 null rentals");
        }
    }

    @Test
    public void testConstructWithValues(){
        Film newFilm = new Film();
        newFilm.setTitle("TESTING");
        newFilm.setDescription("Test Description");
        newFilm.setReleaseYear(Year.of(2003));
        newFilm.setMovieLength((short)3);
        newFilm.setRating("PG");
        newFilm.setScore(50.2F);
        newFilm.setRentalRate(10.99F);
        newFilm.setRentalDuration((byte) 3);

        {
        Assertions.assertNotNull(newFilm, "New film should not be null");
        Assertions.assertEquals("TESTING", newFilm.getTitle(), "title is not TESTING");
        Assertions.assertEquals("Test Description", newFilm.getDescription(), "description is not Test Description");
        Assertions.assertEquals(Year.of(2003) , newFilm.getReleaseYear(), "Year is not 2003");
        Assertions.assertEquals("PG" , newFilm.getRating(), "rating is not PG");
        Assertions.assertEquals((short)3 , newFilm.getMovieLength(), "movie length is not 3");
        Assertions.assertEquals(50.2F , newFilm.getScore(), "score is not 50.2");
        Assertions.assertEquals(10.99F , newFilm.getRentalRate(), "rental rate is not 10.99");
        Assertions.assertEquals((byte) 3 , newFilm.getRentalDuration(), "rental duration is not 3");
        Assertions.assertEquals("10.99 for 3 weeks", newFilm.getRental(), " film rental NOT combining rental rate and duration ");
        }
    }
}
