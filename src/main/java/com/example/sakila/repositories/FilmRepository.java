package com.example.sakila.repositories;

import com.example.sakila.entities.Film;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Year;
import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Short> {
    List<Film> findAllByTitleContainingIgnoreCase(String title);
    List<Film> findAllByCategories_NameContainingIgnoreCase(String categoryName);
    List<Film> findAllByRatingContainingIgnoreCase(String rating);
    List<Film> findAllByReleaseYear(Year releaseYear);

    List<Film> findAllByTitleContainingIgnoreCaseAndCategories_NameContainingIgnoreCase(String title, String categoryName);
    List<Film> findAllByTitleContainingIgnoreCaseAndRatingContainingIgnoreCase(String title, String rating);
    List<Film> findAllByTitleContainingIgnoreCaseAndReleaseYear(String title, Year releaseYear);

    List<Film> findAllByCategories_NameContainingIgnoreCaseAndRatingContainingIgnoreCase(String categoryName, String rating);
    List<Film> findAllByCategories_NameContainingIgnoreCaseAndReleaseYear(String categoryName, Year releaseYear);

    List<Film> findAllByRatingContainingIgnoreCaseAndReleaseYear(String rating, Year releaseYear);

    List<Film> findAllByTitleContainingIgnoreCaseAndCategories_NameContainingIgnoreCaseAndRatingContainingIgnoreCase(String title, String categoryName, String rating);
    List<Film> findAllByTitleContainingIgnoreCaseAndCategories_NameContainingIgnoreCaseAndReleaseYear(String title, String categoryName, Year releaseYear);
    List<Film> findAllByTitleContainingIgnoreCaseAndRatingContainingIgnoreCaseAndReleaseYear(String title, String rating, Year releaseYear);
    List<Film> findAllByCategories_NameContainingIgnoreCaseAndRatingContainingIgnoreCaseAndReleaseYear(String categoryName, String rating, Year releaseYear);

    List<Film> findAllByTitleContainingIgnoreCaseAndCategories_NameContainingIgnoreCaseAndRatingContainingIgnoreCaseAndReleaseYear(String title, String categoryName, String rating, Year releaseYear);
}
