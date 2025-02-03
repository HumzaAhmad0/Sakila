package com.example.sakila.dto.input;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import com.example.sakila.dto.ValidationGroup.*;

import java.util.List;
@Getter
public class ActorInput {
    @NotNull(groups = {Put.class, Post.class}, message = "First Name cannot be null")
    @Size(min = 1, max = 45, message = "First Name must be between 1-45 characters")
    private String firstName;

    @NotNull(groups = {Put.class, Post.class}, message = "Last Name cannot be null")
    @Size(min = 1, max = 45, message = "Last Name must be between 1-45 characters")
    private String lastName;

    private List<Short> films;
}
