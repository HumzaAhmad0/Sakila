package com.example.sakila.dto.input;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import com.example.sakila.dto.ValidationGroup.*;

import java.util.List;
@Getter
public class ActorInput {
    @NotNull(groups = {Put.class, Post.class})
    @Size(min = 1, max = 45)
    private String firstName;

    @NotNull(groups = {Put.class, Post.class})
    @Size(min = 1, max = 45)
    private String lastName;

    private List<Short> films;
}
