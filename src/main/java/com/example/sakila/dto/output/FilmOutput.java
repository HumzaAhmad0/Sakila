package com.example.sakila.dto.output;

import com.example.sakila.entities.Category;
import com.example.sakila.entities.Film;
import com.example.sakila.entities.Language;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.catalina.startup.Catalina;

import java.time.Year;
import java.util.List;

@Getter
@AllArgsConstructor
public class FilmOutput {

    private Short id;
    private String title;
    private String description;
    private Year releaseYear;
    private Language language;
    private Short movieLength;
    private String rating;
    private List<PartialActorOutput> cast;
    private List<PartialCategoryOutput> genre;
    private Float score;
    private Float rentalRate;
    private Byte rentalDuration;
    private String rental;

    public static FilmOutput from(Film film){
        return new FilmOutput(
                film.getId(),
                film.getTitle(),
                film.getDescription(),
                film.getReleaseYear(),
                film.getLanguage(),
                film.getMovieLength(),
                film.getRating(),
                film.getCast()
                        .stream()
                        .map(PartialActorOutput::from)
                        .toList(),
                film.getGenre()
                        .stream()
                        .map(PartialCategoryOutput::from)
                        .toList(),
                film.getScore(),
                film.getRentalRate(),
                film.getRentalDuration(),
                //for some reason it shows the previous un-updated one, but it does update properly
                film.getRental()
        );
    }
}
