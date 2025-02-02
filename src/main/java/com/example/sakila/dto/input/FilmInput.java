package com.example.sakila.dto.input;

import com.example.sakila.dto.ValidationGroup;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.time.Year;
import java.util.List;

@Getter
public class FilmInput {
    @NotNull(groups = {ValidationGroup.Put.class, ValidationGroup.Post.class})
    @Size(min = 1, max = 128)
    private String title;

    private String description;
    private Year releaseYear;

    @NotNull(groups = {ValidationGroup.Put.class, ValidationGroup.Post.class})
    //@Positive
    private Byte language;
    private Short movieLength;

    @Pattern(regexp = "^(G|PG|PG-13|R|NC-17)$")
    private String rating;
    private List<Short> actors;
    private Byte genre;
}
