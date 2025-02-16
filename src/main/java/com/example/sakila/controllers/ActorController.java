package com.example.sakila.controllers;

import com.example.sakila.dto.ValidationGroup;
import com.example.sakila.dto.input.ActorInput;
import com.example.sakila.dto.output.ActorOutput;
import com.example.sakila.services.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class ActorController {

    private final ActorService actorService;

    @Autowired
    public ActorController(ActorService actorService){
        this.actorService = actorService;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @GetMapping ("/actors")
    public List<ActorOutput> getActor(@RequestParam(required = false) Optional<String> name){
        return name
                .map(actorService::listActorsByFullName)
                .orElseGet(actorService::listActors)
                .stream()
                .map(ActorOutput::from)
                .toList();
    }

    @GetMapping("/actors/{id}")
    public ActorOutput getSpecificActor(@PathVariable Short id){
        return actorService.getActorById(id)
                .map(ActorOutput::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/actors")
    public ActorOutput newActor(@Validated(ValidationGroup.Post.class)@RequestBody ActorInput input){
        final var savedActor = actorService.createActor(input);
        return ActorOutput.from(savedActor);
    }

    @PutMapping("/actors/{id}")
    public ActorOutput replaceActor(@PathVariable short id, @Validated(ValidationGroup.Put.class)@RequestBody ActorInput input){
        final var updatedActor = actorService.updateActor(id, input);
        return ActorOutput.from(updatedActor);
    }

    @PatchMapping("/actors/{id}")
    public ActorOutput updateActor(@PathVariable Short id, @Validated(ValidationGroup.Patch.class) @RequestBody ActorInput input){
        final var updatedActor = actorService.updateActor(id, input);
        return ActorOutput.from(updatedActor);
    }

    @DeleteMapping("/actors/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteActor(@PathVariable Short id){
        actorService.deleteActor(id);
    }
}
