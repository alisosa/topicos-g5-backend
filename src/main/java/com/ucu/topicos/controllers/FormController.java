package com.ucu.topicos.controllers;

import com.ucu.topicos.services.FormService;
import dtos.FormQuestionRequest;
import dtos.FormQuestionsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/form")
public class FormController {

    @Autowired
    private FormService formService;

    @GetMapping("/questions")
    public ResponseEntity<Object> obtainFormQuestions(){
        try{
            FormQuestionsResponse response = formService.getQuestions();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping ("/update")
    public ResponseEntity<Object> updateQuestionToForm(FormQuestionRequest requestDto){
        try{
            this.formService.updateQuestionToForm(requestDto);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
    }


}
