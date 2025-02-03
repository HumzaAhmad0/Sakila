package com.example.sakila.services;

import com.example.sakila.dto.input.FilmInput;
import com.example.sakila.entities.Film;
import com.example.sakila.repositories.ActorRepository;
import com.example.sakila.repositories.CategoryRepository;
import com.example.sakila.repositories.FilmRepository;
import com.example.sakila.repositories.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmRepository filmRepository;
    private final ActorRepository actorRepository;
    private final LanguageRepository languageRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public FilmService(FilmRepository filmRepository,
                       ActorRepository actorRepository,
                       LanguageRepository languageRepository,
                       CategoryRepository categoryRepository
    ){
        this.filmRepository = filmRepository;
        this.actorRepository = actorRepository;
        this.languageRepository = languageRepository;
        this.categoryRepository = categoryRepository;
    }

    public Optional<Film> getFilmById(Short id){
        return filmRepository.findById(id);
    }

    public List<Film> listFilms(String title, String categoryName, String rating, Year year){
        if (title == null && categoryName == null && rating == null && year == null) {
            return filmRepository.findAll();
        }

        String filters =
                (title != null ? "title" : "") +
                (categoryName != null ? "category" : "") +
                (rating != null ? "rating" : "") +
                (year != null ? "year" : "");

        return switch (filters) {
            case "title" -> filmRepository.findAllByTitleContainingIgnoreCase(title);
            case "category" -> filmRepository.findAllByCategories_NameContainingIgnoreCase(categoryName);
            case "rating" -> filmRepository.findAllByRatingContainingIgnoreCase(rating);
            case "year" -> filmRepository.findAllByReleaseYear(year);
            case "titlecategory" ->
                    filmRepository.findAllByTitleContainingIgnoreCaseAndCategories_NameContainingIgnoreCase(title, categoryName);
            case "titlerating" ->
                    filmRepository.findAllByTitleContainingIgnoreCaseAndRatingContainingIgnoreCase(title, rating);
            case "titleyear" -> filmRepository.findAllByTitleContainingIgnoreCaseAndReleaseYear(title, year);
            case "categoryrating" ->
                    filmRepository.findAllByCategories_NameContainingIgnoreCaseAndRatingContainingIgnoreCase(categoryName, rating);
            case "categoryyear" -> filmRepository.findAllByCategories_NameContainingIgnoreCaseAndReleaseYear(categoryName, year);
            case "ratingyear" -> filmRepository.findAllByRatingContainingIgnoreCaseAndReleaseYear(rating, year);
            case "titlecategoryrating" ->
                    filmRepository.findAllByTitleContainingIgnoreCaseAndCategories_NameContainingIgnoreCaseAndRatingContainingIgnoreCase(title, categoryName, rating);
            case "titlecategoryyear" ->
                    filmRepository.findAllByTitleContainingIgnoreCaseAndCategories_NameContainingIgnoreCaseAndReleaseYear(title, categoryName, year);
            case "titleratingyear" ->
                    filmRepository.findAllByTitleContainingIgnoreCaseAndRatingContainingIgnoreCaseAndReleaseYear(title, rating, year);
            case "categoryratingyear" ->
                    filmRepository.findAllByCategories_NameContainingIgnoreCaseAndRatingContainingIgnoreCaseAndReleaseYear(categoryName, rating, year);
            case "titlecategoryratingyear" ->
                    filmRepository.findAllByTitleContainingIgnoreCaseAndCategories_NameContainingIgnoreCaseAndRatingContainingIgnoreCaseAndReleaseYear(title, categoryName, rating, year);
            default -> filmRepository.findAll();
        };
    }

    public Film createFilm(FilmInput input){
        final var film = new Film();
        updateFilmFromInput(film, input);
        return filmRepository.save(film);
    }

    public Film updateFilm(Short id, FilmInput input){
        final var film = filmRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        updateFilmFromInput(film, input);
        return filmRepository.save(film);
    }

    public void updateFilmFromInput(Film film, FilmInput input){
        if (input.getTitle() != null) film.setTitle(input.getTitle());
        if (input.getDescription() != null) film.setDescription(input.getDescription());
        if (input.getReleaseYear() != null) film.setReleaseYear(input.getReleaseYear());
        if (input.getMovieLength() != null) film.setMovieLength(input.getMovieLength());
        if (input.getRating() != null) film.setRating(input.getRating());
        if (input.getScore() != null) film.setScore(input.getScore());
        if (input.getRentalRate() != null) film.setRentalRate(input.getRentalRate());
        if (input.getRentalDuration() != null) film.setRentalDuration(input.getRentalDuration());

        if (input.getLanguage() != null){
            final var movieLanguage = languageRepository
                    .findById(input.getLanguage())
                    .orElseThrow(()-> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            String.format("A language with the id %d does not exist", input.getLanguage())
                    ));
            film.setLanguage(movieLanguage);
        }

        if (input.getGenre() != null){
            final var movieGenre = input.getGenre().stream().map(
                    genreID -> categoryRepository
                    .findById(genreID)
                    .orElseThrow(()-> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            String.format("A Genre with the id %d does not exist", genreID)

                    )))
                    .collect(Collectors.toCollection(ArrayList::new));
            film.setCategories(movieGenre);
        }

        if (input.getActors() != null){
            final var actors = input.getActors()
                    .stream()
                    .map(actorId -> actorRepository
                            .findById(actorId)
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.BAD_REQUEST,
                                    String.format("A film with the id %d does not exist", actorId)
                            )))
                    .collect(Collectors.toCollection(ArrayList::new));
            film.setCast(actors);
        }

    }

    public void deleteFilm(Short id){
        if (!filmRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Film not found");
        }
        filmRepository.deleteById(id);
    }

}
