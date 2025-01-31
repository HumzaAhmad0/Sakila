package com.example.sakila.dto;

import jakarta.validation.groups.Default;

public class ValidationGroup {
    public interface Post extends Default{}
    public interface Put extends Default{}
    public interface Patch extends Default{}
}
