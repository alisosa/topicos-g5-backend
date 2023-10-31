package com.ucu.topicos.services;

import com.ucu.topicos.mapper.FormMapper;
import com.ucu.topicos.model.FormQuestionEntity;
import com.ucu.topicos.repository.FormQuestionRepository;
import dtos.FormQuestionRequest;
import dtos.FormQuestionsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormService {

    @Autowired
    private FormQuestionRepository formQuestionRepository;

    public List<FormQuestionsResponse> getQuestions(){

        List<FormQuestionEntity> entities = formQuestionRepository.findAll();

        return entities.stream().map(e -> new FormQuestionsResponse(e.getQuestion(),e.getType(), e.isScorable())).collect(Collectors.toList());
    }


    public void addQuestionToForm (FormQuestionRequest dto) throws Exception{
        this.validateParams(dto);
        this.formQuestionRepository.save(FormMapper.mapToQuestion(dto));
    }

    private void validateParams(FormQuestionRequest dto) throws Exception {
        if (dto.getQuestion() == null){
            throw new Exception("Question content cannot be null");
        }
        if (dto.getType() == null){
            throw new Exception("Question type cannot be null");
        }
    }
}
