package com.example.sakila.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;

import java.time.Year;
import java.util.List;

@Entity
@Table(name = "film")
@Getter
@Setter
@NoArgsConstructor
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id")
    @Setter(AccessLevel.NONE)
    private Short id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;
    @Column(name = "release_year")
    private Year releaseYear;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    @Column(name = "length")
    private Short movieLength;

    //@Enumerated(EnumType.STRING)
    @Column(name = "rating")
    private String rating;

    @ManyToMany
    @JoinTable(
            name = "film_actor",
            joinColumns = {@JoinColumn(name = "film_id")},
            inverseJoinColumns = {@JoinColumn(name = "actor_id")}
    )
    private List<Actor> cast;

    @ManyToMany
    @JoinTable(
            name = "film_category",
            joinColumns = {@JoinColumn(name = "film_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")}
    )
    private List<Category> categories;

    @Column(name = "film_score")
    private Float score;

    @Column(name = "rental_rate")
    private Float rentalRate;
    @Column(name = "rental_duration")
    private Byte rentalDuration;

    @Formula("concat(rental_rate, ' for ', rental_duration, ' weeks')")
    @Setter(AccessLevel.NONE)
    private String rental;

    public String getRental(){
        return "£"+rentalRate + " for " + rentalDuration + " weeks";
    }

    public Film(Short id){
        this.id = id;
    }
    public Film(Short id, String title, List<Category> categories, String rating, Year releaseYear, Float score){
        this.id = id;
        this.title = title;
        this.categories = categories;
        this.rating = rating;
        this.releaseYear = releaseYear;
        this.score = score;
    }
    public Film(String title){
        this.title = title;
    }


    public Film(Short id,String title, String description, Year releaseYear, Language language, Short movieLength, String rating, List<Actor> cast, List<Category> categories, Float score, Float rentalRate, Byte rentalDuration){
        this.id = id;
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.language = language;
        this.movieLength = movieLength;
        this.rating =  rating;
        this.cast = cast;
        this.categories = categories;
        this.score = score;
        this.rentalRate = rentalRate;
        this.rentalDuration = rentalDuration;
    }
}
