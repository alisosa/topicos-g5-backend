package com.ucu.topicos.services;

import com.ucu.topicos.mapper.FormMapper;
import com.ucu.topicos.model.FormQuestionEntity;
import com.ucu.topicos.repository.FormQuestionRepository;
import dtos.FormQuestionRequest;
import dtos.Question;
import dtos.FormQuestionsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormService {

    @Autowired
    private FormQuestionRepository formQuestionRepository;

    public FormQuestionsResponse getQuestions(){

        List<FormQuestionEntity> entities = formQuestionRepository.findAll();

        List<Question> questions = entities.stream().map(e -> new Question(e.getQuestion(),e.getType())).collect(Collectors.toList());

        return new FormQuestionsResponse(questions);
    }


    public void updateQuestionToForm (FormQuestionRequest dto) throws Exception{
        try {
            if (null != dto.getQuestions()){
                this.formQuestionRepository.deleteAll();
                this.formQuestionRepository.saveAll(FormMapper.mapToQuestion(dto));
            }


        }catch (Exception e){
            throw new Exception(e.getCause());
        }
    }


}
