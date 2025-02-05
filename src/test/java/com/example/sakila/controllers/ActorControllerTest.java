package com.example.sakila.controllers;

import com.example.sakila.dto.input.ActorInput;
import com.example.sakila.dto.output.ActorOutput;
import com.example.sakila.entities.Actor;
import com.example.sakila.services.ActorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class ActorControllerTest {

    static ActorController actorController;
    static ActorService mockActorService = mock(ActorService.class);

    @BeforeAll
    public static void testSetUp(){
        actorController = new ActorController(mockActorService);
    }

    @Test
    public void testGetActor(){
        Optional<String> search = Optional.of("b");

        Actor actor1 = new Actor((short)2,"ab", "cd", new ArrayList<>());
        Actor actor2 = new Actor((short)3,"ib", "zo", new ArrayList<>());
        Actor actor3 = new Actor((short)4, "b", "b", new ArrayList<>());

        List<Actor> actors = new ArrayList<>();
        actors.add(actor1);
        actors.add(actor2);
        actors.add(actor3);

        when(mockActorService.listActorsByFullName("b")).thenReturn(actors);
        List<ActorOutput> returnedActors = actorController.getActor(search);

        Assertions.assertNotNull(returnedActors);
        Assertions.assertEquals(3, returnedActors.size());

        ActorOutput actorOutput1 = returnedActors.getFirst();
        Assertions.assertEquals("ab", actorOutput1.getFirstName(), "First actor's first name should be 'ab'");
        Assertions.assertEquals("cd", actorOutput1.getLastName(), "First actor's last name should be 'cd'");

        ActorOutput actorOutput3 = returnedActors.getLast();
        Assertions.assertEquals("b", actorOutput3.getFirstName(), "Third actor's first name should be 'b'");
        Assertions.assertEquals("b", actorOutput3.getLastName(), "Third actor's last name should be 'd'");
    }

    @Test
    public void testGetSpecificActor(){
        Short id = (short)2;

        Actor specifiedActor = new Actor(id);
        specifiedActor.setFilms(Collections.emptyList());

        when(mockActorService.getActorById(id)).thenReturn(Optional.of(specifiedActor));
        ActorOutput returnedOutputActor = actorController.getSpecificActor(id);

        Assertions.assertEquals((short)2, returnedOutputActor.getId(),"id should be 2");
    }

    @Test
    public void testNewActor(){
        ActorInput input = new ActorInput();
        input.setFirstName("dave");

        Actor actor = new Actor();
        actor.setFirstName("dave");
        actor.setFilms(Collections.emptyList());

        when(mockActorService.createActor(input)).thenReturn(actor);
        ActorOutput returnedActorOutput = actorController.newActor(input);

        Assertions.assertEquals("dave", returnedActorOutput.getFirstName(), "first name should be dave");
    }

    @Test
    public void testUpdateActor(){
        Short id = (short)2;
        ActorInput input = new ActorInput();
        input.setFirstName("dave");
        input.setFilms(Collections.emptyList());

        Actor updatedActor = new Actor(id);
        updatedActor.setFirstName("luke");
        updatedActor.setFilms(Collections.emptyList());

        when(mockActorService.updateActor(id,input)).thenReturn(updatedActor);
        ActorOutput returnedActorOutput = actorController.updateActor(id, input);

        Assertions.assertEquals("luke", returnedActorOutput.getFirstName(), "first name should be luke");
    }

    @Test
    public void testDeleteActor(){
        Short id = (short)2;

        actorController.deleteActor(id);
        verify(mockActorService, times(1)).deleteActor(id);
    }
    @Test
    public void testDeleteActorWrongId(){
        Short id = (short)2;

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(mockActorService).deleteActor(id);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> actorController.deleteActor(id));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode(), "404 not found should be the status");

    }
}
