package com.example.sakila.controllers;

import com.example.sakila.dto.input.ActorInput;
import com.example.sakila.dto.output.ActorOutput;
import com.example.sakila.entities.Actor;
import com.example.sakila.repositories.ActorRepository;
import com.example.sakila.repositories.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class ActorController {

    private ActorRepository actorRepository;
    private FilmRepository filmRepository;

    @Autowired
    public ActorController(ActorRepository actorRepository, FilmRepository filmRepository){
        this.actorRepository = actorRepository;
        this.filmRepository = filmRepository;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @GetMapping ("/actors")
    public List<ActorOutput> getActor(@RequestParam(required = false) Optional<String> name){
        return name
                .map(value -> actorRepository.findAllByFullNameContainingIgnoreCase(value))
                .orElseGet(() -> actorRepository.findAll())
                .stream()
                .map(ActorOutput::from)
                .toList();
    }

    @GetMapping("/actors/{id}")
    public ActorOutput getSpecificActor(@PathVariable Short id){
        return actorRepository.findById(id)
                .map(ActorOutput::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/actors")
    public ActorOutput newActor(@RequestBody ActorInput input){
        final var actor = new Actor();
        actor.setFirstName(input.getFirstName().toUpperCase());
        actor.setLastName(input.getLastName().toUpperCase());

        final var films = input.getFilms()
                .stream()
                .map(filmId -> filmRepository
                        .findById(filmId)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                String.format("A film with the id %d does not exist", filmId)
                        )))
                .toList();

        actor.setFilms(films);
        final var savedActor = actorRepository.save(actor);
        return ActorOutput.from(savedActor);
    }

    @PutMapping("/actors/{id}")
    public String updateActor(@PathVariable Short id){
        return "update an actor";

        //change to patch
    }

    @DeleteMapping("/actors/{id}")
    public String deleteActor(@PathVariable Short id){
        return"delete actor by id ";
    }

}
