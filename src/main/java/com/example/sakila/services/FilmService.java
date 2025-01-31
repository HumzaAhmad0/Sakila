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
                       CategoryRepository categoryRepository){
        this.filmRepository = filmRepository;
        this.actorRepository = actorRepository;
        this.languageRepository = languageRepository;
        this.categoryRepository = categoryRepository;
    }

    public Optional<Film> getFilmById(Short id){
        return filmRepository.findById(id);
    }

    public List<Film> listFilms(){
        return filmRepository.findAll();
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

        if (input.getLanguage() != null){
            final var movieLanguage = languageRepository
                    .findById(input.getLanguage())
                    .orElseThrow(()-> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            String.format("A language with the id %d does not exist", input.getLanguage())
                    ));

            film.setLanguage(movieLanguage);
        }

        //WHY IS THIS ASKING TO MAKE METHOD THROWABLE
        if (input.getGenre() != null){
            final var movieGenre = categoryRepository
                    .findById(input.getGenre())
                    .orElseThrow(()-> new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("A Genre with the id %d does not exist", input.getGenre())
            ));
            film.setGenre(movieGenre);
            //NOT LETTING ME SET GENRE WITHOUT CASTING THE ARGUMENT??
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
