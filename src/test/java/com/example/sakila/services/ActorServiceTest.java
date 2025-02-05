package com.example.sakila.services;

import com.example.sakila.dto.input.ActorInput;
import com.example.sakila.entities.Actor;
import com.example.sakila.entities.Film;
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
    public void testReturnActorByID(){
        Short id = 3;
        Actor actor = new Actor((short)3, "testing2", "sam");

        when(mockActorRepo.findById(id)).thenReturn(Optional.of(actor));
        Assertions.assertEquals(Optional.of(actor), actorService.getActorById(id), "returning actor by id not working");
    }
    //add test for wrong id

    @Test
    public void testReturnAllActors(){
        List<Actor> actors = new ArrayList<>();
        actors.add(new Actor("dave", "testing"));
        actors.add(new Actor("testing2", "testing"));
        actors.add(new Actor("sam", "testing"));

        when(mockActorRepo.findAll()).thenReturn(actors);
        Assertions.assertEquals(actors, actorService.listActors(), "Return all actors not working");
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
    public void testUpdateActorById(){
        Short id = (short)2;
        Actor oldActor = new Actor(id);
        oldActor.setFirstName("t".toUpperCase());
        oldActor.setLastName("l".toUpperCase());
        ActorInput input = new ActorInput();
        input.setFirstName("P".toUpperCase());

        when(mockActorRepo.findById(id)).thenReturn(Optional.of(oldActor));
        when(mockActorRepo.save(any(Actor.class))).then(returnsFirstArg());
        Actor updatedActor = actorService.updateActor(id,input);

        Assertions.assertEquals("P",updatedActor.getFirstName(), "Not updating by id correctly");

    }
    @Test
    public void testUpdateActorByInvalidId(){
        Short id = (short)2;
        ActorInput input = new ActorInput();
        input.setFirstName("P".toUpperCase());

        when(mockActorRepo.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(ResponseStatusException.class, ()-> actorService.updateActor(id,input),"error message not shown correctly for incorrect id");

    }

    @Test
    public void testUpdateActorFromInputOnlyNames(){
        String fName = "t";
        String lName = "L";
        //need to add the id check
        Actor savedActor = new Actor(fName.toUpperCase(),lName.toUpperCase());

        Assertions.assertEquals("T L", savedActor.getFullName(), "updating new actor not working");
    }

    @Test
    public void testUpdateActorFromInputWithFilms(){
        Short idOne = (short)3;
        Film filmOne = new Film(idOne);
        Short idTwo = (short)2;
        Film filmTwo = new Film(idTwo);

        List<Short> filmIds = new ArrayList<>();
        filmIds.add(idOne);
        filmIds.add(idTwo);

        ActorInput actorInput = new ActorInput();
        actorInput.setFilms(filmIds);

        Actor actor = new Actor("t", "l");

        when(mockFilmRepo.findById(idOne)).thenReturn(Optional.of(filmOne));
        when(mockFilmRepo.findById(idTwo)).thenReturn(Optional.of(filmTwo));
        Actor savedActor = actorService.createActor(actorInput);

        Assertions.assertEquals(2,savedActor.getFilms().size(), "Films not being added correctly");
        Assertions.assertTrue(savedActor.getFilms().contains(filmOne), "film id 1 not being added");
        Assertions.assertTrue(savedActor.getFilms().contains(filmTwo), "film id 1 not being added");

    }

    @Test
    public void testDeleteActor(){
        Short id = (short)3;

        when(mockActorRepo.existsById(id)).thenReturn(false);
        Assertions.assertThrowsExactly(ResponseStatusException.class, () -> actorService.deleteActor(id), "delete not working");
    }
}
