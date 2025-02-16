package com.example.sakila.controllers;

import com.example.sakila.dto.output.CategoryOutput;
import com.example.sakila.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/genres")
    public List<CategoryOutput> getGenres(){
        return categoryService
                .listGenres()
                .stream()
                .map(CategoryOutput::from)
                .toList();
    }
}
