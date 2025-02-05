package com.example.sakila.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;

import java.util.List;

@Entity
@Table(name = "actor")
@Getter
@Setter
@NoArgsConstructor
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "actor_id")
    @Setter(AccessLevel.NONE)
    private Short id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;


    @Formula("concat(first_name, ' ', last_name)")
    @Setter(AccessLevel.NONE)
    private String fullName;

    @ManyToMany
    @JoinTable(
            name = "film_actor",
            joinColumns = {@JoinColumn(name = "actor_id")},
            inverseJoinColumns = {@JoinColumn(name = "film_id")}
    )
    private List<Film> films;

    public String getFullName(){
        return firstName + " " + lastName;
    }


    public Actor(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public Actor(Short id,String firstName, String lastName){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public Actor(List<Film> films){
        this.films = films;
    }
}

