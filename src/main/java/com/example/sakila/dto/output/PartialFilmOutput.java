package com.example.sakila.dto.output;

import com.example.sakila.entities.Film;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Year;

@Getter
@AllArgsConstructor
public class PartialFilmOutput {
    private Short id;
    private String title;
    private Year releaseDate;

    public static PartialFilmOutput from(Film film){
        return new PartialFilmOutput(
                film.getId(),
                film.getTitle(),
                film.getReleaseYear()
        );
    }
}
