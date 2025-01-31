package com.example.sakila.dto.input;

import com.example.sakila.dto.ValidationGroup;
import com.example.sakila.entities.Language;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    private Byte language;
    private Short movieLength;
    private String rating;
    private List<Short> actors;
}
