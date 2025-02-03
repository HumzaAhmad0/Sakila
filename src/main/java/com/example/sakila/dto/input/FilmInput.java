package com.example.sakila.dto.input;

import com.example.sakila.dto.ValidationGroup;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.time.Year;
import java.util.List;

@Getter
public class FilmInput {
    @NotNull(groups = {ValidationGroup.Put.class, ValidationGroup.Post.class}, message = "Title cannot be null")
    @Size(min = 1, max = 128, message = "Title must be between 1-128 characters")
    private String title;

    private String description;
    private Year releaseYear;

    @NotNull(groups = {ValidationGroup.Put.class, ValidationGroup.Post.class}, message = "Language cannot be null")
    //@Positive
    @Min(value = 1, message = "Movie length cannot be below 1")
    private Byte language;
    private Short movieLength;

    @Pattern(regexp = "^(G|PG|PG-13|R|NC-17)$", message = "Rating can only be: 'G, PG, PG-13, R, NC-17'")
    private String rating;
    private List<Short> actors;

    //maybe have a validation?
    private List<Byte> genre;

    @DecimalMin(value = "0.00", inclusive = true, message = "Score cannot be below 0.00")
    @DecimalMax(value = "100.00", inclusive = true, message = "Score cannot be above 100.00")
    @Digits(integer = 3, fraction = 2, message = "Score can only be 3 digits before the decimal and 2 digits after e.g XXX.YY")
    private Float score;

    @NotNull(groups = {ValidationGroup.Put.class, ValidationGroup.Post.class})
    @DecimalMin(value = "0.00", inclusive = true, message = "Rental Rate cannot be below 0.00")
//    @DecimalMax(value = "100.00", inclusive = true)
    // issue with: out of range above 99.99
    @Digits(integer = 3, fraction = 2, message = "Rental rate can only be 3 digits before the decimal and 2 digits after e.g XXX.YY")
    private Float rentalRate;

    @NotNull(groups = {ValidationGroup.Put.class, ValidationGroup.Post.class}, message = "Rental Duration cannot be null")
    @Min(value = 1, message = "Rental Duration cannot be below 1")
    private Byte rentalDuration;
}
