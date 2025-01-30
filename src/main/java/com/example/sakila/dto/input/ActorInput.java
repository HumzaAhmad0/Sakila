package com.example.sakila.dto.input;

import lombok.Getter;

import java.util.List;
@Getter
public class ActorInput {
    private String firstName;
    private String lastName;
    private List<Short> films;
}
