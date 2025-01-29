package com.example.sakila.controllers;

import com.example.sakila.dto.output.ActorOutput;
import com.example.sakila.entities.Actor;
import com.example.sakila.repositories.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class ActorController {

    private ActorRepository actorRepository;

    @Autowired
    public ActorController(ActorRepository actorRepository){
        this.actorRepository = actorRepository;
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
    public String newActor(){
        return "create new actor";
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
