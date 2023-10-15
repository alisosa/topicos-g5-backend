package com.ucu.topicos.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    private String id;

    @Column(length = 255)
    private String name;

    @Column(length = 255)
    private String mail;

    @Enumerated(EnumType.STRING)
    private ERole role;
}
