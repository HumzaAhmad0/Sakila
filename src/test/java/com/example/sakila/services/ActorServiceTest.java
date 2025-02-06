package com.example.sakila.services;

import com.example.sakila.dto.input.ActorInput;
import com.example.sakila.entities.Actor;
import com.example.sakila.entities.Film;
import com.example.sakila.repositories.ActorRepository;
import com.example.sakila.repositories.FilmRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
    public void testReturnActorByID(){
        Short id = 3;
        Actor actor = new Actor(id, "testing2", "sam");

        when(mockActorRepo.findById(id)).thenReturn(Optional.of(actor));
        Optional<Actor> returnedActor = actorService.getActorById(id);

        {
            Assertions.assertNotNull(returnedActor);
            Assertions.assertTrue(returnedActor.isPresent());

            Actor retrievedActor = returnedActor.get();
            Assertions.assertEquals(retrievedActor.getId(), actor.getId(), "id not matching");
            Assertions.assertEquals(retrievedActor.getFullName(), actor.getFullName(), "full name not matching");
            Assertions.assertEquals(Optional.of(actor), returnedActor, "returning actor by id not working");
        }
    }
    @Test
    public void testReturnActorByWrongID(){
        Short id = 3;

        when(mockActorRepo.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(ResponseStatusException.class, () -> actorService.getActorById(id), "wrong id message not working");
    }

    @Test
    public void testReturnAllActors(){
        Film testFilm = new Film((short)2);

        List<Film> films = new ArrayList<>();
        films.add(new Film((short)1));
        films.add(testFilm);
        films.add(new Film((short)3));

        List<Actor> actors = new ArrayList<>();
        actors.add(new Actor("dave", "testing", new ArrayList<>()));
        actors.add(new Actor("testing2", "testing",new ArrayList<>()));
        actors.add(new Actor("sam", "testing", films));

        when(mockActorRepo.findAll()).thenReturn(actors);
        List<Actor> returnedActors = actorService.listActors();

        {
            Assertions.assertNotNull(returnedActors, "returned actors should not be null");
            Assertions.assertEquals(3, returnedActors.size(), "returned actors should have 3 actors");

            Assertions.assertEquals(actors.getFirst().getFullName(), returnedActors.getFirst().getFullName(), "first returned full name incorrect");
            Assertions.assertTrue(returnedActors.getFirst().getFilms().isEmpty(), "returned actor 1 film should be empty");

            Assertions.assertEquals(actors.get(1).getFullName(), returnedActors.get(1).getFullName(), "second returned full name incorrect");
            Assertions.assertTrue(returnedActors.get(1).getFilms().isEmpty(), "returned actor 2 film should be empty");

            Assertions.assertEquals(actors.getLast().getFullName(), returnedActors.getLast().getFullName(), "third returned full name incorrect");
            Assertions.assertEquals(3,returnedActors.getLast().getFilms().size(), "returned actor 3 film should have 3 films");
            Assertions.assertTrue(returnedActors.getLast().getFilms().contains(testFilm), "returned actor 3 films should include testFilm");

        }
    }

    @Test
    public void testReturnActorByNameContaining(){
        List<Actor> filteredActors = new ArrayList<>();
        filteredActors.add(new Actor((short)1,"ab", "cd"));
        filteredActors.add(new Actor((short)2,"ib", "zo"));

        String search = "b";

        when(mockActorRepo.findAllByFullNameContainingIgnoreCase(search)).thenReturn(filteredActors);
        List<Actor> returnedActors = actorService.listActorsByFullName(search);

        {
            Assertions.assertNotNull(returnedActors, "returned actors should not be null");
            Assertions.assertEquals(2, returnedActors.size(), "returned should have 2 actors");

            Assertions.assertEquals(filteredActors.getFirst().getId(), returnedActors.getFirst().getId(), "id do not match for first output");
            Assertions.assertEquals(filteredActors.getFirst().getFullName(), returnedActors.getFirst().getFullName(), "full names do not match for first output");

            Assertions.assertEquals(filteredActors.getLast().getId(), returnedActors.getLast().getId(), "id do not match for last output");
            Assertions.assertEquals(filteredActors.getLast().getFullName(), returnedActors.getLast().getFullName(), "full names do not match for last output");
        }
    }

    @Test
    public void testCreateActor(){
        ActorInput input = new ActorInput();
        input.setFirstName("T");
        input.setLastName("L");

        Actor savedActor = new Actor("T","L");

        when(mockActorRepo.save(any(Actor.class))).then(returnsFirstArg());
        Actor returnedActor = actorService.createActor(input);

        Assertions.assertNotNull(returnedActor, "returned actor should not be null");
        Assertions.assertEquals(savedActor.getFullName(),returnedActor.getFullName(), "creating new actor not working");
    }

    @Test
    public void testUpdateActorById(){
        Short id = 2;
        Actor oldActor = new Actor(id);
        oldActor.setFirstName("t".toUpperCase());
        oldActor.setLastName("l".toUpperCase());

        ActorInput input = new ActorInput();
        input.setFirstName("P".toUpperCase());

        when(mockActorRepo.findById(id)).thenReturn(Optional.of(oldActor));
        when(mockActorRepo.save(any(Actor.class))).then(returnsFirstArg());
        Actor updatedActor = actorService.updateActor(id,input);

        {
            Assertions.assertNotNull(updatedActor, "updated actor should not be null");
            Assertions.assertEquals("P", updatedActor.getFirstName(), "Not updating by id correctly");
            Assertions.assertEquals(oldActor.getLastName(), updatedActor.getLastName(), "last names do not match");
            Assertions.assertEquals(oldActor.getId(), updatedActor.getId(), "id names do not match");
        }
    }

    @Test
    public void testUpdateActorByInvalidId(){
        Short id = 2;
        ActorInput input = new ActorInput();
        input.setFirstName("P".toUpperCase());

        when(mockActorRepo.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(ResponseStatusException.class, ()-> actorService.updateActor(id,input),"error message not shown correctly for incorrect id");
    }

    //IDK if I should include the next 2 tests since the method is private
    @Test
    public void testUpdateActorFromInputOnlyNames(){
        Short id = 2;

        ActorInput input = new ActorInput();
        input.setFirstName("t".toUpperCase());
        input.setLastName("L".toUpperCase());

        Actor returnedActor = new Actor(id,input.getFirstName(),input.getLastName());

        Assertions.assertNotNull(returnedActor, "returned actor should not be null");
        Assertions.assertEquals("T L", returnedActor.getFullName(), "updating new actor not working");
    }

    @Test
    public void testUpdateActorFromInputWithFilms(){
        Short idOne = 3;
        Short idTwo = 2;
        Film filmOne = new Film(idOne);
        Film filmTwo = new Film(idTwo);

        List<Short> filmIds = new ArrayList<>();
        filmIds.add(idOne);
        filmIds.add(idTwo);

        ActorInput input = new ActorInput();
        input.setFilms(filmIds);

        when(mockFilmRepo.findById(idOne)).thenReturn(Optional.of(filmOne));
        when(mockFilmRepo.findById(idTwo)).thenReturn(Optional.of(filmTwo));
        Actor savedActor = actorService.createActor(input);

        {
            Assertions.assertNotNull(savedActor, "saved actor should not be null");
            Assertions.assertEquals(2, savedActor.getFilms().size(), "Films not being added correctly");
            Assertions.assertTrue(savedActor.getFilms().contains(filmOne), "film id 1 not being added");
            Assertions.assertTrue(savedActor.getFilms().contains(filmTwo), "film id 1 not being added");
        }
    }

    @Test
    public void testDeleteActorInvalidId(){
        Short id = 3;

        when(mockActorRepo.existsById(id)).thenReturn(false);
        Assertions.assertThrowsExactly(ResponseStatusException.class, () -> actorService.deleteActor(id), "delete not working");
    }
    @Test
    public void testDeleteActor(){
        Short id = 3;

        when(mockActorRepo.existsById(id)).thenReturn(true);
        actorService.deleteActor(id);
        verify(mockActorRepo, times(1)).deleteById(id);
    }
}
