package com.ucu.topicos.mapper;

import com.ucu.topicos.model.FormQuestionEntity;
import dtos.FormQuestionRequest;
import dtos.Question;

import java.util.ArrayList;
import java.util.List;

public class FormMapper {

    public static List<FormQuestionEntity> mapToQuestion (FormQuestionRequest dto){
        List<FormQuestionEntity> response = new ArrayList<>();
        for (Question item: dto.getQuestions()) {
            FormQuestionEntity entity = new FormQuestionEntity();
            entity.setQuestion(item.getQuestion());
            entity.setType(item.getType());
            response.add(entity);
        }
        return response;
    }
}
