package com.example.sakila.controllers;

import com.example.sakila.dto.input.FilmInput;
import com.example.sakila.dto.output.FilmOutput;
import com.example.sakila.entities.Film;
import com.example.sakila.repositories.ActorRepository;
import com.example.sakila.repositories.FilmRepository;
import com.example.sakila.repositories.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class FilmController {

    private FilmRepository filmRepository;
    private ActorRepository actorRepository;
    private LanguageRepository languageRepository;

    @Autowired
    public FilmController(FilmRepository filmRepository,
                          ActorRepository actorRepository,
                          LanguageRepository languageRepository){
        this.filmRepository = filmRepository;
        this.actorRepository = actorRepository;
        this.languageRepository = languageRepository;
    }

    @GetMapping ("/films")
    public List<FilmOutput> retrieveMovies(){
        return filmRepository.findAll()
                .stream()
                .map(FilmOutput::from)
                .toList();
    }

    @GetMapping ("/films/{id}")
    public FilmOutput retrieveMoviesByID(@PathVariable Short id){
        return filmRepository.findById(id)
                .map(FilmOutput::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping ("/films")
    public FilmOutput createFilm(@RequestBody FilmInput input){
        final var film = new Film();

        film.setTitle(input.getTitle());
        film.setDescription(input.getDescription());
        film.setReleaseYear(input.getReleaseYear());
        film.setMovieLength(input.getMovieLength());
        film.setRating(input.getRating());

        final var movieLanguage = languageRepository
                .findById(input.getLanguage())
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        String.format("A language with the id %d does not exist", input.getLanguage())
                ));

        film.setLanguage(movieLanguage);

        final var actors = input.getActors()
                .stream()
                .map(actorId -> actorRepository
                        .findById(actorId)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                String.format("A film with the id %d does not exist", actorId)
                        )))
                .toList();

        film.setCast(actors);

        final var savedFilms = filmRepository.save(film);
        return FilmOutput.from(savedFilms);
    }

    @DeleteMapping("films/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFilm(@PathVariable Short id){
        if (!filmRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Film not found");
        }

        filmRepository.deleteById(id);
    }
}
