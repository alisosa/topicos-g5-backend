package com.ucu.topicos.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "questions")
public class FormQuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question;
    private String type;
    private boolean scorable;
}
