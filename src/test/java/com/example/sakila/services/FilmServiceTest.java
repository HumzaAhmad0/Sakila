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
    public void testGetFilmById(){
        Short id = 2;
        Film film = new Film(id);

        when(mockfilmRepository.findById(id)).thenReturn(Optional.of(film));
        Optional<Film> returnedFilm = filmService.getFilmById(id);

        {
        Assertions.assertNotNull(returnedFilm, "should not be null");
        Assertions.assertTrue(returnedFilm.isPresent());

        Film retrievedFilm = returnedFilm.get();
        Assertions.assertEquals(retrievedFilm.getId(),film.getId(), "find film by id not working");
        }
    }

    @Test
    public void testGetFilmByWrongId(){
        Short id = 2;
        when(mockfilmRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(ResponseStatusException.class, () -> filmService.getFilmById(id), "wrong id message not working");
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

        {
            Assertions.assertNotNull(result, "result should not be null");
            Assertions.assertEquals(3,result.size(), "returned should have 3 films");

            Assertions.assertEquals(films.getFirst().getId(), result.getFirst().getId(), "First id do not match");
            Assertions.assertEquals(films.getFirst().getTitle(), result.getFirst().getTitle(), "First title do not match");
            Assertions.assertEquals(films.getFirst().getCategories(), result.getFirst().getCategories(), "First categories do not match");
            Assertions.assertEquals(films.getFirst().getRating(), result.getFirst().getRating(), "First rating do not match");
            Assertions.assertEquals(films.getFirst().getReleaseYear(), result.getFirst().getReleaseYear(), "First Release Year do not match");
            Assertions.assertEquals(films.getFirst().getScore(), result.getFirst().getScore(), "First Score Year do not match");

            Assertions.assertEquals(films.get(1).getId(), result.get(1).getId(), "Second id do not match");
            Assertions.assertEquals(films.get(1).getTitle(), result.get(1).getTitle(), "Second title do not match");
            Assertions.assertEquals(films.get(1).getCategories(), result.get(1).getCategories(), "Second categories do not match");
            Assertions.assertEquals(films.get(1).getRating(), result.get(1).getRating(), "Second rating do not match");
            Assertions.assertEquals(films.get(1).getReleaseYear(), result.get(1).getReleaseYear(), "Second Release Year do not match");
            Assertions.assertEquals(films.get(1).getScore(), result.get(1).getScore(), "Second Score Year do not match");

            Assertions.assertEquals(films.getLast().getId(), result.getLast().getId(), "Last id do not match");
            Assertions.assertEquals(films.getLast().getTitle(), result.getLast().getTitle(), "Last title do not match");
            Assertions.assertEquals(films.getLast().getCategories(), result.getLast().getCategories(), "Last categories do not match");
            Assertions.assertEquals(films.getLast().getRating(), result.getLast().getRating(), "Last rating do not match");
            Assertions.assertEquals(films.getLast().getReleaseYear(), result.getLast().getReleaseYear(), "Last Release Year do not match");
            Assertions.assertEquals(films.getLast().getScore(), result.getLast().getScore(), "Last Score Year do not match");
        }
    }

    @Test
    public void testCreateFilm(){
        FilmInput input = new FilmInput();
        input.setTitle("Dave");
        Film film = new Film("Dave");

        when(mockfilmRepository.save(any(Film.class))).then(returnsFirstArg());
        Film returnedFilm = filmService.createFilm(input);

        Assertions.assertNotNull(returnedFilm, "returnedFilm should not be null");
        Assertions.assertEquals(returnedFilm.getTitle(), film.getTitle(), "creating new film not working");
    }

    @Test
    public void testUpdateFilmById() {
        Short id = (short) 2;

        Film oldFilm = new Film(id);
        oldFilm.setTitle("dave");

        FilmInput input = new FilmInput();
        input.setTitle("bob");

        when(mockfilmRepository.findById(id)).thenReturn(Optional.of(oldFilm));
        when(mockfilmRepository.save(any(Film.class))).then(returnsFirstArg());
        Film updatedFilm = filmService.updateFilm(id, input);

        {
            Assertions.assertEquals(oldFilm.getScore(), updatedFilm.getScore(), "other fields should be unchanged");
            Assertions.assertNotNull(updatedFilm, "updated film should not be null");
            Assertions.assertEquals(oldFilm.getId(), updatedFilm.getId(), "id should be the same");
            Assertions.assertEquals("bob", updatedFilm.getTitle(), "Update films not working");
        }
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
    public void testDeleteFilmWithInvalidId(){
        Short id = (short)3;
        when(mockfilmRepository.existsById(id)).thenReturn(false);
        Assertions.assertThrowsExactly(ResponseStatusException.class, ()-> filmService.deleteFilm(id),"deleting film id message not working");
    }

    @Test
    public void testDeleteFilm(){
        Short id = (short)3;
        when(mockfilmRepository.existsById(id)).thenReturn(true);
        filmService.deleteFilm(id);
        verify(mockfilmRepository, times(1)).deleteById(id);
    }
}
