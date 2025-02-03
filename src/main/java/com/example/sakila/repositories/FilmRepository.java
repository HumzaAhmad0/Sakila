package com.example.sakila.repositories;

import com.example.sakila.entities.Film;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Year;
import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Short> {
    @Query("SELECT f FROM Film f " +
            "JOIN f.categories c " +
            "WHERE (:title IS NULL OR LOWER(f.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:categoryName IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :categoryName, '%'))) " +
            "AND (:rating IS NULL OR LOWER(f.rating) = LOWER(:rating)) " +
            "AND (:releaseYear IS NULL OR f.releaseYear = :releaseYear)")
    List<Film> findFilmsByFilters(@Param("title") String title,
                                  @Param("categoryName") String categoryName,
                                  @Param("rating") String rating,
                                  @Param("releaseYear") Year releaseYear
//                                  , Pageable pageable
    );


    Pageable pageOfFive = PageRequest.of(0, 5);
    Pageable secondPage = PageRequest.of(1, 5);
}
