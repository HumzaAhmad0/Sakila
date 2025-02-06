package com.example.sakila.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ActorTest {

    @Test
    public void testDefaultConstruct(){
        Actor newActor = new Actor();

        {
            Assertions.assertNotNull(newActor);
            Assertions.assertEquals("null null", newActor.getFullName(), "Default actor has other than 2 nulls");
            Assertions.assertNull(newActor.getFilms(), "Default actor should have null films");
        }
    }

    @Test
    public void testConstructWithValues(){
        Actor newActor = new Actor();
        newActor.setFirstName("Joe");
        newActor.setLastName("Blogs");

        {
            Assertions.assertNotNull(newActor);
            Assertions.assertEquals("Joe", newActor.getFirstName(), "Actor first name not Joe");
            Assertions.assertEquals("Blogs", newActor.getLastName(), "Actor last name not Blogs");
            Assertions.assertEquals("Joe Blogs", newActor.getFullName(), "Actor Full name not Joe Blogs");
            Assertions.assertNull(newActor.getFilms(), "Actor should have null films");
        }
    }
}
