package com.example.sakila.controllers;

import com.example.sakila.dto.output.FilmOutput;
import com.example.sakila.entities.Film;
import com.example.sakila.repositories.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class FilmController {

    private FilmRepository filmRepository;

    @Autowired
    public FilmController(FilmRepository filmRepository){
        this.filmRepository = filmRepository;
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
}
