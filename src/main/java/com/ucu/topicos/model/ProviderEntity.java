package com.ucu.topicos.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "providers")
public class ProviderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String rut;
    private Integer score;

}
