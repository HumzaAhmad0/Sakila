package com.example.sakila.services;

import com.example.sakila.dto.input.ActorInput;
import com.example.sakila.entities.Actor;
import com.example.sakila.repositories.ActorRepository;
import com.example.sakila.repositories.FilmRepository;
import org.assertj.core.internal.Shorts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

public class ActorServiceTest {


    static ActorService actorService;
    static ActorRepository mockActorRepo = mock(ActorRepository.class);
    static FilmRepository mockFilmRepo = mock(FilmRepository.class);

    @BeforeAll
    public static void testSetup(){
        actorService = new ActorService(mockActorRepo, mockFilmRepo);
    }

    @Test
    public void testReturnAllActors(){
        List<Actor> actors = new ArrayList<>();
        actors.add(new Actor("dave", "testing"));
        actors.add(new Actor("testing2", "testing"));
        actors.add(new Actor("sam", "testing"));

        when(mockActorRepo.findAll()).thenReturn(actors);
        Assertions.assertEquals(actors, actorService.listActors(), "Return all actors not working");
    }

    //add test for wrong id
    @Test
    public void testReturnActorByID(){
        Short id = 3;
        Actor actor = new Actor((short)3, "testing2", "sam");

        when(mockActorRepo.findById(id)).thenReturn(Optional.of(actor));
        Assertions.assertEquals(Optional.of(actor), actorService.getActorById(id), "returning actor by id not working");
    }

    @Test
    public void testReturnActorByNameContaining(){
        List<Actor> filteredActors = new ArrayList<>();
        filteredActors.add(new Actor("ab", "cd"));
        filteredActors.add(new Actor("ib", "zo"));

        String search = "b";

        when(mockActorRepo.findAllByFullNameContainingIgnoreCase(search)).thenReturn(filteredActors);
        Assertions.assertEquals(filteredActors, actorService.listActorsByFullName(search), "filtering actors by name not working");
    }

    @Test
    public void testCreateActor(){
        ActorInput actorInput = new ActorInput();
        actorInput.setFirstName("T");
        actorInput.setLastName("L");

        String fName = "T";
        String lName = "L";

        Actor savedActor = new Actor(fName,lName);

        when(mockActorRepo.save(any(Actor.class))).then(returnsFirstArg());
        Actor returnedActor = actorService.createActor(actorInput);

        Assertions.assertEquals(savedActor.getFullName(),returnedActor.getFullName(), "creating new actor not working");
    }

    @Test
    public void testDeleteActor(){
        Short id = (short)3;

        when(mockActorRepo.existsById(id)).thenReturn(true);
        Assertions.assertThrowsExactly(ResponseStatusException.class, () -> actorService.deleteActor(id), "delete not working");
    }
}
