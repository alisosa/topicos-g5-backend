package com.ucu.topicos.mapper;

import com.ucu.topicos.model.FormQuestionEntity;
import dtos.FormQuestionRequest;

public class FormMapper {

    public static FormQuestionEntity mapToQuestion (FormQuestionRequest dto){
        FormQuestionEntity response = new FormQuestionEntity();
        response.setQuestion(dto.getQuestion());
        response.setType(dto.getType());
        response.setScorable(dto.isScorable());
        return response;
    }
}
