package com.ucu.topicos.mapper;

import com.ucu.topicos.model.FormQuestionEntity;
import dtos.FormQuestionRequest;

import java.util.ArrayList;
import java.util.List;

public class FormMapper {

    public static List<FormQuestionEntity> mapToQuestion (FormQuestionRequest dto){
        List<FormQuestionEntity> response = new ArrayList<>();
        dto.getQuestions().stream().map(item -> response.add(new FormQuestionEntity(item.getQuestion(),item.getType())));
        return response;
    }
}
