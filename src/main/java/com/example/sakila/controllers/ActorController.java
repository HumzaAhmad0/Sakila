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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

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

    @PatchMapping("/actors/{id}")
    public ActorOutput updateActor(@PathVariable Short id, @RequestBody ActorInput input){

        final var actor = actorRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Actor not found"
        ));


        //can add not empty for validation later
        if (input.getFirstName() != null){
            actor.setFirstName(input.getFirstName().toUpperCase());
        }

        if (input.getLastName() != null){
            actor.setLastName(input.getLastName().toUpperCase());
        }

        if (input.getFilms() != null){
            final var films = input.getFilms().stream()
                    .map(filmId -> filmRepository
                            .findById(filmId)
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.BAD_REQUEST,
                                    String.format("A film with the id %d does not exist", filmId)
                            )))
                    .collect(Collectors.toCollection(ArrayList::new));
            //collect takes all items from stream
            //the collector is the argument which implements set of functions which adds to some collection
            //collection is generic parent datatype which has lists, hashmaps, etc
            //new empty array list added
            actor.setFilms(films);
        }

        final var updatedActor = actorRepository.save(actor);
        return ActorOutput.from(updatedActor);

    }

    @DeleteMapping("actors/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteActor(@PathVariable Short id){
        if (!actorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor not found");
        }

        actorRepository.deleteById(id);
    }



}
