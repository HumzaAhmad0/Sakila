package com.example.sakila.dto.output;

import com.example.sakila.entities.Actor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PartialActorOutput {
    private short id;
    private String fullName;

    public static PartialActorOutput from(Actor actor){
        return new PartialActorOutput(
                actor.getId(),
                actor.getFullName());
    }
}
