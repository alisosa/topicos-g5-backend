package dtos;

import java.util.List;



public class FormQuestionRequest {

    private List<Question> questions;

    public FormQuestionRequest (){}

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
