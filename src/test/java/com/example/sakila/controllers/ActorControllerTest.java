package com.example.sakila.controllers;

import com.example.sakila.dto.input.ActorInput;
import com.example.sakila.dto.output.ActorOutput;
import com.example.sakila.entities.Actor;
import com.example.sakila.entities.Film;
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

        ArrayList<Film> films = new ArrayList<>();
        films.add(new Film((short)2));
        films.add(new Film((short)3));
        films.add(new Film((short)4));


        Actor actor1 = new Actor((short)2,"ab", "cd", new ArrayList<>());
        Actor actor2 = new Actor((short)3,"ib", "zo", new ArrayList<>());
        Actor actor3 = new Actor((short)4, "b", "b", films);

        List<Actor> actors = new ArrayList<>();
        actors.add(actor1);
        actors.add(actor2);
        actors.add(actor3);

        when(mockActorService.listActorsByFullName("b")).thenReturn(actors);
        List<ActorOutput> returnedActors = actorController.getActor(search);

        Assertions.assertNotNull(returnedActors, "returned actors should not be null");
        Assertions.assertEquals(3, returnedActors.size(), "returned actors should have 3 actors");

        ActorOutput actorOutput1 = returnedActors.getFirst();
        Assertions.assertEquals("ab", actorOutput1.getFirstName(), "First actor's first name should be 'ab'");
        Assertions.assertEquals("cd", actorOutput1.getLastName(), "First actor's last name should be 'cd'");
        Assertions.assertTrue(actorOutput1.getFilms().isEmpty(), "First actor's films should be empty ");

        ActorOutput actorOutput2 = returnedActors.get(1);
        Assertions.assertEquals("ib", actorOutput2.getFirstName(), "Second actor's first name should be 'ib'");
        Assertions.assertEquals("zo", actorOutput2.getLastName(), "Second actor's last name should be 'zo'");
        Assertions.assertTrue(actorOutput2.getFilms().isEmpty(), "Second actor's films should be empty ");

        ActorOutput actorOutput3 = returnedActors.getLast();
        Assertions.assertEquals("b", actorOutput3.getFirstName(), "Third actor's first name should be 'b'");
        Assertions.assertEquals("b", actorOutput3.getLastName(), "Third actor's last name should be 'd'");
        Assertions.assertEquals(3,actorOutput3.getFilms().size(), "third actor's films have 3 films ");
    }

    @Test
    public void testGetSpecificActor(){
        Short id = (short)2;

        Actor specifiedActor = new Actor(id);
        specifiedActor.setFilms(Collections.emptyList());

        when(mockActorService.getActorById(id)).thenReturn(Optional.of(specifiedActor));
        ActorOutput returnedOutputActor = actorController.getSpecificActor(id);
        {
            Assertions.assertNotNull(returnedOutputActor, "returned actor output should not be null");
            Assertions.assertEquals((short) 2, returnedOutputActor.getId(), "id should be 2");
            Assertions.assertTrue(returnedOutputActor.getFilms().isEmpty(), "returned actors should be empty");
        }
    }

    @Test
    public void testGetAllActors(){
        ArrayList<Film> films = new ArrayList<>();
        films.add(new Film((short)2));
        films.add(new Film((short)3));
        films.add(new Film((short)4));

        List<Actor> actors = new ArrayList<>();
        actors.add(new Actor((short)1, "AAA", "BBB", new ArrayList<>()));
        actors.add(new Actor((short)2, "CCC", "DDD", films));
        actors.add(new Actor((short)3, "EEE", "FFF", new ArrayList<>()));

        when(mockActorService.listActors()).thenReturn(actors);
        List<ActorOutput> returnedActors = actorController.getActor(Optional.empty());

        {
        Assertions.assertNotNull(returnedActors,"returned actor should not be null");

        Assertions.assertEquals((short)1,returnedActors.getFirst().getId(), "first actor id should be 1");
        Assertions.assertEquals("AAA BBB",returnedActors.getFirst().getFullName(), "first actor full name incorrect");
        Assertions.assertTrue(returnedActors.getFirst().getFilms().isEmpty(), "First actor output films should be empty");

        Assertions.assertEquals((short)2,returnedActors.get(1).getId(), "second actor id should be 2");
        Assertions.assertEquals("CCC DDD",returnedActors.get(1).getFullName(), "second actor full name incorrect");
        Assertions.assertEquals(3,returnedActors.get(1).getFilms().size(), "Second actor output films should have 3 films");

        Assertions.assertEquals((short)3,returnedActors.getLast().getId(), "third actor id should be 3");
        Assertions.assertEquals("EEE FFF",returnedActors.getLast().getFullName(), "third actor full name incorrect");
        Assertions.assertTrue(returnedActors.getLast().getFilms().isEmpty(), "Third actor output films should be empty");
        }
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

        {
            Assertions.assertNotNull(returnedActorOutput, "returnedActorOutput should not be null");
            Assertions.assertEquals("dave", returnedActorOutput.getFirstName(), "first name should be dave");
            Assertions.assertTrue(returnedActorOutput.getFilms().isEmpty(), "films should be empty");
        }
    }

    @Test
    public void testUpdateActor() {
        Short id = (short) 2;
        ActorInput input = new ActorInput();
        input.setFirstName("luke");
        input.setFilms(Collections.emptyList());

        Actor updatedActor = new Actor(id);
        updatedActor.setFirstName("luke");
        updatedActor.setFilms(Collections.emptyList());

        when(mockActorService.updateActor(id, input)).thenReturn(updatedActor);
        ActorOutput returnedActorOutput = actorController.updateActor(id, input);

        {
            Assertions.assertNotNull(returnedActorOutput, "returnedActorOutput should not be null");
            Assertions.assertEquals("luke", returnedActorOutput.getFirstName(), "first name should be luke");
            Assertions.assertTrue(returnedActorOutput.getFilms().isEmpty(), "films should be empty");
        }
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
