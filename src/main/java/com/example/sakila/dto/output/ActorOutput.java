package com.example.sakila.dto.output;

import com.example.sakila.entities.Actor;
import com.example.sakila.entities.Film;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ActorOutput {
    private Short id;
    private String firstName;
    private String lastName;
    private String fullName;
    private List<PartialFilmOutput> films;

    public static ActorOutput from(Actor actor){
        return new ActorOutput(
                actor.getId(),
                actor.getFirstName(),
                actor.getLastName(),
                actor.getFullName(),
                actor.getFilms()
                        .stream()
                        .map(PartialFilmOutput::from)
                        .toList()
        );
    }
}
