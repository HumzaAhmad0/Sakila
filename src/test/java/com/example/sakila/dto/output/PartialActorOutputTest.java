package com.example.sakila.dto.output;

import com.example.sakila.entities.Actor;
import com.example.sakila.entities.Film;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PartialActorOutputTest {

    @Test
    public void testPartialFilmOutputFromFilm(){
        Actor actor = new Actor(
                (short)1,
                "Joe Blogs");

        PartialActorOutput partialActorOutput = PartialActorOutput.from(actor);

        Assertions.assertNotNull(partialActorOutput, "Partial actor output should not be null");

        Assertions.assertEquals(actor.getId(), partialActorOutput.getId(), "ID mismatch");
        Assertions.assertEquals(actor.getFullName(), partialActorOutput.getFullName(), "Full name mismatch");

    }

}
