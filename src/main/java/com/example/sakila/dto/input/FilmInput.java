package com.example.sakila.dto.input;

import com.example.sakila.entities.Language;
import lombok.Getter;

import java.time.Year;
import java.util.List;

@Getter
public class FilmInput {
    private String title;
    private String description;
    private Year releaseYear;
    private Byte language;
    private Byte movieLength;
    private String rating;
    private List<Short> actors;
}
