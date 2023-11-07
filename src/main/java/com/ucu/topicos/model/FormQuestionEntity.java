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

    public FormQuestionEntity (){};

    public FormQuestionEntity (String question, String type){
        this.question = question;
        this.type = type;
    }
}
