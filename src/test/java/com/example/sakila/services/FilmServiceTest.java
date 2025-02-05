package com.example.sakila.services;

import com.example.sakila.dto.input.FilmInput;
import com.example.sakila.entities.Category;
import com.example.sakila.entities.Film;
import com.example.sakila.repositories.ActorRepository;
import com.example.sakila.repositories.CategoryRepository;
import com.example.sakila.repositories.FilmRepository;
import com.example.sakila.repositories.LanguageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

public class FilmServiceTest {

    static FilmService filmService;
    static FilmRepository mockfilmRepository = mock(FilmRepository.class);
    static ActorRepository mockactorRepository = mock(ActorRepository.class);
    static LanguageRepository mocklanguageRepository = mock(LanguageRepository.class);
    static CategoryRepository mockcategoryRepository = mock(CategoryRepository.class);

    @BeforeAll
    public static void testSetUp(){
        filmService = new FilmService(mockfilmRepository,mockactorRepository,mocklanguageRepository,mockcategoryRepository);
    }

    @Test
    public void testGetFilmByID(){
        Short id = (short)2;
        Film film = new Film(id);

        when(mockfilmRepository.findById(id)).thenReturn(Optional.of(film));
        Assertions.assertEquals(Optional.of(film), filmService.getFilmById(id), "find film by id not working");
    }

    @Test
    public void testGetAllFilms(){
        String title = "ABC";
        String categoryName = "DEF";
        String rating = "PG";
        Year year = Year.of(2003);
        Integer sortByScore = 1;
        Short id =2;
        Short id2 =3;
        Short id3 =4;

        Category category = new Category("DEF");
        List<Category> categories= new ArrayList<>();
        categories.add(category);

        List<Film> films = new ArrayList<>();
        films.add(new Film(id,title,categories,rating,year,12.0F));
        films.add(new Film(id2,"A",categories,"G",year, 8.3F));
        films.add(new Film(id3,"BC",categories,rating,year, 12.5F));


        when(mockfilmRepository.findFilmsByFilters(title,categoryName,rating,year)).thenReturn(films);
        List<Film> result = filmService.listFilms(title, categoryName, rating, year, sortByScore);

        Assertions.assertEquals(films, result, "List films not working");
    }

    @Test
    public void testCreateFilm(){
        FilmInput input = new FilmInput();
        input.setTitle("Dave");
        Film film = new Film("Dave");

        when(mockfilmRepository.save(any(Film.class))).then(returnsFirstArg());
        Film returnedFilm = filmService.createFilm(input);

        Assertions.assertEquals(returnedFilm.getTitle(), film.getTitle(), "creating new film not working");
    }

    @Test
    public void testUpdateFilmById(){
        Short id = (short)2;

        Film oldFilm = new Film(id);
        oldFilm.setTitle("dave");
        FilmInput input = new FilmInput();
        input.setTitle("bob");

        when(mockfilmRepository.findById(id)).thenReturn(Optional.of(oldFilm));
        when(mockfilmRepository.save(any(Film.class))).then(returnsFirstArg());
        Film updatedFilm = filmService.updateFilm(id,input);

        Assertions.assertEquals("bob",updatedFilm.getTitle(), "Update films not working");
    }
    @Test
    public void testUpdateFilmByInvalidId(){
        Short id = (short)2;
        FilmInput input = new FilmInput();
        input.setTitle("bob");

        when(mockfilmRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrowsExactly(ResponseStatusException.class, ()-> filmService.updateFilm(id,input), "response to wrong id not working");
    }

    @Test
    public void testUpdateFilmWithIncorrectLanguage(){
        FilmInput input = new FilmInput();
        input.setLanguage((byte)2);
        Short id = (short)2;

        when(mocklanguageRepository.findById(input.getLanguage())).thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(ResponseStatusException.class, ()-> filmService.updateFilm(id,input), "response to wrong language not working");
    }
    @Test
    public void testUpdateFilmWithIncorrectGenre(){
        FilmInput input = new FilmInput();
        Byte invalidGenreId = (byte)1;
        Short id = (short)2;

        when(mockcategoryRepository.findById(invalidGenreId)).thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(ResponseStatusException.class, ()-> filmService.updateFilm(id,input), "response to wrong genre not working");
    }

    @Test
    public void testUpdateFilmWithIncorrectActors(){
        FilmInput input = new FilmInput();
        Short id = (short)2;
        Short invalidActorId = (short)1;

        when(mockactorRepository.findById(invalidActorId)).thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(ResponseStatusException.class, ()-> filmService.updateFilm(id,input), "response to wrong actor not working");
    }

    @Test
    public void testDeleteFilm(){
        Short id = (short)3;
        when(mockfilmRepository.existsById(id)).thenReturn(false);
        Assertions.assertThrowsExactly(ResponseStatusException.class, ()-> filmService.deleteFilm(id),"deleting film id message not working");

    }
}
