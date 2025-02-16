package com.example.sakila;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
public class SakilaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SakilaApiApplication.class, args);
	}

}
