package com.ucu.topicos.services;

import com.ucu.topicos.mapper.FormMapper;
import com.ucu.topicos.model.FormQuestionEntity;
import com.ucu.topicos.repository.FormQuestionRepository;
import dtos.FormQuestionRequest;
import dtos.Provider;
import dtos.Question;
import dtos.FormQuestionsResponse;
import dtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormService {

    @Autowired
    private FormQuestionRepository formQuestionRepository;
    @Autowired
    private UserService userService;

    public FormQuestionsResponse getQuestions(String userToken){

        UserDTO userDTO = userService.verifyToken(userToken);
        Provider provider = new Provider();
        if (userDTO != null){
            provider.setRut(userDTO.getRut());
            provider.setEmail(userDTO.getEmail());
        }

        List<FormQuestionEntity> entities = formQuestionRepository.findAll();

        List<Question> questions = entities.stream().map(e -> new Question(e.getQuestion(),e.getType())).collect(Collectors.toList());

        return new FormQuestionsResponse(questions, provider);
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
