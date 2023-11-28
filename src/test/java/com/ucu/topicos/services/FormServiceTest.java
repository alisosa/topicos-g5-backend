package com.ucu.topicos.services;

import com.ucu.topicos.model.FormQuestionEntity;
import com.ucu.topicos.repository.FormQuestionRepository;
import dtos.FormQuestionRequest;
import dtos.FormQuestionsResponse;
import dtos.UserDTO;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class FormServiceTest {

    @Mock
    private FormQuestionRepository formQuestionRepository;
    @Mock
    private UserService userService;
    @Mock UserDTO userDTO;
    @Mock
    FormQuestionEntity formQuestionEntity;

    @InjectMocks
    private FormService formService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetQuestionsWithValidToken() {
        String userToken = "userToken";
        when(userDTO.getRut()).thenReturn("rutNumber");
        when(userDTO.getEmail()).thenReturn("test@gmail.com");
        when(userService.verifyToken(userToken)).thenReturn(userDTO);
        when(formQuestionEntity.getQuestion()).thenReturn("Question One");
        when(formQuestionEntity.getType()).thenReturn("QuestionType");
        when(formQuestionRepository.findAll()).thenReturn(List.of(formQuestionEntity));

        FormQuestionsResponse response = formService.getQuestions(userToken);

        assertEquals("Question One", response.getQuestions().get(0).getQuestion());
        assertEquals("rutNumber", response.getProvider().getRut());
        assertEquals("test@gmail.com", response.getProvider().getEmail());
    }

    @Test
    public void testUpdateQuestionToForm() throws Exception {
        FormQuestionRequest request = new FormQuestionRequest();
        request.setQuestions(Collections.emptyList());

        formService.updateQuestionToForm(request);

        verify(formQuestionRepository, times(1)).deleteAll();
        verify(formQuestionRepository, times(1)).saveAll(anyList());
    }



}
