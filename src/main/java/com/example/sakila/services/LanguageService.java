package com.example.sakila.services;

import com.example.sakila.repositories.LanguageRepository;
import com.example.sakila.entities.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {
    private final LanguageRepository languageRepository;
    
    @Autowired
    public LanguageService(LanguageRepository languageRepository){
        this.languageRepository = languageRepository;
    }
    
    public List<Language> listLanguages(){
        return languageRepository.findAll();
    }
}
