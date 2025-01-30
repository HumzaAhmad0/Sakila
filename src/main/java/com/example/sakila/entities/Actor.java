package com.example.sakila.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import java.util.List;

@Entity
@Table(name = "actor")
@Getter
@Setter
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

    @ManyToMany(mappedBy = "cast")
    private List<Film> films;

    public String getFullName(){
        return firstName + " " + lastName;
    }


}
