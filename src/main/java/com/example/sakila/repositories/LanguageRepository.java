package com.example.sakila.repositories;

import com.example.sakila.entities.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Byte> {
}
