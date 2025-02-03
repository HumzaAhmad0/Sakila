package com.example.sakila.controllers;

import com.example.sakila.dto.ValidationGroup;
import com.example.sakila.dto.input.FilmInput;
import com.example.sakila.dto.output.FilmOutput;
import com.example.sakila.services.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Year;
import java.util.List;

@RestController
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService){
        this.filmService = filmService;
    }

    @GetMapping ("/films")
    public List<FilmOutput> retrieveMovies(@RequestParam(required = false) String title,
                                           @RequestParam(required = false) String categoryName,
                                           @RequestParam(required = false) String rating,
                                           @RequestParam(required = false) Year year){
        return filmService.listFilms(title,categoryName,rating,year)
                .stream()
                .map(FilmOutput::from)
                .toList();
    }

    @GetMapping ("/films/{id}")
    public FilmOutput retrieveMoviesByID(@PathVariable Short id){
        return filmService.getFilmById(id)
                .map(FilmOutput::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping ("/films")
    public FilmOutput createFilm(@Validated(ValidationGroup.Post.class)@RequestBody FilmInput input){
        final var savedFilms = filmService.createFilm(input);
        return FilmOutput.from(savedFilms);
    }

    @PutMapping("/films/{id}")
    public FilmOutput replaceFilm(@PathVariable Short id,
                                  @Validated(ValidationGroup.Put.class)@RequestBody FilmInput input){
        final var updatedFilm = filmService.updateFilm(id,input);
        return FilmOutput.from(updatedFilm);
    }

    @PatchMapping("/films/{id}")
    public FilmOutput editFilm(@PathVariable Short id,
                               @Validated(ValidationGroup.Patch.class) @RequestBody FilmInput input){
        final var updatedFilm = filmService.updateFilm(id,input);
        return FilmOutput.from(updatedFilm);
    }

    @DeleteMapping("/films/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFilm(@PathVariable Short id){
        filmService.deleteFilm(id);
    }
}
