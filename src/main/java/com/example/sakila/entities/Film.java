package com.example.sakila.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;
import java.util.List;

@Entity
@Table(name = "film")
@Getter
@Setter
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
    private List<Category> genre;

    @Column(name = "film_score")
    private Float score;

}
