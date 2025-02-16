package com.example.sakila.controllers;

import com.example.sakila.dto.output.LanguageOutput;
import com.example.sakila.entities.Language;
import com.example.sakila.services.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class LanguageController {

    private final LanguageService languageService;

    @Autowired
    public LanguageController(LanguageService languageService){
        this.languageService = languageService;
    }

    @GetMapping("/languages")
    public List<LanguageOutput> getLanguages(){
        return languageService
                .listLanguages()
                .stream()
                .map(LanguageOutput::from)
                .toList();
    }

}
