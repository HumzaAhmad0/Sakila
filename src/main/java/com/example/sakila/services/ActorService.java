package com.example.sakila.services;

import com.example.sakila.dto.input.ActorInput;
import com.example.sakila.entities.Actor;
import com.example.sakila.repositories.ActorRepository;
import com.example.sakila.repositories.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActorService {

    private final ActorRepository actorRepository;
    private final FilmRepository filmRepository;

    @Autowired
    public ActorService(ActorRepository actorRepository, FilmRepository filmRepository){
        this.actorRepository = actorRepository;
        this.filmRepository = filmRepository;
    }

    public Optional<Actor> getActorById(Short id){
        Optional<Actor> actor= actorRepository.findById(id);
        if (actor.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return actor;
    }

    public List<Actor> listActors(){
        return actorRepository.findAll();
    }

    public List<Actor> listActorsByFullName(String name){
        return actorRepository.findAllByFullNameContainingIgnoreCase(name);
    }

    private void updateActorFromInput(Actor actor, ActorInput input){
        if (input.getFirstName() != null) actor.setFirstName(input.getFirstName().toUpperCase());
        if (input.getLastName() != null) actor.setLastName(input.getLastName().toUpperCase());

        if (input.getFilms() != null){
            System.out.println("printing");
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
            //collection is generic parent datatype which has lists, hashmaps, etc.
            //new empty array list added
            actor.setFilms(films);
        }
    }

    public Actor createActor(ActorInput input){
        final var actor = new Actor();
        updateActorFromInput(actor, input);
        return actorRepository.save(actor);
    }

    public Actor updateActor(Short id, ActorInput input){
        final var actor = actorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        updateActorFromInput(actor, input);
        return actorRepository.save(actor);
    }

    public void deleteActor(Short id){
        if (!actorRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        actorRepository.deleteById(id);
    }


}
