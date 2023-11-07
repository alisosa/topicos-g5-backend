package dtos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Question {

    private String question;
    private String type;
    private Boolean scorable;

    public Question (String question, String type){
        this.question = question;
        this.type = type;
    }

    public Question (String question, String type, boolean scorable){
        this.question = question;
        this.type = type;
        this.scorable = scorable;
    }

}
