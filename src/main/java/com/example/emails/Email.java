package com.example.emails;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Email {
    @Id
    private Long id;
    // outros campos, getters e setters
}
