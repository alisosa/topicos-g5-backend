package dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FormQuestionRequest {

    private String question;
    private String type;
    private boolean scorable;

}
